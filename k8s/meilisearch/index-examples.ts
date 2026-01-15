#!/usr/bin/env bun
/**
 * Indexes Aidbox examples into Meilisearch from GitHub Actions artifact.
 *
 * This script:
 * 1. Fetches the latest examples-metadata artifact from GitHub Actions
 * 2. Downloads and extracts examples-metadata.json from the ZIP
 * 3. Creates temp index and indexes all examples documents
 * 4. Swaps temp index with main index (zero downtime)
 * 5. Deletes old temp index
 *
 * Environment variables required:
 * - MEILISEARCH_HOST_URL: Meilisearch server URL
 * - MEILISEARCH_API_KEY: Meilisearch API key
 * - GITHUB_TOKEN: GitHub Personal Access Token for API access
 */

import { MeiliSearch } from "meilisearch";

interface ExampleDocument {
  id: string;
  title: string;
  description: string;
  category: string;
  github_url: string;
  readme_url: string;
  features: string[];
  languages: string[];
}

interface ExamplesMetadata {
  examples: ExampleDocument[];
  features_list: string[];
  languages_list: string[];
}

interface GitHubArtifact {
  id: number;
  name: string;
  created_at: string;
  expired: boolean;
}

function log(message: string, data?: Record<string, any>) {
  if (data) {
    const extras = Object.entries(data)
      .map(([k, v]) => `${k}=${v}`)
      .join(" ");
    console.log(`${message} ${extras}`);
  } else {
    console.log(message);
  }
}

function validateEnv() {
  const required = ["MEILISEARCH_HOST_URL", "MEILISEARCH_API_KEY", "GITHUB_TOKEN"];
  const missing = required.filter((v) => !process.env[v]);

  if (missing.length > 0) {
    log("ERROR: Missing required environment variables:", { vars: missing.join(", ") });
    process.exit(1);
  }

  log("Environment variables validated");
}

async function fetchLatestArtifact(githubToken: string): Promise<GitHubArtifact | null> {
  const url = "https://api.github.com/repos/Aidbox/examples/actions/artifacts";
  const headers = {
    Authorization: `Bearer ${githubToken}`,
    Accept: "application/vnd.github.v3+json",
  };

  try {
    log("Fetching artifacts list from GitHub...");
    const response = await fetch(url, { headers });

    if (!response.ok) {
      log(`ERROR: GitHub API HTTP error: ${response.status} ${response.statusText}`);
      process.exit(1);
    }

    const data = await response.json();
    const artifacts = data.artifacts || [];
    log(`Found ${artifacts.length} total artifacts`);

    // Filter for examples-metadata artifacts that are not expired
    const examplesArtifacts = artifacts.filter(
      (a: GitHubArtifact) => a.name === "examples-metadata" && !a.expired
    );

    if (examplesArtifacts.length === 0) {
      log("WARNING: No valid examples-metadata artifacts found");
      return null;
    }

    // Sort by created_at and get the latest
    const latest = examplesArtifacts.sort(
      (a: GitHubArtifact, b: GitHubArtifact) => a.created_at.localeCompare(b.created_at)
    )[examplesArtifacts.length - 1];

    log("Latest artifact found:", {
      artifact_id: latest.id,
      created_at: latest.created_at,
    });

    return latest;
  } catch (error) {
    log(`ERROR: Failed to fetch artifacts: ${error}`);
    process.exit(1);
  }
}

async function downloadArtifact(githubToken: string, artifactId: number): Promise<ArrayBuffer> {
  const url = `https://api.github.com/repos/Aidbox/examples/actions/artifacts/${artifactId}/zip`;
  const headers = {
    Authorization: `Bearer ${githubToken}`,
    Accept: "application/vnd.github.v3+json",
  };

  try {
    log(`Downloading artifact ${artifactId}...`);

    // First request - GitHub will return 302 redirect
    const response = await fetch(url, {
      headers,
      redirect: "manual",
    });

    if (response.status === 302) {
      const redirectUrl = response.headers.get("Location");
      if (!redirectUrl) {
        log("ERROR: Got 302 but no Location header");
        process.exit(1);
      }

      log("Following redirect to Azure storage...");

      // Follow redirect WITHOUT Authorization header (Azure doesn't need it)
      const redirectResponse = await fetch(redirectUrl);
      if (!redirectResponse.ok) {
        log(`ERROR: Failed to download from redirect: ${redirectResponse.status}`);
        process.exit(1);
      }

      const zipBytes = await redirectResponse.arrayBuffer();
      log(`Artifact downloaded from redirect:`, { size: zipBytes.byteLength });
      return zipBytes;
    }

    if (!response.ok) {
      log(`ERROR: Failed to download artifact: ${response.status}`);
      process.exit(1);
    }

    const zipBytes = await response.arrayBuffer();
    log(`Artifact downloaded directly:`, { size: zipBytes.byteLength });
    return zipBytes;
  } catch (error) {
    log(`ERROR: Failed to download artifact: ${error}`);
    process.exit(1);
  }
}

async function extractMetadata(zipBytes: ArrayBuffer): Promise<ExamplesMetadata> {
  try {
    log("Extracting examples-metadata.json from ZIP...");

    // Use Bun's built-in unzipping
    const decoder = new TextDecoder();
    const zipBlob = new Blob([zipBytes]);

    // Bun doesn't have built-in ZIP, so we'll use a workaround
    // Write to temp file and use unzipping
    const tempFile = `/tmp/examples-${Date.now()}.zip`;
    await Bun.write(tempFile, zipBlob);

    // Use shell unzip to extract
    const proc = Bun.spawn(["unzip", "-p", tempFile, "examples-metadata.json"]);
    const output = await new Response(proc.stdout).text();

    // Clean up temp file
    await Bun.spawn(["rm", tempFile]).exited;

    const data = JSON.parse(output) as ExamplesMetadata;
    log(`Extracted metadata:`, { examples_count: data.examples.length });

    return data;
  } catch (error) {
    log(`ERROR: Failed to extract metadata: ${error}`);
    process.exit(1);
  }
}

function transformForMeilisearch(examplesData: ExamplesMetadata): ExampleDocument[] {
  log("Transforming data for Meilisearch...");

  const examples = examplesData.examples || [];

  if (examples.length === 0) {
    log("WARNING: No examples found in metadata");
    return [];
  }

  const documents: ExampleDocument[] = [];

  for (const example of examples) {
    // Validate required fields
    if (!example.id) {
      log("WARNING: Skipping example without id:", { example });
      continue;
    }

    if (!example.title) {
      log("WARNING: Skipping example without title:", { id: example.id });
      continue;
    }

    // Meilisearch doesn't allow slashes in document IDs
    const fixedExample = {
      ...example,
      id: example.id.replace(/\//g, "_"),
    };

    documents.push(fixedExample);
  }

  log(`Prepared ${documents.length} documents for indexing`);
  return documents;
}

async function reindexExamples(
  client: MeiliSearch,
  indexName: string,
  documents: ExampleDocument[]
) {
  const tempIndexName = `${indexName}_temp`;

  log(`\nStarting reindex for: ${indexName}`);
  log(`Total documents: ${documents.length}`);

  // 1. Delete temp index if exists from previous failed run
  try {
    await client.deleteIndex(tempIndexName);
    log(`Deleted index: ${tempIndexName}`);
  } catch (e) {
    // Index doesn't exist, that's fine
  }

  // 2. Create temp index with settings
  log(`Creating index: ${tempIndexName}`);
  const createTask = await client.createIndex(tempIndexName, { primaryKey: "id" });
  await client.waitForTask(createTask.taskUid);

  // 3. Apply settings to temp index
  const settings = {
    searchableAttributes: ["title", "description", "category", "features", "languages"],
    filterableAttributes: ["category", "features", "languages"],
    displayedAttributes: [
      "id",
      "title",
      "description",
      "category",
      "github_url",
      "readme_url",
      "features",
      "languages",
    ],
    rankingRules: ["words", "typo", "proximity", "attribute", "sort", "exactness"],
  };

  log(`Applying settings to index: ${tempIndexName}`);
  const settingsTask = await client.index(tempIndexName).updateSettings(settings);
  await client.waitForTask(settingsTask.taskUid);

  // 4. Add documents to temp index in batches
  const batchSize = 100;
  for (let i = 0; i < documents.length; i += batchSize) {
    const batch = documents.slice(i, i + batchSize);
    log(`Indexing ${i + 1}-${Math.min(i + batchSize, documents.length)}/${documents.length}`);

    const addTask = await client.index(tempIndexName).addDocuments(batch);
    await client.waitForTask(addTask.taskUid);
  }

  // 5. Check if main index exists
  let mainExists = false;
  try {
    await client.getIndex(indexName);
    mainExists = true;
  } catch (e) {
    mainExists = false;
  }

  if (!mainExists) {
    // Create empty main index first
    log(`Creating main index: ${indexName}`);
    const mainCreateTask = await client.createIndex(indexName, { primaryKey: "id" });
    await client.waitForTask(mainCreateTask.taskUid);
  }

  // 6. Swap indexes (atomic operation)
  log(`Swapping indexes: ${indexName} <-> ${tempIndexName}`);
  const swapTask = await client.swapIndexes([{ indexes: [indexName, tempIndexName] }]);
  await client.waitForTask(swapTask.taskUid);

  // 7. Delete old temp index (now contains old data)
  await client.deleteIndex(tempIndexName);
  log(`Deleted index: ${tempIndexName}`);

  log(`\nReindex completed successfully for: ${indexName}`);
}

async function main() {
  log("=".repeat(60));
  log("Starting Aidbox Examples Indexer");
  log("=".repeat(60));

  // Step 1: Validate environment
  validateEnv();

  const meilisearchUrl = process.env.MEILISEARCH_HOST_URL!.replace(/\/$/, "");
  const apiKey = process.env.MEILISEARCH_API_KEY!;
  const githubToken = process.env.GITHUB_TOKEN!;

  // Initialize Meilisearch client
  const client = new MeiliSearch({
    host: meilisearchUrl,
    apiKey,
  });

  // Step 2: Fetch latest artifact
  const artifact = await fetchLatestArtifact(githubToken);
  if (!artifact) {
    log("No artifact to process, exiting");
    process.exit(0);
  }

  // Step 3: Download artifact ZIP
  const zipBytes = await downloadArtifact(githubToken, artifact.id);

  // Step 4: Extract metadata
  const examplesData = await extractMetadata(zipBytes);

  // Step 5: Transform data
  const documents = transformForMeilisearch(examplesData);

  // Step 6: Reindex with temp/swap pattern
  await reindexExamples(client, "examples", documents);

  log("=".repeat(60));
  log("Indexing completed successfully");
  log("=".repeat(60));
}

main().catch((error) => {
  log(`FATAL ERROR: ${error}`);
  process.exit(1);
});

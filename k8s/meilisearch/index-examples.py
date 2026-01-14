#!/usr/bin/env python3
"""
Indexes Aidbox examples into Meilisearch from GitHub Actions artifact.

This script:
1. Fetches the latest examples-metadata artifact from GitHub Actions
2. Downloads and extracts examples-metadata.json from the ZIP
3. Configures the Meilisearch "examples" index
4. Indexes all examples documents

Environment variables required:
- MEILISEARCH_HOST_URL: Meilisearch server URL
- MEILISEARCH_API_KEY: Meilisearch API key
- GITHUB_TOKEN: GitHub Personal Access Token for API access
"""

import os
import sys
import json
import time
from io import BytesIO
from zipfile import ZipFile
from urllib.request import Request, urlopen, build_opener, HTTPRedirectHandler
from urllib.error import HTTPError, URLError


class NoRedirectHandler(HTTPRedirectHandler):
    """Custom handler that doesn't follow redirects"""
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        return None


def log(message, **kwargs):
    """Log message with optional key-value pairs"""
    if kwargs:
        extras = " ".join(f"{k}={v}" for k, v in kwargs.items())
        print(f"{message} {extras}", flush=True)
    else:
        print(message, flush=True)


def validate_env():
    """Validate required environment variables"""
    required = ["MEILISEARCH_HOST_URL", "MEILISEARCH_API_KEY", "GITHUB_TOKEN"]
    missing = [var for var in required if not os.getenv(var)]

    if missing:
        log("ERROR: Missing required environment variables:", vars=", ".join(missing))
        sys.exit(1)

    log("Environment variables validated")


def fetch_latest_artifact(github_token):
    """
    Fetch list of artifacts and return the latest examples-metadata artifact.

    Returns:
        dict: Artifact metadata with 'id' and 'created_at' fields, or None if not found
    """
    url = "https://api.github.com/repos/Aidbox/examples/actions/artifacts"
    headers = {
        "Authorization": f"Bearer {github_token}",
        "Accept": "application/vnd.github.v3+json"
    }

    try:
        log("Fetching artifacts list from GitHub...")
        req = Request(url, headers=headers)
        with urlopen(req, timeout=10) as response:
            data = json.loads(response.read().decode())

        artifacts = data.get("artifacts", [])
        log(f"Found {len(artifacts)} total artifacts")

        # Filter for examples-metadata artifacts that are not expired
        examples_artifacts = [
            a for a in artifacts
            if a.get("name") == "examples-metadata" and not a.get("expired", False)
        ]

        if not examples_artifacts:
            log("WARNING: No valid examples-metadata artifacts found")
            return None

        # Sort by created_at and get the latest
        latest = sorted(examples_artifacts, key=lambda x: x.get("created_at", ""))[-1]
        log(f"Latest artifact found:",
            artifact_id=latest["id"],
            created_at=latest.get("created_at"))

        return latest

    except HTTPError as e:
        log(f"ERROR: GitHub API HTTP error: {e.code} {e.reason}")
        sys.exit(1)
    except URLError as e:
        log(f"ERROR: Network error fetching artifacts: {e.reason}")
        sys.exit(1)
    except Exception as e:
        log(f"ERROR: Failed to fetch artifacts: {e}")
        sys.exit(1)


def download_artifact(github_token, artifact_id):
    """
    Download artifact ZIP from GitHub (handles 302 redirect to Azure).

    Args:
        github_token: GitHub PAT
        artifact_id: Artifact ID to download

    Returns:
        bytes: ZIP file contents
    """
    url = f"https://api.github.com/repos/Aidbox/examples/actions/artifacts/{artifact_id}/zip"
    headers = {
        "Authorization": f"Bearer {github_token}",
        "Accept": "application/vnd.github.v3+json"
    }

    try:
        log(f"Downloading artifact {artifact_id}...")

        # First request - GitHub will return 302 redirect
        # Use custom opener that doesn't follow redirects
        opener = build_opener(NoRedirectHandler())
        req = Request(url, headers=headers)

        try:
            response = opener.open(req, timeout=30)
            zip_bytes = response.read()
            log(f"Artifact downloaded directly:", size=len(zip_bytes))
            return zip_bytes
        except HTTPError as e:
            if e.code == 302:
                # Get redirect location
                redirect_url = e.headers.get("Location")
                if not redirect_url:
                    log("ERROR: Got 302 but no Location header")
                    sys.exit(1)

                log("Following redirect to Azure storage...")

                # Follow redirect WITHOUT Authorization header (Azure doesn't need it)
                redirect_req = Request(redirect_url)
                with urlopen(redirect_req, timeout=30) as redirect_response:
                    zip_bytes = redirect_response.read()
                    log(f"Artifact downloaded from redirect:", size=len(zip_bytes))
                    return zip_bytes
            else:
                raise

    except HTTPError as e:
        log(f"ERROR: HTTP error downloading artifact: {e.code} {e.reason}")
        error_body = e.read().decode() if e.fp else ""
        if error_body:
            log(f"Error details: {error_body}")
        sys.exit(1)
    except URLError as e:
        log(f"ERROR: Network error downloading artifact: {e.reason}")
        sys.exit(1)
    except Exception as e:
        log(f"ERROR: Failed to download artifact: {e}")
        sys.exit(1)


def extract_metadata(zip_bytes):
    """
    Extract examples-metadata.json from ZIP bytes.

    Args:
        zip_bytes: ZIP file as bytes

    Returns:
        dict: Parsed JSON data
    """
    try:
        log("Extracting examples-metadata.json from ZIP...")

        with ZipFile(BytesIO(zip_bytes)) as zip_file:
            # List files in ZIP
            file_list = zip_file.namelist()
            log(f"ZIP contains {len(file_list)} files")

            if "examples-metadata.json" not in file_list:
                log("ERROR: examples-metadata.json not found in ZIP")
                log("Files in ZIP:", files=", ".join(file_list))
                sys.exit(1)

            # Extract and parse JSON
            with zip_file.open("examples-metadata.json") as json_file:
                data = json.load(json_file)

            examples_count = len(data.get("examples", []))
            log(f"Extracted metadata:", examples_count=examples_count)

            return data

    except json.JSONDecodeError as e:
        log(f"ERROR: Invalid JSON in metadata file: {e}")
        sys.exit(1)
    except Exception as e:
        log(f"ERROR: Failed to extract metadata: {e}")
        sys.exit(1)


def transform_for_meilisearch(examples_data):
    """
    Transform examples data for Meilisearch indexing.

    Args:
        examples_data: Dict with 'examples', 'features_list', 'languages_list'

    Returns:
        list: Documents ready for indexing
    """
    log("Transforming data for Meilisearch...")

    examples = examples_data.get("examples", [])

    if not examples:
        log("WARNING: No examples found in metadata")
        return []

    # Keep all fields as-is
    # Expected fields: id, title, description, category, github_url, features, languages
    documents = []

    for example in examples:
        # Validate required fields
        if not example.get("id"):
            log("WARNING: Skipping example without id:", example=example)
            continue

        if not example.get("title"):
            log("WARNING: Skipping example without title:", id=example.get("id"))
            continue

        # Fix ID: Meilisearch doesn't allow slashes in document IDs
        # Replace / with _ (underscore)
        example_copy = example.copy()
        example_copy["id"] = example_copy["id"].replace("/", "_")

        documents.append(example_copy)

    log(f"Prepared {len(documents)} documents for indexing")
    return documents


def make_meilisearch_request(url, method, headers, data=None, retry_count=3):
    """
    Make HTTP request to Meilisearch with retry logic.

    Args:
        url: Request URL
        method: HTTP method (GET, POST, PUT, etc.)
        headers: Request headers dict
        data: Optional request body (will be JSON encoded)
        retry_count: Number of retries on failure

    Returns:
        dict: Response data (parsed JSON) or None
    """
    for attempt in range(retry_count):
        try:
            req_data = json.dumps(data).encode() if data else None
            req = Request(url, data=req_data, headers=headers, method=method)

            with urlopen(req, timeout=30) as response:
                if response.status in [200, 201, 202]:
                    response_data = response.read().decode()
                    return json.loads(response_data) if response_data else {}
                else:
                    log(f"WARNING: Unexpected status {response.status}")
                    return None

        except HTTPError as e:
            if e.code == 404 and method == "GET":
                # Index doesn't exist yet, this is OK
                return None
            log(f"Attempt {attempt + 1}/{retry_count} failed:",
                error=f"{e.code} {e.reason}")
            if attempt < retry_count - 1:
                time.sleep(2 ** attempt)  # Exponential backoff
            else:
                raise
        except URLError as e:
            log(f"Attempt {attempt + 1}/{retry_count} failed:", error=str(e.reason))
            if attempt < retry_count - 1:
                time.sleep(2 ** attempt)
            else:
                raise


def configure_index(meilisearch_url, api_key):
    """
    Create and configure the examples index in Meilisearch.

    Args:
        meilisearch_url: Meilisearch server URL
        api_key: Meilisearch API key
    """
    log("Configuring Meilisearch index...")

    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json"
    }

    # Create index if it doesn't exist
    index_url = f"{meilisearch_url}/indexes"
    index_data = {
        "uid": "examples",
        "primaryKey": "id"
    }

    try:
        # Check if index exists
        check_url = f"{meilisearch_url}/indexes/examples"
        existing = make_meilisearch_request(check_url, "GET", headers)

        if not existing:
            log("Creating new index...")
            make_meilisearch_request(index_url, "POST", headers, index_data)
            log("Index created")
        else:
            log("Index already exists")

        # Configure index settings
        settings_url = f"{meilisearch_url}/indexes/examples/settings"
        settings_data = {
            "searchableAttributes": [
                "title",
                "description",
                "category",
                "features",
                "languages"
            ],
            "filterableAttributes": [
                "category",
                "features",
                "languages"
            ],
            "displayedAttributes": [
                "id",
                "title",
                "description",
                "category",
                "github_url",
                "features",
                "languages"
            ],
            "rankingRules": [
                "words",
                "typo",
                "proximity",
                "attribute",
                "sort",
                "exactness"
            ]
        }

        log("Updating index settings...")
        make_meilisearch_request(settings_url, "PATCH", headers, settings_data)
        log("Index settings updated")

    except Exception as e:
        log(f"ERROR: Failed to configure index: {e}")
        sys.exit(1)


def index_documents(meilisearch_url, api_key, documents):
    """
    Index documents into Meilisearch examples index.

    Args:
        meilisearch_url: Meilisearch server URL
        api_key: Meilisearch API key
        documents: List of documents to index
    """
    if not documents:
        log("No documents to index")
        return

    log(f"Indexing {len(documents)} documents...")

    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json"
    }

    try:
        url = f"{meilisearch_url}/indexes/examples/documents"
        response = make_meilisearch_request(url, "POST", headers, documents)

        if response:
            task_uid = response.get("taskUid")
            log("Documents indexed successfully:", task_uid=task_uid)
        else:
            log("WARNING: Indexing may have failed (no response)")

    except Exception as e:
        log(f"ERROR: Failed to index documents: {e}")
        sys.exit(1)


def main():
    """Main execution flow"""
    log("=" * 60)
    log("Starting Aidbox Examples Indexer")
    log("=" * 60)

    # Step 1: Validate environment
    validate_env()

    meilisearch_url = os.getenv("MEILISEARCH_HOST_URL").rstrip("/")
    api_key = os.getenv("MEILISEARCH_API_KEY")
    github_token = os.getenv("GITHUB_TOKEN")

    # Step 2: Fetch latest artifact
    artifact = fetch_latest_artifact(github_token)
    if not artifact:
        log("No artifact to process, exiting")
        sys.exit(0)

    # Step 3: Download artifact ZIP
    zip_bytes = download_artifact(github_token, artifact["id"])

    # Step 4: Extract metadata
    examples_data = extract_metadata(zip_bytes)

    # Step 5: Transform data
    documents = transform_for_meilisearch(examples_data)

    # Step 6: Configure index
    configure_index(meilisearch_url, api_key)

    # Step 7: Index documents
    index_documents(meilisearch_url, api_key, documents)

    log("=" * 60)
    log("Indexing completed successfully")
    log("=" * 60)


if __name__ == "__main__":
    main()

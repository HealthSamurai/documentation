# AI Prompts

## How to use
Copy the prompt to a file in your repo.

Use the "include file" feature from your AI tool to include the prompt when chatting with your AI assistant. For example, with GitHub Copilot, use #<filename>, in Cursor, use @Files, and in Zed, use /file, in Claude Code use @<filename>.

## For creating Access Policies

Copy the following script, past it to your terminal and run it to generate promt file:

```bash
OUT="combined_access_policy_docs.md"
{
  echo "# Aidbox Access Policies - Combined Documentation"
  echo ""
  echo "# Access Policies Overview"
  echo ""
  curl -s "https://raw.githubusercontent.com/HealthSamurai/documentation/master/docs/access-control/authorization/access-policies.md"
  echo ""
  echo "# Access Policy Examples"
  echo ""
  curl -s "https://raw.githubusercontent.com/HealthSamurai/documentation/master/docs/tutorials/security-access-control-tutorials/accesspolicy-examples.md"
  echo ""
  echo "# Access Policy Best Practices"
  echo ""
  curl -s "https://raw.githubusercontent.com/HealthSamurai/documentation/master/docs/tutorials/security-access-control-tutorials/accesspolicy-best-practices.md"
  echo ""
} > "$OUT"
```

## For creating Search Parameters

Copy the following script, past it to your terminal and run it to generate promt file:

```bash
OUT="combined_search_parameter_docs.md"
{
  echo "# Aidbox Search Parameters - Combined Documentation"
  echo ""
  echo "# Search Parameters Overview"
  echo ""
  curl -s "https://raw.githubusercontent.com/HealthSamurai/documentation/master/docs/api/rest-api/fhir-search/searchparameter.md"
  echo ""
  echo "# Search Parameter Examples"
  echo ""
  curl -s "https://raw.githubusercontent.com/HealthSamurai/documentation/master/docs/tutorials/crud-search-tutorials/search-tutorials/custom-searchparameter-tutorial.md"
  echo ""
} > "$OUT"
```

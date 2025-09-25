# AI Prompts

## How to use
Copy the prompt to a file in your repo.

Use the "include file" feature from your AI tool to include the prompt when chatting with your AI assistant. For example, with GitHub Copilot, use #<filename>, in Cursor, use @Files, and in Zed, use /file, in Claude Code use @<filename>.

## For creating Access Policies

Use the following script to generate promt file:

```bash
#!/bin/bash
OUT="combined_access_policy_docs.md"
{
echo "# Aidbox Access Policies - Combined Documentation

---

# Access Policies Overview
"
curl -s "https://raw.githubusercontent.com/HealthSamurai/documentation/master/docs/access-control/authorization/access-policies.md"
echo "

---

# Access Policy Examples
"
curl -s "https://raw.githubusercontent.com/HealthSamurai/documentation/master/docs/tutorials/security-access-control-tutorials/accesspolicy-examples.md"
echo "

---

# Access Policy Best Practices
"
curl -s "https://raw.githubusercontent.com/HealthSamurai/documentation/master/docs/tutorials/security-access-control-tutorials/accesspolicy-best-practices.md"
} > "$OUT"
echo "Combined documentation saved to: $OUT"
```
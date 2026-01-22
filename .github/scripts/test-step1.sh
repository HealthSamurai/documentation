#!/bin/bash
set -e

COMMIT=${1:-e62d42d4923c43323e8d9fc476adde08a9302343}
GEMINI_API_KEY=${GEMINI_API_KEY}

if [ -z "$GEMINI_API_KEY" ]; then
  echo "Error: GEMINI_API_KEY environment variable not set"
  exit 1
fi

# Get changed files from commit
CHANGED=$(git diff --name-only --diff-filter=AM ${COMMIT}~1 ${COMMIT} -- 'docs/**/*.md' | grep -v 'docs/reference/' | grep -v 'docs/deprecated/' | grep -v 'assets/' | grep -v 'images/' | head -5)

if [ -z "$CHANGED" ]; then
  echo "No relevant changes"
  exit 0
fi

echo "Changed files:"
echo "$CHANGED"
echo "$CHANGED" > /tmp/changed_files.txt

# Collect changed files info
echo "=== Changed/new documentation files ===" > /tmp/changed_info.txt
while read -r file; do
  if [ -f "$file" ]; then
    echo "" >> /tmp/changed_info.txt
    echo "FILE: $file" >> /tmp/changed_info.txt
    TITLE=$(grep -m1 '^#' "$file" | sed 's/^#* //')
    echo "TITLE: $TITLE" >> /tmp/changed_info.txt
    echo "FIRST 50 LINES:" >> /tmp/changed_info.txt
    head -50 "$file" >> /tmp/changed_info.txt
  fi
done < /tmp/changed_files.txt

# Build prompt
cat .github/prompts/cross-links-step1.txt > /tmp/prompt1.txt
echo "" >> /tmp/prompt1.txt
echo "=== Changed files ===" >> /tmp/prompt1.txt
cat /tmp/changed_info.txt >> /tmp/prompt1.txt
echo "" >> /tmp/prompt1.txt
echo "=== Documentation structure ===" >> /tmp/prompt1.txt
grep -v '/deprecated/' docs/SUMMARY.md | head -300 >> /tmp/prompt1.txt

echo "Prompt size: $(wc -c < /tmp/prompt1.txt) bytes"

# Build JSON payload
echo "Building JSON payload..."
jq -n --rawfile prompt /tmp/prompt1.txt '{contents: [{parts: [{text: $prompt}]}], generationConfig: {responseMimeType: "application/json"}}' > /tmp/payload1.json
echo "Payload size: $(wc -c < /tmp/payload1.json) bytes"

# Call Gemini
echo "Calling Gemini (Step 1)..."
API_KEY=$(echo "$GEMINI_API_KEY" | tr -d '[:space:]')

HTTP_CODE=$(curl -s -w "%{http_code}" -o /tmp/gemini_response.json \
  -X POST "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${API_KEY}" \
  -H "Content-Type: application/json" \
  -d @/tmp/payload1.json)

echo "HTTP status: $HTTP_CODE"

if [ "$HTTP_CODE" != "200" ]; then
  echo "Gemini API error:"
  cat /tmp/gemini_response.json
  exit 1
fi

RESPONSE=$(cat /tmp/gemini_response.json)
RESULT=$(echo "$RESPONSE" | python3 -c "import sys,json; d=json.loads(sys.stdin.read(),strict=False); print(d.get('candidates',[{}])[0].get('content',{}).get('parts',[{}])[0].get('text',''))" 2>/dev/null)

if [ -z "$RESULT" ] || [ "$RESULT" = "null" ]; then
  echo "Error parsing Gemini response:"
  cat /tmp/gemini_response.json | head -50
  exit 1
fi

echo "Step 1 result:"
echo "$RESULT" | python3 -m json.tool

FILES_TO_CHECK=$(echo "$RESULT" | python3 -c "import sys,json; d=json.loads(sys.stdin.read(),strict=False); print('\n'.join(d.get('files_to_check',[])[:5]))" 2>/dev/null)

if [ -z "$FILES_TO_CHECK" ]; then
  echo "No files to check"
  exit 0
fi

# Filter out changed files from files_to_check
echo "$FILES_TO_CHECK" > /tmp/files_to_check_raw.txt
echo "" > /tmp/files_to_check.txt
while read -r check_file; do
  # Normalize paths
  check_file_norm=$(echo "$check_file" | sed 's|^docs/||')
  IS_CHANGED=false
  while read -r changed_file; do
    changed_file_norm=$(echo "$changed_file" | sed 's|^docs/||')
    if [ "$check_file_norm" = "$changed_file_norm" ]; then
      IS_CHANGED=true
      break
    fi
  done < /tmp/changed_files.txt

  if [ "$IS_CHANGED" = "false" ]; then
    echo "$check_file" >> /tmp/files_to_check.txt
  else
    echo "Skipping $check_file (it's in changed files)"
  fi
done < /tmp/files_to_check_raw.txt

FILES_TO_CHECK=$(cat /tmp/files_to_check.txt | grep -v '^$')
if [ -z "$FILES_TO_CHECK" ]; then
  echo "No files to check after filtering"
  exit 0
fi

echo ""
echo "Files to check:"
cat /tmp/files_to_check.txt

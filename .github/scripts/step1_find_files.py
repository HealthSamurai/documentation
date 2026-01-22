#!/usr/bin/env python3
"""
Step 1: Find related files that should link to new documentation
"""
import os
import sys
import json
import subprocess
import urllib.request
import urllib.parse

def get_changed_files(commit_sha):
    """Get changed .md files from commit"""
    cmd = [
        'git', 'diff', '--name-only', '--diff-filter=AM',
        f'{commit_sha}~1', commit_sha, '--', 'docs/'
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    files = [
        f for f in result.stdout.strip().split('\n')
        if f and f.endswith('.md')
        and 'docs/reference/' not in f and 'docs/deprecated/' not in f
        and '/assets/' not in f and '/images/' not in f
    ]
    return files[:5]  # Max 5 files

def collect_file_info(filepath, commit_sha=None):
    """Collect title and first 50 lines from file"""
    # Try to read from git if commit is specified
    if commit_sha:
        cmd = ['git', 'show', f'{commit_sha}:{filepath}']
        result = subprocess.run(cmd, capture_output=True, text=True)
        if result.returncode == 0:
            content = result.stdout
            lines = content.split('\n')
        else:
            # File might not exist in that commit
            return None
    elif os.path.exists(filepath):
        with open(filepath, 'r') as f:
            lines = f.readlines()
    else:
        return None

    # Find title (first # heading)
    title = ''
    for line in lines:
        if line.startswith('#'):
            title = line.lstrip('#').strip()
            break

    first_50 = '\n'.join(lines[:50])

    return {
        'file': filepath,
        'title': title,
        'content': first_50
    }

def build_prompt(changed_files_info):
    """Build prompt for Gemini"""
    # Read prompt template
    with open('.github/prompts/cross-links-step1.txt', 'r') as f:
        prompt = f.read()

    prompt += '\n\n=== Changed files ===\n'
    for info in changed_files_info:
        prompt += f'\nFILE: {info["file"]}\n'
        prompt += f'TITLE: {info["title"]}\n'
        prompt += f'FIRST 50 LINES:\n{info["content"]}\n'

    prompt += '\n=== Documentation structure ===\n'
    with open('docs/SUMMARY.md', 'r') as f:
        summary_lines = [l for l in f.readlines() if '/deprecated/' not in l]
        prompt += ''.join(summary_lines[:300])

    return prompt

def call_gemini(prompt, api_key):
    """Call Gemini API"""
    url = f'https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={api_key.strip()}'

    payload = {
        'contents': [{'parts': [{'text': prompt}]}],
        'generationConfig': {'responseMimeType': 'application/json'}
    }

    req = urllib.request.Request(
        url,
        data=json.dumps(payload).encode('utf-8'),
        headers={'Content-Type': 'application/json'}
    )

    with urllib.request.urlopen(req) as response:
        data = json.loads(response.read().decode('utf-8'))

    result_text = data['candidates'][0]['content']['parts'][0]['text']
    return json.loads(result_text)

def main():
    commit_sha = sys.argv[1] if len(sys.argv) > 1 else 'e62d42d4923c43323e8d9fc476adde08a9302343'
    api_key = os.environ.get('GEMINI_API_KEY')

    if not api_key:
        print('Error: GEMINI_API_KEY environment variable not set', file=sys.stderr)
        sys.exit(1)

    # Get changed files
    changed_files = get_changed_files(commit_sha)
    if not changed_files:
        print('No relevant changes')
        sys.exit(0)

    print('Changed files:')
    for f in changed_files:
        print(f'  {f}')

    # Collect file info
    changed_info = [collect_file_info(f, commit_sha) for f in changed_files]
    changed_info = [i for i in changed_info if i]  # Filter None

    # Build prompt
    prompt = build_prompt(changed_info)
    print(f'\nPrompt size: {len(prompt)} bytes')

    # Call Gemini
    print('Calling Gemini...')
    result = call_gemini(prompt, api_key)

    print('\nStep 1 result:')
    print(json.dumps(result, indent=2))

    # Filter out changed files from suggestions
    files_to_check = result.get('files_to_check', [])
    changed_paths = set(f.replace('docs/', '') for f in changed_files)

    filtered = []
    for check_file in files_to_check:
        check_path = check_file.replace('docs/', '')
        if check_path in changed_paths:
            print(f'Skipping {check_file} (it\'s in changed files)')
        else:
            filtered.append(check_file)

    if not filtered:
        print('\nNo files to check after filtering')
        sys.exit(0)

    # Write to output file
    with open('/tmp/files_to_check.txt', 'w') as f:
        f.write('\n'.join(filtered))

    print('\nFiles to check:')
    for f in filtered:
        print(f'  {f}')

if __name__ == '__main__':
    main()

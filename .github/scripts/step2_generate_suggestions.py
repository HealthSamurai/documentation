#!/usr/bin/env python3
"""
Step 2: Generate cross-link suggestions for target files
"""
import os
import sys
import json
import subprocess
import urllib.request

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
            return None
    elif os.path.exists(filepath):
        with open(filepath, 'r') as f:
            lines = f.readlines()
        lines = [l.rstrip('\n') for l in lines]
    else:
        return None

    # Find title
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

def read_target_files(files_list_path):
    """Read target files from list"""
    if not os.path.exists(files_list_path):
        return []

    with open(files_list_path, 'r') as f:
        files = [line.strip() for line in f if line.strip()]

    targets = []
    for filepath in files:
        # Normalize path
        if not filepath.startswith('docs/'):
            filepath = f'docs/{filepath}'

        if not os.path.exists(filepath):
            continue

        with open(filepath, 'r') as f:
            lines = f.readlines()

        # Add line numbers
        numbered = ''.join([f'    {i+1:5d}\t{line}' for i, line in enumerate(lines[:500])])

        targets.append({
            'file': filepath,
            'content': numbered
        })

    return targets

def build_prompt(changed_info, targets):
    """Build prompt for Gemini"""
    with open('.github/prompts/cross-links-step2.txt', 'r') as f:
        prompt = f.read()

    prompt += '\n\n=== NEW FILES ===\n'
    for info in changed_info:
        prompt += f'\nFILE: {info["file"]}\n'
        prompt += f'TITLE: {info["title"]}\n'
        prompt += f'FIRST 50 LINES:\n{info["content"]}\n'

    for target in targets:
        prompt += f'\n\nTARGET FILE: {target["file"]}\n'
        prompt += f'CONTENT (with line numbers):\n{target["content"]}\n'
        prompt += '---END---\n'

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

    # Read changed files list
    with open('/tmp/changed_files.txt', 'r') as f:
        changed_files = [line.strip() for line in f if line.strip()]

    # Collect changed file info
    changed_info = [collect_file_info(f, commit_sha) for f in changed_files]
    changed_info = [i for i in changed_info if i]

    # Read target files
    targets = read_target_files('/tmp/files_to_check.txt')

    if not targets:
        print('No target files to check')
        sys.exit(0)

    print(f'Target files: {len(targets)}')

    # Build prompt
    prompt = build_prompt(changed_info, targets)
    print(f'Prompt size: {len(prompt)} bytes')

    # Call Gemini
    print('Calling Gemini (Step 2)...')
    result = call_gemini(prompt, api_key)

    print('\nStep 2 result:')
    print(json.dumps(result, indent=2))

    suggestions = result.get('suggestions', [])
    if not suggestions:
        print('\nNo suggestions')
        sys.exit(0)

    # Write to output file
    with open('/tmp/suggestions.json', 'w') as f:
        json.dump(result, f)

    print(f'\nFound {len(suggestions)} suggestions')

if __name__ == '__main__':
    main()

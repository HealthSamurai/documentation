#!/usr/bin/env python3
"""
Generate weekly commits summary using Gemini API
"""
import os
import sys
import json
import subprocess
import urllib.request
import urllib.error
import time

def get_commits_info():
    """Collect commits from last 7 days"""
    # Check if there are any commits
    result = subprocess.run(
        ['git', 'rev-list', '--count', '--since=7 days ago', 'HEAD'],
        capture_output=True, text=True
    )
    commit_count = int(result.stdout.strip())

    if commit_count == 0:
        print('No commits in the last week')
        return None

    # Get detailed commit info with file changes
    result = subprocess.run(
        ['git', 'log', '--since=7 days ago', '--pretty=format:### %s%n**Author:** %an | **Date:** %ad%n',
         '--date=short', '--stat'],
        capture_output=True, text=True
    )
    commit_log = result.stdout[:5000]  # Limit to avoid huge output

    # Get most changed files
    result = subprocess.run(
        ['git', 'log', '--since=7 days ago', '--pretty=format:', '--name-only'],
        capture_output=True, text=True
    )
    files = [f for f in result.stdout.split('\n') if f]
    file_counts = {}
    for f in files:
        file_counts[f] = file_counts.get(f, 0) + 1

    most_changed = sorted(file_counts.items(), key=lambda x: x[1], reverse=True)[:15]

    # Build commits info text
    info = f"## Commits this week\n\n{commit_log}\n\n"
    info += f"## Overall stats\n\n- Total commits: {commit_count}\n\n"
    info += "## Most changed files\n"
    for file, count in most_changed:
        info += f"{count:4d} {file}\n"

    return info

def build_prompt(commits_info):
    """Build prompt for Gemini"""
    with open('.github/prompts/weekly-summary.txt', 'r') as f:
        template = f.read()

    return template.replace('{{COMMITS_INFO}}', commits_info)

def call_gemini(prompt, api_key):
    """Call Gemini API with retry logic"""
    url = f'https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={api_key.strip()}'

    payload = {
        'contents': [{'parts': [{'text': prompt}]}]
    }

    for attempt in range(1, 4):
        print(f'Attempt {attempt}/3')

        try:
            req = urllib.request.Request(
                url,
                data=json.dumps(payload).encode('utf-8'),
                headers={'Content-Type': 'application/json'}
            )

            with urllib.request.urlopen(req) as response:
                data = json.loads(response.read().decode('utf-8'))

            summary = data.get('candidates', [{}])[0].get('content', {}).get('parts', [{}])[0].get('text', '')

            if summary:
                print('Success!')
                return summary

            print('Empty response')

        except urllib.error.HTTPError as e:
            if e.code == 429:
                print('Rate limited, waiting 60s...')
                time.sleep(60)
                continue
            else:
                print(f'HTTP Error {e.code}: {e.read().decode()}')

        time.sleep(10)

    raise Exception('Failed to generate summary after 3 attempts')

def main():
    api_key = os.environ.get('GEMINI_API_KEY')

    if not api_key:
        print('Error: GEMINI_API_KEY environment variable not set', file=sys.stderr)
        sys.exit(1)

    print('Collecting commits info...')
    commits_info = get_commits_info()

    if not commits_info:
        sys.exit(0)

    print(f'Found commits, building prompt...')
    prompt = build_prompt(commits_info)
    print(f'Prompt size: {len(prompt)} bytes')

    print('Calling Gemini...')
    summary = call_gemini(prompt, api_key)

    print('\n=== Generated Summary ===')
    print(summary)

    # Write to output file
    with open('/tmp/summary.md', 'w') as f:
        f.write(summary)

    print(f'\nSummary written to /tmp/summary.md')

if __name__ == '__main__':
    main()

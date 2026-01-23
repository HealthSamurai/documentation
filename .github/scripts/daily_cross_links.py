#!/usr/bin/env python3
"""
Daily batch cross-links: process all changed files from last 24h,
create separate PR for each file
"""
import os
import sys
import json
import subprocess
import time

def get_changed_files_last_24h():
    """Get all .md files changed in last 24 hours"""
    cmd = [
        'git', 'log', '--since=24 hours ago', '--name-only',
        '--pretty=format:', '--diff-filter=AM', '--', 'docs/'
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)

    files = []
    seen = set()
    for line in result.stdout.split('\n'):
        f = line.strip()
        if (f and f.endswith('.md') and f not in seen
            and 'docs/reference/' not in f and 'docs/deprecated/' not in f
            and '/assets/' not in f and '/images/' not in f
            and not f.endswith('/SUMMARY.md') and not f.endswith('/README.md')):
            files.append(f)
            seen.add(f)

    return files

def get_changed_files_from_commit(commit_sha):
    """Get .md files changed in specific commit"""
    cmd = [
        'git', 'diff', '--name-only', '--diff-filter=AM',
        f'{commit_sha}~1', commit_sha, '--', 'docs/'
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)

    files = [
        f.strip() for f in result.stdout.split('\n')
        if f.strip() and f.strip().endswith('.md')
        and 'docs/reference/' not in f and 'docs/deprecated/' not in f
        and '/assets/' not in f and '/images/' not in f
        and not f.strip().endswith('/SUMMARY.md') and not f.strip().endswith('/README.md')
    ]

    return files

def get_file_title(filepath):
    """Extract title from markdown file"""
    try:
        with open(filepath, 'r') as f:
            for line in f:
                if line.startswith('#'):
                    return line.lstrip('#').strip()
    except:
        pass
    return os.path.basename(filepath)

def process_file(filepath, api_key):
    """
    Process single file:
    1. Find related files (step1)
    2. Generate suggestions (step2)
    3. Apply changes
    4. Create PR

    Returns: (success: bool, pr_url: str or None, error: str or None)
    """
    print(f"\n{'='*60}")
    print(f"Processing: {filepath}")
    print('='*60)

    # Get commit that added this file
    cmd = ['git', 'log', '--diff-filter=A', '--format=%H', '-1', '--', filepath]
    result = subprocess.run(cmd, capture_output=True, text=True)
    commit_sha = result.stdout.strip()

    if not commit_sha:
        print(f"Could not find commit for {filepath}")
        return False, None, "No commit found"

    print(f"Found in commit: {commit_sha[:8]}")

    # Write file to temp for scripts
    with open('/tmp/changed_files.txt', 'w') as f:
        f.write(filepath)

    # Step 1: Find related files
    print("\nStep 1: Finding related files...")
    result = subprocess.run(
        ['python3', '.github/scripts/step1_find_files.py', commit_sha],
        env={**os.environ, 'GEMINI_API_KEY': api_key},
        capture_output=True, text=True
    )

    if result.returncode != 0:
        print(f"Step 1 failed: {result.stderr}")
        return False, None, f"Step 1 failed: {result.stderr[:200]}"

    if not os.path.exists('/tmp/files_to_check.txt') or os.path.getsize('/tmp/files_to_check.txt') == 0:
        print("No related files found")
        return False, None, "No related files"

    with open('/tmp/files_to_check.txt', 'r') as f:
        target_files = [line.strip() for line in f if line.strip()]
    print(f"Target files: {target_files}")

    # Step 2: Generate suggestions
    print("\nStep 2: Generating suggestions...")
    result = subprocess.run(
        ['python3', '.github/scripts/step2_generate_suggestions.py', commit_sha],
        env={**os.environ, 'GEMINI_API_KEY': api_key},
        capture_output=True, text=True
    )

    if result.returncode != 0:
        print(f"Step 2 failed: {result.stderr}")
        return False, None, f"Step 2 failed: {result.stderr[:200]}"

    if not os.path.exists('/tmp/suggestions.json'):
        print("No suggestions generated")
        return False, None, "No suggestions"

    with open('/tmp/suggestions.json', 'r') as f:
        suggestions = json.load(f)

    if not suggestions.get('suggestions'):
        print("Empty suggestions")
        return False, None, "Empty suggestions"

    print(f"Generated {len(suggestions['suggestions'])} suggestions")

    # Apply suggestions
    print("\nApplying suggestions...")
    apply_script = """
import json, re, os
with open('/tmp/suggestions.json', 'r') as f:
    data = json.loads(f.read(), strict=False)
suggestions = data.get('suggestions', [])
if not suggestions:
    print('No suggestions')
    exit(0)
print(f'Applying {len(suggestions)} suggestions...')
def normalize(s):
    return re.sub(r'\\s+', ' ', s.strip())
for s in suggestions:
    target = s.get('target_file', '')
    op = s.get('operation', '')
    find = s.get('find', '')
    replace = s.get('replace', '')
    ln = s.get('line_number')
    print(f'--- {target} ({op})')
    if not os.path.exists(target):
        print('SKIP: not found')
        continue
    links = re.findall(r'\\]\\(([^)]+\\.md)\\)', replace)
    with open(target, 'r') as f:
        content = f.read()
    if any(l in content for l in links):
        print('SKIP: link exists')
        continue
    if op == 'append':
        with open(target, 'a') as f:
            f.write(replace + '\\n')
        print('DONE: appended')
    elif op == 'replace':
        if find and find in content:
            content = content.replace(find, replace, 1)
            with open(target, 'w') as f:
                f.write(content)
            print('DONE: exact match')
        elif find:
            norm = normalize(find)
            lines = content.split('\\n')
            done = False
            for i in range(len(lines)):
                for w in range(1, min(6, len(lines)-i+1)):
                    if normalize('\\n'.join(lines[i:i+w])) == norm:
                        with open(target, 'w') as f:
                            f.write('\\n'.join(lines[:i] + [replace] + lines[i+w:]))
                        print(f'DONE: fuzzy at line {i+1}')
                        done = True
                        break
                if done:
                    break
            if done:
                continue
        if ln:
            try:
                idx = int(ln) - 1
                lines = content.split('\\n')
                if 0 <= idx < len(lines):
                    with open(target, 'w') as f:
                        f.write('\\n'.join(lines[:idx+1] + [replace] + lines[idx+1:]))
                    print(f'DONE: line {ln} fallback')
                    continue
            except:
                pass
        print('SKIP: not found')
"""

    result = subprocess.run(['python3', '-c', apply_script], capture_output=True, text=True)
    print(result.stdout)

    # Check if any changes were made
    result = subprocess.run(['git', 'diff', '--quiet', 'docs/'], capture_output=True)
    if result.returncode == 0:
        print("No changes applied")
        return False, None, "No changes applied"

    # Create PR
    print("\nCreating PR...")
    title = get_file_title(filepath)
    branch = f"auto/cross-links-{os.path.basename(filepath).replace('.md', '')}-{int(time.time())}"

    # Configure git
    subprocess.run(['git', 'config', 'user.name', 'github-actions[bot]'], check=True)
    subprocess.run(['git', 'config', 'user.email', 'github-actions[bot]@users.noreply.github.com'], check=True)

    # Create branch and commit
    subprocess.run(['git', 'checkout', '-b', branch], check=True)
    subprocess.run(['git', 'add', 'docs/'], check=True)
    subprocess.run([
        'git', 'commit', '-m',
        f'docs: add cross-links for {title}\n\nCo-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>'
    ], check=True)
    subprocess.run(['git', 'push', 'origin', branch], check=True)

    # Create PR with gh
    pr_title = f"docs: add cross-links for {title}"
    pr_body = f"Auto-generated cross-links for `{filepath}`.\n\nPlease review."

    result = subprocess.run([
        'gh', 'pr', 'create',
        '--title', pr_title,
        '--body', pr_body,
        '--head', branch,
        '--base', 'master'
    ], capture_output=True, text=True, check=True)

    pr_url = result.stdout.strip()
    print(f"PR created: {pr_url}")

    # Return to master
    subprocess.run(['git', 'checkout', 'master'], check=True)

    return True, pr_url, None

def main():
    api_key = os.environ.get('GEMINI_API_KEY')
    if not api_key:
        print('Error: GEMINI_API_KEY not set', file=sys.stderr)
        sys.exit(1)

    # Check if test commit is provided
    if len(sys.argv) > 1:
        test_commit = sys.argv[1]
        print(f"Testing on commit: {test_commit}")
        files = get_changed_files_from_commit(test_commit)
    else:
        print("Collecting changed files from last 24 hours...")
        files = get_changed_files_last_24h()

    if not files:
        print("No files changed")
        sys.exit(0)

    print(f"\nFound {len(files)} changed files:")
    for f in files:
        print(f"  - {f}")

    results = []
    for filepath in files:
        success, pr_url, error = process_file(filepath, api_key)
        results.append({
            'file': filepath,
            'success': success,
            'pr_url': pr_url,
            'error': error
        })

        # Small delay between files to avoid rate limits
        time.sleep(2)

    # Summary
    print("\n" + "="*60)
    print("SUMMARY")
    print("="*60)

    successful = [r for r in results if r['success']]
    failed = [r for r in results if not r['success']]

    print(f"\nProcessed: {len(results)} files")
    print(f"Successful PRs: {len(successful)}")
    print(f"Failed: {len(failed)}")

    if successful:
        print("\n✅ Created PRs:")
        for r in successful:
            print(f"  - {r['file']}")
            print(f"    {r['pr_url']}")

    if failed:
        print("\n❌ Failed:")
        for r in failed:
            print(f"  - {r['file']}: {r['error']}")

if __name__ == '__main__':
    main()

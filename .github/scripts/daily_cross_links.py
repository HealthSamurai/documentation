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

def collect_suggestions_for_file(filepath, api_key):
    """
    Collect suggestions for single file without creating PR:
    1. Find related files (step1)
    2. Generate suggestions (step2)

    Returns: (success: bool, suggestions: list, error: str or None)
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

    # Add source file info to each suggestion
    for s in suggestions['suggestions']:
        s['source_file'] = filepath
        s['source_title'] = get_file_title(filepath)

    print(f"Generated {len(suggestions['suggestions'])} suggestions")
    return True, suggestions['suggestions'], None

def group_suggestions_by_target(all_suggestions):
    """Group suggestions by target_file"""
    grouped = {}
    for s in all_suggestions:
        target = s.get('target_file', '')
        if target not in grouped:
            grouped[target] = []
        grouped[target].append(s)
    return grouped

def apply_suggestions(suggestions):
    """Apply suggestions to target files"""
    print(f"\nApplying {len(suggestions)} suggestions...")

    # Normalize function
    def normalize(s):
        import re
        return re.sub(r'\s+', ' ', s.strip())

    applied_count = 0
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

        # Check if link already exists
        import re
        links = re.findall(r'\]\(([^)]+\.md)\)', replace)
        with open(target, 'r') as f:
            content = f.read()

        if any(l in content for l in links):
            print('SKIP: link exists')
            continue

        if op == 'append':
            with open(target, 'a') as f:
                f.write(replace + '\n')
            print('DONE: appended')
            applied_count += 1
        elif op == 'replace':
            if find and find in content:
                content = content.replace(find, replace, 1)
                with open(target, 'w') as f:
                    f.write(content)
                print('DONE: exact match')
                applied_count += 1
            elif find:
                norm = normalize(find)
                lines = content.split('\n')
                done = False
                for i in range(len(lines)):
                    for w in range(1, min(6, len(lines)-i+1)):
                        if normalize('\n'.join(lines[i:i+w])) == norm:
                            with open(target, 'w') as f:
                                f.write('\n'.join(lines[:i] + [replace] + lines[i+w:]))
                            print(f'DONE: fuzzy at line {i+1}')
                            done = True
                            applied_count += 1
                            break
                    if done:
                        break
                if done:
                    continue
            if ln:
                try:
                    idx = int(ln) - 1
                    lines = content.split('\n')
                    if 0 <= idx < len(lines):
                        with open(target, 'w') as f:
                            f.write('\n'.join(lines[:idx+1] + [replace] + lines[idx+1:]))
                        print(f'DONE: line {ln} fallback')
                        applied_count += 1
                        continue
                except:
                    pass
            print('SKIP: not found')

    return applied_count

def create_pr_for_target(target_file, suggestions):
    """Create PR for a target file with all its suggestions"""
    print(f"\n{'='*60}")
    print(f"Creating PR for target: {target_file}")
    print(f"Suggestions count: {len(suggestions)}")
    print('='*60)

    # Apply suggestions
    applied = apply_suggestions(suggestions)

    if applied == 0:
        print("No changes applied")
        return False, None, "No changes applied"

    # Check if any changes were made
    result = subprocess.run(['git', 'diff', '--quiet', 'docs/'], capture_output=True)
    if result.returncode == 0:
        print("No git changes detected")
        return False, None, "No changes"

    # Collect source files for PR description
    source_files = list(set(s.get('source_file', '') for s in suggestions))
    source_titles = list(set(s.get('source_title', '') for s in suggestions))

    # Create branch
    target_basename = os.path.basename(target_file).replace('.md', '')
    branch = f"auto/cross-links-target-{target_basename}-{int(time.time())}"

    # Configure git
    subprocess.run(['git', 'config', 'user.name', 'github-actions[bot]'], check=True)
    subprocess.run(['git', 'config', 'user.email', 'github-actions[bot]@users.noreply.github.com'], check=True)

    # Create branch and commit
    subprocess.run(['git', 'checkout', '-b', branch], check=True)
    subprocess.run(['git', 'add', 'docs/'], check=True)

    commit_msg = f'docs: add cross-links to {os.path.basename(target_file)}\n\n'
    commit_msg += f'Added links from {len(source_files)} source files:\n'
    for sf in source_files:
        commit_msg += f'- {sf}\n'

    subprocess.run(['git', 'commit', '-m', commit_msg], check=True)
    subprocess.run(['git', 'push', 'origin', branch], check=True)

    # Create PR
    pr_title = f"docs: add cross-links to {os.path.basename(target_file)}"
    pr_body = f"Auto-generated cross-links to `{target_file}`\n\n"
    pr_body += f"**Source files ({len(source_files)}):**\n"
    for sf, st in zip(source_files, source_titles):
        pr_body += f"- [{st}]({sf})\n"
    pr_body += f"\n**Changes applied:** {applied} suggestions\n"
    pr_body += "\nPlease review."

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

    # Phase 1: Collect all suggestions
    print("\n" + "="*60)
    print("PHASE 1: Collecting suggestions from all files")
    print("="*60)

    all_suggestions = []
    collection_errors = []

    for filepath in files:
        success, suggestions, error = collect_suggestions_for_file(filepath, api_key)
        if success and suggestions:
            all_suggestions.extend(suggestions)
            print(f"✓ {filepath}: {len(suggestions)} suggestions")
        else:
            collection_errors.append({'file': filepath, 'error': error})
            print(f"✗ {filepath}: {error}")

        # Small delay to avoid rate limits
        time.sleep(2)

    print(f"\nTotal suggestions collected: {len(all_suggestions)}")

    if not all_suggestions:
        print("No suggestions to process")
        sys.exit(0)

    # Phase 2: Group by target file
    print("\n" + "="*60)
    print("PHASE 2: Grouping suggestions by target file")
    print("="*60)

    grouped = group_suggestions_by_target(all_suggestions)
    print(f"\nTarget files to update: {len(grouped)}")
    for target, suggs in grouped.items():
        print(f"  - {target}: {len(suggs)} suggestions")

    # Phase 3: Create PRs for each target
    print("\n" + "="*60)
    print("PHASE 3: Creating PRs for each target file")
    print("="*60)

    pr_results = []
    for target_file, suggestions in grouped.items():
        success, pr_url, error = create_pr_for_target(target_file, suggestions)
        pr_results.append({
            'target': target_file,
            'success': success,
            'pr_url': pr_url,
            'error': error,
            'suggestions_count': len(suggestions)
        })

        # Small delay between PRs
        time.sleep(2)

    # Final Summary
    print("\n" + "="*60)
    print("FINAL SUMMARY")
    print("="*60)

    successful_prs = [r for r in pr_results if r['success']]
    failed_prs = [r for r in pr_results if not r['success']]

    print(f"\nSource files processed: {len(files)}")
    print(f"Total suggestions: {len(all_suggestions)}")
    print(f"Target files: {len(grouped)}")
    print(f"Successful PRs: {len(successful_prs)}")
    print(f"Failed PRs: {len(failed_prs)}")

    if collection_errors:
        print(f"\nCollection errors: {len(collection_errors)}")
        for e in collection_errors:
            print(f"  - {e['file']}: {e['error']}")

    if successful_prs:
        print("\n✅ Created PRs:")
        for r in successful_prs:
            print(f"  - {r['target']} ({r['suggestions_count']} suggestions)")
            print(f"    {r['pr_url']}")

    if failed_prs:
        print("\n❌ Failed PRs:")
        for r in failed_prs:
            print(f"  - {r['target']}: {r['error']}")

if __name__ == '__main__':
    main()

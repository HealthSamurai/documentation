#!/usr/bin/env python3
"""
Fix title case in SUMMARY.md files and update corresponding h1 headers in markdown files.
"""

import os
import sys
import re

# Words that should be lowercase in title case (unless first or last word)
LOWERCASE_WORDS = {
    # Articles
    'a', 'an', 'the',
    
    # Coordinating conjunctions
    'and', 'but', 'or', 'nor', 'for', 'yet', 'so',
    
    # Prepositions (common ones, usually 4 letters or less)
    'at', 'by', 'in', 'of', 'on', 'to', 'up', 'as', 'for', 'from',
    'into', 'like', 'over', 'with', 'upon', 'per', 'via',
    
    # Other
    'vs', 'v'
}

def should_be_capitalized(word, position, total_words):
    """Determine if a word should be capitalized according to title case rules."""
    # First or last word is always capitalized
    if position == 0 or position == total_words - 1:
        return True
    
    # Check if it's a word that should stay lowercase
    if word.lower() in LOWERCASE_WORDS:
        return False
    
    # Otherwise, it should be capitalized
    return True

def fix_word_case(word, position, total_words):
    """Fix the case of a word according to title case rules."""
    # Handle punctuation at the end (but not closing parenthesis if word starts with open parenthesis)
    trailing_punct = ''
    if word and word[-1] in ',.;:!?':
        trailing_punct = word[-1]
        word = word[:-1]
    elif word and word[-1] == ')' and not word.startswith('('):
        # Only treat ) as punctuation if the word doesn't start with (
        trailing_punct = word[-1]
        word = word[:-1]
    
    # If word is in quotes or backticks, leave it as-is
    if ((word.startswith('"') and word.endswith('"')) or 
        (word.startswith("'") and word.endswith("'")) or
        (word.startswith('`') and word.endswith('`'))):
        return word + trailing_punct
    
    # Handle words that start with parenthesis
    if word.startswith('('):
        # Check if it's (/
        if word.startswith('(/'):
            inner_word = word[2:]  # Remove (/
            if inner_word and inner_word[0].islower():
                return '(/' + inner_word.capitalize() + trailing_punct
            else:
                return word + trailing_punct
        else:
            # Just regular parenthesis
            inner_word = word[1:]  # Remove (
            if inner_word:
                # Process the inner word recursively
                fixed_inner = fix_word_case(inner_word, position, total_words)
                return '(' + fixed_inner
            else:
                return word + trailing_punct
    
    # Check for words with special characters (like $, /, etc.)
    if any(char in word for char in ['$', '/', '\\', '@', '#', '%', '&']):
        # These are likely technical terms, leave them as-is
        return word + trailing_punct
    
    # Check for version numbers or similar patterns
    if re.match(r'^v?\d+(\.\d+)*$', word, re.IGNORECASE):
        return word + trailing_punct
    
    # If word is already all uppercase and longer than 1 char, assume it's an acronym
    if word.isupper() and len(word) > 1:
        return word + trailing_punct
    
    # If word has mixed case (like mCode, AidboxProfile), preserve it
    if not word.islower() and not word.isupper() and any(c.islower() for c in word) and any(c.isupper() for c in word):
        return word + trailing_punct
    
    # Handle hyphenated words
    if '-' in word:
        parts = word.split('-')
        fixed_parts = []
        for i, part in enumerate(parts):
            # For hyphenated words, capitalize each part unless it's a small word
            if part.isupper() and len(part) > 1:
                # Keep acronyms as-is
                fixed_parts.append(part)
            elif part.islower() and (i == 0 or not part in LOWERCASE_WORDS):
                fixed_parts.append(part.capitalize())
            elif part.islower() and part in LOWERCASE_WORDS and i > 0:
                fixed_parts.append(part.lower())
            else:
                # Keep existing capitalization for mixed case
                fixed_parts.append(part)
        return '-'.join(fixed_parts) + trailing_punct
    
    # Fix if word is all lowercase and should be capitalized
    if word.islower() and should_be_capitalized(word, position, total_words):
        return word.capitalize() + trailing_punct
    
    # Special case: if it's the first word and it's not capitalized, capitalize it
    if position == 0 and len(word) > 0 and word[0].islower():
        return word.capitalize() + trailing_punct
    
    # Otherwise, keep the existing case
    return word + trailing_punct

def fix_title_case(title):
    """Fix title case for a given title."""
    # Skip titles that are entirely in backticks (code)
    if title.startswith('`') and title.endswith('`'):
        return title
    
    # Skip titles that start with $ or / (likely commands or paths)
    if title.startswith('$') or title.startswith('/'):
        return title
    
    # Split title into words, preserving special characters
    words = re.findall(r'[^\s]+', title)
    if not words:
        return title
    
    fixed_words = []
    for i, word in enumerate(words):
        # Skip words in backticks
        if word.startswith('`') and word.endswith('`'):
            fixed_words.append(word)
            continue
        
        fixed_word = fix_word_case(word, i, len(words))
        fixed_words.append(fixed_word)
    
    return ' '.join(fixed_words)

def update_h1_in_file(file_path, new_title):
    """Update the h1 header in a markdown file."""
    if not os.path.exists(file_path):
        print(f"  Warning: File not found: {file_path}")
        return False
    
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    # Find the first h1 header
    h1_found = False
    for i, line in enumerate(lines):
        if line.strip().startswith('# '):
            old_h1 = line.strip()[2:].strip()
            if old_h1 != new_title:
                lines[i] = f"# {new_title}\n"
                print(f"  Updated h1 in {file_path}")
                print(f"    Old: {old_h1}")
                print(f"    New: {new_title}")
                h1_found = True
                
                # Write the updated content back
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.writelines(lines)
                return True
            else:
                print(f"  h1 already correct in {file_path}")
                return False
    
    if not h1_found:
        print(f"  Warning: No h1 header found in {file_path}")
    
    return False

def fix_title_case_in_summary(dry_run=False):
    """Fix title case in SUMMARY.md files and update corresponding files."""
    summary_files = [
        'docs/SUMMARY.md',
        'docs-new/forms/docs/SUMMARY.md'
    ]
    
    total_fixed = 0
    files_updated = 0
    
    for summary_file in summary_files:
        if not os.path.exists(summary_file):
            continue
        
        print(f"\nProcessing {summary_file}...")
        
        with open(summary_file, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        new_lines = []
        summary_modified = False
        
        for line_num, line in enumerate(lines, 1):
            # Check if line contains a title (markdown link)
            match = re.match(r'^(\s*\*\s*\[)([^\]]+)(\]\()([^)]+)(\).*)$', line)
            if match:
                indent, title, middle, file_path, rest = match.groups()
                
                # Fix the title case
                fixed_title = fix_title_case(title)
                
                if fixed_title != title:
                    print(f"\nLine {line_num}: Fixing title")
                    print(f"  Old: {title}")
                    print(f"  New: {fixed_title}")
                    
                    if not dry_run:
                        # Update the line in SUMMARY.md
                        new_line = f"{indent}{fixed_title}{middle}{file_path}{rest}\n"
                        new_lines.append(new_line)
                        summary_modified = True
                        total_fixed += 1
                        
                        # Update the h1 in the corresponding file
                        full_file_path = os.path.join(os.path.dirname(summary_file), file_path)
                        if update_h1_in_file(full_file_path, fixed_title):
                            files_updated += 1
                    else:
                        new_lines.append(line)
                else:
                    new_lines.append(line)
            else:
                new_lines.append(line)
        
        # Write the updated SUMMARY.md
        if summary_modified and not dry_run:
            with open(summary_file, 'w', encoding='utf-8') as f:
                f.writelines(new_lines)
            print(f"\nUpdated {summary_file}")
    
    print(f"\nSummary:")
    print(f"  Titles fixed in SUMMARY.md: {total_fixed}")
    print(f"  Markdown files updated: {files_updated}")
    
    if dry_run:
        print("\nThis was a dry run. Use --fix to apply changes.")
    
    return 0 if total_fixed == 0 else 1

if __name__ == "__main__":
    dry_run = '--fix' not in sys.argv
    
    if dry_run:
        print("Running in dry-run mode. Use --fix to apply changes.\n")
    
    sys.exit(fix_title_case_in_summary(dry_run))
#!/usr/bin/env python3
"""
Check that titles in SUMMARY.md follow title case conventions.
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

def check_word_case(word, position, total_words):
    """Check if a word follows proper case conventions."""
    # Handle punctuation at the end (but not closing parenthesis if word starts with open parenthesis)
    trailing_punct = ''
    if word and word[-1] in ',.;:!?':
        trailing_punct = word[-1]
        word = word[:-1]
    elif word and word[-1] == ')' and not word.startswith('('):
        # Only treat ) as punctuation if the word doesn't start with (
        trailing_punct = word[-1]
        word = word[:-1]
    
    # If word is in quotes, leave it as-is
    if (word.startswith('"') and word.endswith('"')) or (word.startswith("'") and word.endswith("'")):
        return True, word + trailing_punct
    
    # Handle words that start with parenthesis
    if word.startswith('('):
        # Check if it's (/
        if word.startswith('(/'):
            inner_word = word[2:]  # Remove (/
            if inner_word and inner_word[0].islower():
                expected = '(/' + inner_word.capitalize() + trailing_punct
                return False, expected
            else:
                return True, word + trailing_punct
        else:
            # Just regular parenthesis
            inner_word = word[1:]  # Remove (
            if inner_word:
                # Process the inner word recursively
                is_correct, expected_inner = check_word_case(inner_word, position, total_words)
                if not is_correct:
                    return False, '(' + expected_inner
                else:
                    return True, word + trailing_punct
            else:
                return True, word + trailing_punct
    
    # Check for words with special characters (like $, /, etc.)
    if any(char in word for char in ['$', '/', '\\', '@', '#', '%', '&']):
        # These are likely technical terms, leave them as-is
        return True, word + trailing_punct
    
    # Check for version numbers or similar patterns
    if re.match(r'^v?\d+(\.\d+)*$', word, re.IGNORECASE):
        return True, word + trailing_punct
    
    # If word has mixed case (like mCode, AidboxProfile), preserve it
    if not word.islower() and not word.isupper() and any(c.islower() for c in word) and any(c.isupper() for c in word):
        return True, word + trailing_punct
    
    # SIMPLIFIED LOGIC: Only check if a word is all lowercase when it should be capitalized
    if word.islower() and should_be_capitalized(word, position, total_words):
        # This word is all lowercase but should be capitalized
        expected = word.capitalize() + trailing_punct
        return False, expected
    
    # Special case: first word must always be capitalized
    if position == 0 and len(word) > 0 and word[0].islower():
        expected = word.capitalize() + trailing_punct
        return False, expected
    
    # Otherwise, accept whatever case the author chose
    return True, word + trailing_punct

def check_title_case(title):
    """Check if a title follows title case conventions."""
    # Skip titles that are entirely in backticks (code)
    if title.startswith('`') and title.endswith('`'):
        return True, title
    
    # Split title into words, preserving special characters
    words = re.findall(r'[^\s]+', title)
    if not words:
        return True, title
    
    issues = []
    expected_words = []
    
    for i, word in enumerate(words):
        # Skip words in backticks
        if word.startswith('`') and word.endswith('`'):
            expected_words.append(word)
            continue
            
        is_correct, expected_word = check_word_case(word, i, len(words))
        expected_words.append(expected_word)
        
        if not is_correct:
            issues.append(f"'{word}' should be '{expected_word}'")
    
    expected_title = ' '.join(expected_words)
    return len(issues) == 0, expected_title

def check_title_case_in_summary():
    """Check SUMMARY.md files for proper title case."""
    summary_files = [
        'docs/SUMMARY.md',
        'docs-new/forms/docs/SUMMARY.md'
    ]
    
    errors_found = False
    
    for summary_file in summary_files:
        if not os.path.exists(summary_file):
            continue
            
        print(f"Checking {summary_file}...")
        
        with open(summary_file, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        for line_num, line in enumerate(lines, 1):
            # Check if line contains a title (markdown link)
            title_match = re.search(r'\[(.*?)\]', line)
            if title_match:
                title = title_match.group(1)
                
                # Skip empty titles or titles that are just code
                if not title or title.startswith('$') or title.startswith('/'):
                    continue
                
                is_correct, expected_title = check_title_case(title)
                
                if not is_correct:
                    errors_found = True
                    print(f"WARNING: Line {line_num}: Title case issue")
                    print(f"  Found: {title}")
                    print(f"  Expected: {expected_title}")
                    print(f"  Full line: {line.strip()}")
                    print()
    
    if errors_found:
        print("WARNING: Found title case issues in SUMMARY.md files.")
        print("Please review the suggestions above.")
        # Return 0 for now (warning only, not error)
        return 0
    else:
        print("SUCCESS: All titles follow title case conventions.")
        return 0

if __name__ == "__main__":
    sys.exit(check_title_case_in_summary())
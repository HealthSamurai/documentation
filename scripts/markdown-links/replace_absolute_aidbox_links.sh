#!/bin/bash

# This script converts absolute URLs (https://docs.aidbox.app/) to relative links in markdown files.
# It processes all .md files in the docs directory and:
# 1. Identifies links with absolute Aidbox URLs
# 2. Calculates the relative path from the current file to the target file
# 3. Replaces the absolute URL with a relative one
# 4. Falls back to "broken-reference" if the target file doesn't exist

set -euo pipefail

find docs/ -type f -name '*.md' | while read srcfile; do
    grep -q 'https://docs.aidbox.app' "$srcfile" || continue
    echo "Processing $srcfile"
    export srcfile
    perl -pe '
s{\[([^\]]+)\]\((https://docs\.aidbox\.app[^\)]*)\)}{
  my ($text, $absurl) = ($1, $2);
  my $relpath = $absurl;
  $relpath =~ s!https://docs\.aidbox\.app/!docs/!;
  $relpath .= ".md" unless $relpath =~ /\.[a-zA-Z0-9]+$/ || $relpath =~ /[#?]$/ || $relpath =~ /\/$/;
  my $srcfile = $ENV{srcfile};
  my $reldir = $srcfile; $reldir =~ s!/[^/]+$!!;
  my $relurl = `test -e "$relpath" && realpath --relative-to="$reldir" "$relpath" 2>/dev/null`;
  $relurl =~ s/\s+$//;
  $relurl =~ s!(\.{2})([^/])!$1/$2!g;
  $relurl ? "[$text]($relurl)" : "[$text](broken-reference)";
}gex
' "$srcfile" > "$srcfile.tmp"
    mv "$srcfile.tmp" "$srcfile"
done

echo "All files processed."

#!/bin/bash

# For all markdown files in docs/, append 'README.md' to relative links ending with a slash,
# except for 'broken-reference/' and absolute links (http/https), including links with anchors.
find docs/ -type f -name '*.md' | while read file; do
  # 1. [text](some/path/#anchor) -> [text](some/path/README.md#anchor)
  perl -pi -e '
    s{(\[[^\]]*\]\()((?!https?://)[^\)\s]+/)(#[^)]*\))}{
      $2 eq "broken-reference/" ? "$1$2$3" : "$1$2README.md$3"
    }ge;
    # 2. [text](some/path/) -> [text](some/path/README.md)
    s{(\[[^\]]*\]\()((?!https?://)[^\)\s]+/)(\))}{
      $2 eq "broken-reference/" ? "$1$2$3" : "$1$2README.md$3"
    }ge;
  ' "$file"
done

echo "All relative links ending with a slash (except broken-reference/ and absolute links) now point to README.md, including links with anchors, in all markdown files in docs/"

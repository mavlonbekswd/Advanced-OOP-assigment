#!/bin/sh
# Approximate word count per report section.
# Counts the English prose only: stops at the Uzbek block, and skips headings, the
# italic status line, horizontal rules and code fences, so the number is close to
# what Word will report.
dir=$(dirname "$0")
total=0
for file in "$dir"/0*.md "$dir"/appendix-*.md; do
    count=$(awk '
        /^<!-- UZ -->/ { exit }
        /^```/ { fence = !fence; next }
        fence  { next }
        /^#/   { next }
        /^\*(Target|Status|Worth|ARCHIVED)/ { next }
        /^---$/ { next }
        { print }
    ' "$file" | wc -w | tr -d ' ')
    total=$((total + count))
    printf '%-40s %5s\n' "$(basename "$file")" "$count"
done
printf '%-40s %5s\n' "TOTAL" "$total"

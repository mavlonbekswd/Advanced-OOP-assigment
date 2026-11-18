#!/bin/sh
# Approximate word count per report section.
# Skips headings, the italic status line under each heading, horizontal rules and
# code fences, so the number is close to what Word will report for the prose.
total=0
for file in "$(dirname "$0")"/0*.md "$(dirname "$0")"/appendix-*.md; do
    count=$(awk '
        /^```/ { fence = !fence; next }
        fence  { next }
        /^#/   { next }
        /^\*(Target|Status|Worth)/ { next }
        /^---$/ { next }
        { print }
    ' "$file" | wc -w | tr -d ' ')
    total=$((total + count))
    printf '%-40s %5s\n' "$(basename "$file")" "$count"
done
printf '%-40s %5s\n' "TOTAL (excluding appendix target)" "$total"

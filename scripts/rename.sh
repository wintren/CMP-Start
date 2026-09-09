#!/usr/bin/env bash
# Rewrites the template's placeholder names. Run once, from the repo root, then review the diff.
#
#   ./scripts/rename.sh com.acme.tracker Tracker
#
# Changes the base package (com.template), the Gradle root project name, the Android
# applicationId, the display name, and moves the source directories to match.
set -euo pipefail

OLD_PACKAGE="com.template"
OLD_PROJECT="CMP_Start"
OLD_DISPLAY="CMP Start"

NEW_PACKAGE="${1:-}"
NEW_PROJECT="${2:-}"

if [[ -z "$NEW_PACKAGE" || -z "$NEW_PROJECT" ]]; then
    echo "usage: $0 <new.base.package> <NewProjectName>" >&2
    echo "example: $0 com.acme.tracker Tracker" >&2
    exit 1
fi

if [[ ! "$NEW_PACKAGE" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$ ]]; then
    echo "error: '$NEW_PACKAGE' is not a valid lowercase, dotted package name" >&2
    exit 1
fi

if [[ ! -f settings.gradle.kts ]]; then
    echo "error: run this from the repository root" >&2
    exit 1
fi

if [[ -d .git ]] && ! git diff --quiet 2>/dev/null; then
    echo "error: working tree is dirty — commit or stash first, so the rename is reviewable" >&2
    exit 1
fi

OLD_PATH="${OLD_PACKAGE//.//}"
NEW_PATH="${NEW_PACKAGE//.//}"

echo "package : $OLD_PACKAGE -> $NEW_PACKAGE"
echo "project : $OLD_PROJECT -> $NEW_PROJECT"
echo

# 1. Move source directories, deepest first, so a parent move never invalidates a queued child.
find . -type d -path "*/$OLD_PATH" -not -path "./.git/*" -not -path "*/build/*" -print0 |
    xargs -0 -I{} echo {} |
    awk '{ print length, $0 }' | sort -rn | cut -d" " -f2- |
    while read -r dir; do
        parent="${dir%/$OLD_PATH}"
        target="$parent/$NEW_PATH"
        mkdir -p "$(dirname "$target")"
        mv "$dir" "$target"
        echo "moved $dir"
        # Prune the directories the old package path left behind.
        old_root="$parent/${OLD_PATH%%/*}"
        [[ -d "$old_root" ]] && find "$old_root" -type d -empty -delete 2>/dev/null || true
    done

# 2. Rewrite references. Text files only, and never inside build output or git internals.
find . -type f \
    \( -name "*.kt" -o -name "*.kts" -o -name "*.xml" -o -name "*.toml" \
       -o -name "*.md" -o -name "*.html" -o -name "*.pro" -o -name "*.properties" \) \
    -not -path "./.git/*" -not -path "*/build/*" -not -path "*/.gradle/*" \
    -not -path "*/.kotlin/*" -not -path "./scripts/rename.sh" -print0 |
    xargs -0 sed -i '' \
        -e "s/$OLD_PACKAGE/$NEW_PACKAGE/g" \
        -e "s/$OLD_PROJECT/$NEW_PROJECT/g" \
        -e "s/$OLD_DISPLAY/$NEW_PROJECT/g"

echo
echo "Done. Now:"
echo "  1. git diff            — review, especially README.md and docs/architecture.md prose"
echo "  2. ./gradlew :archtest:test :launch:desktop:compileKotlinDesktop"
echo "  3. delete this script"

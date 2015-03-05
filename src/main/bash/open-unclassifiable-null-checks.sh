#!/bin/bash

XATTR_PATH=".xattr"

grep 'UnclassifiableException' "$1" | perl -p -e 's/^.* (.*.java) line (\d+) column (\d+) .*$/xattr -w com.macromates.selectionRange $2:$3 $1/g' > "$XATTR_PATH"
source "$XATTR_PATH"
rm "$XATTR_PATH"
FILES=$(grep 'UnclassifiableException' "$1" | perl -p -e 's/^.* (.*.java) .*[\r\n]+/$1 /g')
mate $FILES

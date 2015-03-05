#!/bin/bash

ack '^([\w\.]+)(:.*)?$' "$1" | perl -p -e 's/^([\w\.]+)(:.*)?$/\1/' | sort | uniq -c | sort

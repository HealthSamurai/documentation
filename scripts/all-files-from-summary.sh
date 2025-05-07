#!/bin/bash
grep -oP '\]\(\K[^)]+' ./docs/SUMMARY.md | grep -v '^http' | sort | uniq

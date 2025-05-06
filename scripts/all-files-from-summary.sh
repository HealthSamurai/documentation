#!/bin/bash
# Вывести все локальные пути к файлам из SUMMARY.md (без http)
grep -oP '\]\(\K[^)]+' SUMMARY.md | grep -v '^http' | sort | uniq

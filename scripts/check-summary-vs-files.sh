#!/bin/bash
# Сравнить файлы из docs и пути из SUMMARY.md

# Получить все файлы из docs
scripts/all-files.sh > all_files.txt
# Получить все пути из SUMMARY.md
scripts/all-files-from-summary.sh > summary_files.txt

# Файлы в docs, которые не используются в SUMMARY.md
comm -23 all_files.txt summary_files.txt > not_in_summary.txt
# Пути из SUMMARY.md, которых нет на диске
comm -13 all_files.txt summary_files.txt > not_on_disk.txt

# Вывести результат
echo 'Файлы в docs, которые не используются в SUMMARY.md:'
cat not_in_summary.txt

echo 'Пути из SUMMARY.md, которых нет на диске:'
cat not_on_disk.txt 
#!/usr/bin/env bash
set -e

# Получаем все файлы, упомянутые в SUMMARY.md (относительные пути от docs/)
ALL_FROM_SUMMARY=$(./scripts/all-files-from-summary.sh)
# Получаем все реальные файлы в docs/ (относительные пути от docs/)
ALL_ON_DISK=$(./scripts/all-files.sh | grep -v '^/SUMMARY.md$')

# Сохраняем во временные файлы для сравнения
TMP_SUMMARY=$(mktemp)
TMP_DISK=$(mktemp)
echo "$ALL_FROM_SUMMARY" | sort > "$TMP_SUMMARY"
echo "$ALL_ON_DISK" | sort > "$TMP_DISK"

# Файлы, которые есть на диске, но не упомянуты в SUMMARY.md
echo "Files in docs/ not referenced in SUMMARY.md:"
comm -23 "$TMP_DISK" "$TMP_SUMMARY"

# Пути, которые есть в SUMMARY.md, но не существуют на диске
echo "Paths in SUMMARY.md that do not exist on disk:"
comm -13 "$TMP_DISK" "$TMP_SUMMARY"

# Проверка ошибок
if [[ -s "$TMP_DISK" && -s "$TMP_SUMMARY" ]]; then
    if [[ -n $(comm -23 "$TMP_DISK" "$TMP_SUMMARY") || -n $(comm -13 "$TMP_DISK" "$TMP_SUMMARY") ]]; then
        echo -e "\n[pre-push] ERROR: docs/SUMMARY.md and docs/ are out of sync. Fix the issues above before pushing."
        exit 1
    fi
fi

echo "SUMMARY.md and docs/ are in sync."

# Удаляем временные файлы
rm -f "$TMP_SUMMARY" "$TMP_DISK"

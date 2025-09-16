#!/bin/bash

# Zero-downtime reindexing script using Meilisearch index swapping
# Usage: ./reindex-with-swap.sh <index_name> <config_path> <docker_service>
# Example: ./reindex-with-swap.sh docs k8s/meilisearch/config.json docs-scraper
# Example: ./reindex-with-swap.sh fhirbase k8s/meilisearch/config-fhirbase.json docs-scraper-fhirbase

set -e

INDEX_NAME=${1:-docs}
CONFIG_PATH=${2:-k8s/meilisearch/config.json}
SCRAPER_SERVICE=${3:-docs-scraper}
MEILISEARCH_URL=${MEILISEARCH_URL:-http://localhost:7700}
MEILISEARCH_API_KEY=${MEILISEARCH_API_KEY:--60DBZGy6zoDL6Q--s1-dHBWptiVKvK-XRsaacdvkOSM}

echo "Starting zero-downtime reindexing..."
echo "  Index: $INDEX_NAME"
echo "  Config: $CONFIG_PATH"
echo "  Service: $SCRAPER_SERVICE"
echo "  Meilisearch URL: $MEILISEARCH_URL"

# 1. Run scraper to populate temp index
echo "Running $SCRAPER_SERVICE to populate ${INDEX_NAME}_temp..."
docker-compose run --rm $SCRAPER_SERVICE

# 2. Check if indexes exist
echo "Checking indexes..."
MAIN_EXISTS=$(curl -s -o /dev/null -w "%{http_code}" \
    -H "Authorization: Bearer $MEILISEARCH_API_KEY" \
    "$MEILISEARCH_URL/indexes/$INDEX_NAME")

TEMP_EXISTS=$(curl -s -o /dev/null -w "%{http_code}" \
    -H "Authorization: Bearer $MEILISEARCH_API_KEY" \
    "$MEILISEARCH_URL/indexes/${INDEX_NAME}_temp")

echo "Index status: $INDEX_NAME=$MAIN_EXISTS, ${INDEX_NAME}_temp=$TEMP_EXISTS"

# 3. Create empty main index if it doesn't exist
if [ "$MAIN_EXISTS" != "200" ]; then
    echo "Creating empty $INDEX_NAME index..."
    curl -X POST "$MEILISEARCH_URL/indexes" \
        -H "Authorization: Bearer $MEILISEARCH_API_KEY" \
        -H "Content-Type: application/json" \
        -d "{\"uid\": \"$INDEX_NAME\", \"primaryKey\": \"objectID\"}"
    sleep 2
fi

# 4. Swap indexes if temp exists
if [ "$TEMP_EXISTS" = "200" ]; then
    echo "Swapping indexes: $INDEX_NAME <-> ${INDEX_NAME}_temp"
    curl -X POST "$MEILISEARCH_URL/swap-indexes" \
        -H "Authorization: Bearer $MEILISEARCH_API_KEY" \
        -H "Content-Type: application/json" \
        -d "[{\"indexes\": [\"$INDEX_NAME\", \"${INDEX_NAME}_temp\"]}]"

    sleep 2

    # 5. Delete old temp index
    echo "Deleting old temp index: ${INDEX_NAME}_temp"
    curl -X DELETE "$MEILISEARCH_URL/indexes/${INDEX_NAME}_temp" \
        -H "Authorization: Bearer $MEILISEARCH_API_KEY"

    echo "Zero-downtime reindexing completed for $INDEX_NAME"
else
    echo "Error: Temp index ${INDEX_NAME}_temp was not created"
    exit 1
fi
#!/bin/bash
set -e

if [ -z "$REMARK42_SECRET" ] || [ -z "$GOOGLE_CID" ] || [ -z "$GOOGLE_CSEC" ]; then
  echo "Error: Required environment variables not set"
  echo "Usage: REMARK42_SECRET=xxx GOOGLE_CID=xxx GOOGLE_CSEC=xxx ./create-secret.sh"
  exit 1
fi

kubectl create secret generic remark42-oauth \
  --namespace=gitbok \
  --from-literal=remark42-secret="$REMARK42_SECRET" \
  --from-literal=google-client-id="$GOOGLE_CID" \
  --from-literal=google-client-secret="$GOOGLE_CSEC" \
  --dry-run=client -o yaml | kubectl apply -f -

echo "Secret remark42-oauth created/updated successfully"

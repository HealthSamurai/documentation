#!/bin/bash

echo "🚀 Building F2 Navigation React app..."

# Change to the React project directory
cd "$(dirname "$0")"

# Build the project
npm run build

if [ $? -eq 0 ]; then
    echo "✅ Build completed successfully!"
    echo "📄 Output: ../resources/public/f2-navigation-react.js"
    
    # Show file size
    file_size=$(du -h ../resources/public/f2-navigation-react.js | cut -f1)
    echo "📦 File size: $file_size"
else
    echo "❌ Build failed!"
    exit 1
fi
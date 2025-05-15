import os
import re
import urllib.parse

# Settings
assets_dir = ".gitbook/assets"
search_dirs = ["."]
image_extensions = [".png", ".jpg", ".jpeg", ".gif", ".webp", ".svg"]

def sanitize_filename(filename):
    # Decode URL-encoded characters (like %20)
    filename = urllib.parse.unquote(filename)
    
    # Get file extension
    name, ext = os.path.splitext(filename)
    
    # Replace all invalid characters with dash
    # Keep only letters, numbers, dashes and underscores
    sanitized = re.sub(r'[^a-zA-Z0-9\-_]', '-', name)
    
    # Replace multiple dashes with a single one
    sanitized = re.sub(r'-+', '-', sanitized)
    
    # Remove leading and trailing dashes
    sanitized = sanitized.strip('-')
    
    # If name is empty after sanitization, generate a random one
    if not sanitized:
        import uuid
        sanitized = f"image-{uuid.uuid4().hex[:8]}"
    
    return f"{sanitized}{ext.lower()}"

def find_and_replace_in_files(old_filename, new_filename):
    pattern = re.escape(old_filename)
    for root, _, files in os.walk("."):
        for file in files:
            if not file.endswith(".md"):
                continue
            filepath = os.path.join(root, file)
            try:
                with open(filepath, "r", encoding="utf-8") as f:
                    content = f.read()
                
                # Search for both normal and URL-encoded versions
                old_encoded = urllib.parse.quote(old_filename)
                new_encoded = urllib.parse.quote(new_filename)
                
                if old_filename in content or old_encoded in content:
                    new_content = content.replace(old_filename, new_filename)
                    new_content = new_content.replace(old_encoded, new_encoded)
                    
                    with open(filepath, "w", encoding="utf-8") as f:
                        f.write(new_content)
                    print(f"✅ Updated file: {filepath}")
            except Exception as e:
                print(f"❌ Error processing file {filepath}: {str(e)}")

def main():
    renamed_files = {}  # Track renamed files
    
    # Collect all files that need to be renamed
    for filename in os.listdir(assets_dir):
        if any(filename.lower().endswith(ext) for ext in image_extensions):
            new_filename = sanitize_filename(filename)
            if new_filename != filename:
                renamed_files[filename] = new_filename

    # Perform renaming and update links
    for old_filename, new_filename in renamed_files.items():
        old_path = os.path.join(assets_dir, old_filename)
        new_path = os.path.join(assets_dir, new_filename)
        
        try:
            # If target file already exists, add random suffix
            if os.path.exists(new_path):
                import uuid
                name, ext = os.path.splitext(new_filename)
                new_filename = f"{name}-{uuid.uuid4().hex[:6]}{ext}"
                new_path = os.path.join(assets_dir, new_filename)
            
            os.rename(old_path, new_path)
            print(f"📁 {old_filename} -> {new_filename}")
            
            find_and_replace_in_files(old_filename, new_filename)
        except Exception as e:
            print(f"❌ Error renaming {old_filename}: {str(e)}")

if __name__ == "__main__":
    main()

import os
import uuid
import re

# Настройки
assets_dir = ".gitbook/assets"
search_dirs = ["."]
image_extensions = [".png", ".jpg", ".jpeg", ".gif", ".webp", ".svg"]

def generate_uuid_filename(ext):
    return f"{uuid.uuid4().hex}{ext}"

def find_and_replace_in_files(old_filename, new_filename):
    pattern = re.escape(old_filename)
    for root, _, files in os.walk("."):
        for file in files:
            if not file.endswith(".md"):
                continue
            filepath = os.path.join(root, file)
            with open(filepath, "r", encoding="utf-8") as f:
                content = f.read()
            # Заменяем только первое вхождение
            if old_filename in content:
                new_content = content.replace(old_filename, new_filename, 1)
                with open(filepath, "w", encoding="utf-8") as f:
                    f.write(new_content)
                print(f"✅ Заменено в файле: {filepath}")
                return  # Только первая замена

def main():
    for filename in os.listdir(assets_dir):
        if " " in filename and any(filename.lower().endswith(ext) for ext in image_extensions):
            old_path = os.path.join(assets_dir, filename)
            ext = os.path.splitext(filename)[1]
            new_filename = generate_uuid_filename(ext)
            new_path = os.path.join(assets_dir, new_filename)

            os.rename(old_path, new_path)
            print(f"📁 {filename} -> {new_filename}")

            find_and_replace_in_files(filename, new_filename)

if __name__ == "__main__":
    main()

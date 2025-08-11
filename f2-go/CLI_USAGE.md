# f2 CLI Usage

The f2 tool provides command-line interface for managing GitBook documentation structure.

## Building

```bash
cd f2-go
go build -o f2 ./cmd/f2
```

## Commands

### 1. Rename

Renames a page in the documentation, updating title, filename, and all references.

```bash
./f2 rename <path-to-file> <new-title>
```

**Example:**
```bash
./f2 rename "getting-started/run-aidbox-locally.md" "Local Development Guide"
```

**What it does:**
- Updates title in SUMMARY.md
- Renames the physical file (generates filename from new title)
- Updates H1 header in the markdown file
- Adds redirect to .gitbook.yaml if URL changes
- Updates all links pointing to this file

### 2. Move

Moves a page to a different position in the documentation hierarchy.

```bash
./f2 move <path-to-file> <new-path-to-file>
```

**Example:**
```bash
./f2 move "readme/features.md" "tutorials/features.md"
```

**What it does:**
- Shows interactive position selector (numbered list)
- Updates SUMMARY.md structure
- Moves physical file to new location
- Adds redirect if URL changes
- Updates all relative links in the moved file
- Updates all links pointing to this file

### 3. Delete

Deletes a page from the documentation.

```bash
./f2 delete <path-to-file>
```

**Example:**
```bash
./f2 delete "getting-started/upload-sample-data.md"
```

**What it does:**
- Asks for confirmation
- Removes from SUMMARY.md
- Deletes physical file
- Removes from redirects if it was a redirect target
- Shows warning about potential broken links

## TUI Mode

Run without arguments to launch the interactive TUI (Terminal User Interface):

```bash
./f2
```

Or specify a custom docs directory:

```bash
./f2 path/to/docs
```

## Important Notes

1. **Always work from the documentation root directory** (where .gitbook.yaml is located)
2. **File paths are relative to the docs/ directory**
3. **Backup your documentation before major changes**
4. **The tool automatically handles:**
   - URL generation (removes .md extension, handles README.md)
   - Redirect management in .gitbook.yaml
   - Link updates across all documentation files
   - Filename generation from titles (lowercase, hyphens, no special chars)

## File Naming Convention

When renaming files, the tool automatically generates filenames:
- Converts to lowercase
- Replaces spaces with hyphens
- Removes special characters
- Adds .md extension

Example: "My New Page!" → "my-new-page.md"
package main

import (
	"fmt"
	"log"
	"os"
	"path/filepath"

	"github.com/f2-go/internal/core"
)

func main() {
	if len(os.Args) < 3 {
		fmt.Println("Usage: go run debug-cli.go <src> <dst>")
		os.Exit(1)
	}

	srcPath := os.Args[1]
	dstPath := os.Args[2]

	fmt.Printf("=== Debug CLI Move ===\n")
	fmt.Printf("Source: %s\n", srcPath)
	fmt.Printf("Destination: %s\n", dstPath)

	// Создаем файловую систему
	fs := &osFileSystem{}

	// Инициализируем менеджеры
	_, _, redirectManager, _, err := initializeManagers(fs, ".")
	if err != nil {
		log.Fatal("Failed to initialize managers:", err)
	}

	fmt.Printf("\n=== After initializeManagers ===\n")
	fmt.Printf("redirectManager type: %T\n", redirectManager)

	// Показываем текущие redirects
	fmt.Printf("\n=== Current redirects ===\n")
	for from, to := range redirectManager.GetRedirects() {
		fmt.Printf("  %s -> %s\n", from, to)
	}

	// Симулируем добавление redirect
	oldURL := pathToURL(srcPath)
	newURL := pathToURL(dstPath)
	
	if oldURL != newURL {
		fmt.Printf("\n=== Adding redirect ===\n")
		fmt.Printf("Old URL: %s\n", oldURL)
		fmt.Printf("New URL: %s\n", newURL)
		
		err = redirectManager.AddRedirect(oldURL, dstPath)
		if err != nil {
			log.Fatal("Error adding redirect:", err)
		}

		// Показываем обновленные redirects
		fmt.Printf("\n=== Updated redirects ===\n")
		for from, to := range redirectManager.GetRedirects() {
			fmt.Printf("  %s -> %s\n", from, to)
		}

		// Сериализуем
		serialized := redirectManager.Serialize()
		fmt.Printf("\n=== Serialized result ===\n")
		fmt.Println(string(serialized))

		// Проверяем содержимое
		resultStr := string(serialized)
		fmt.Printf("\n=== Content check ===\n")
		fmt.Printf("Contains 'root: .': %v\n", contains(resultStr, "root: ."))
		fmt.Printf("Contains 'structure:': %v\n", contains(resultStr, "structure:"))
		fmt.Printf("Contains 'redirects:': %v\n", contains(resultStr, "redirects:"))
		fmt.Printf("Length: %d\n", len(resultStr))

		// Сохраняем в файл
		gitbookPath := filepath.Join(".", ".gitbook.yaml")
		err = os.WriteFile(gitbookPath, serialized, 0644)
		if err != nil {
			log.Fatal("Error saving .gitbook.yaml:", err)
		}

		fmt.Printf("\n=== Saved to %s ===\n", gitbookPath)
	} else {
		fmt.Printf("\n=== No redirect needed (same URL) ===\n")
	}
}

type osFileSystem struct{}

func (fs *osFileSystem) ReadFile(path string) ([]byte, error) {
	return os.ReadFile(path)
}

func (fs *osFileSystem) WriteFile(path string, data []byte, perm os.FileMode) error {
	return os.WriteFile(path, data, perm)
}

func (fs *osFileSystem) Exists(path string) bool {
	_, err := os.Stat(path)
	return err == nil
}

func (fs *osFileSystem) MoveFile(src, dst string) error {
	return os.Rename(src, dst)
}

func (fs *osFileSystem) Remove(path string) error {
	return os.Remove(path)
}

func (fs *osFileSystem) MkdirAll(path string, perm os.FileMode) error {
	return os.MkdirAll(path, perm)
}

func (fs *osFileSystem) ReadDir(path string) ([]os.DirEntry, error) {
	return os.ReadDir(path)
}

func (fs *osFileSystem) Walk(root string, walkFn func(path string, isDir bool) error) error {
	return filepath.Walk(root, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		return walkFn(path, info.IsDir())
	})
}

func initializeManagers(fs core.FileSystem, docsPath string) (*core.Summary, core.SummaryManager, core.RedirectManager, core.LinkUpdater, error) {
	// Load SUMMARY.md
	summaryPath := filepath.Join(docsPath, "docs", "SUMMARY.md")
	if !fs.Exists(summaryPath) {
		return nil, nil, nil, nil, fmt.Errorf("SUMMARY.md not found in '%s/docs'", docsPath)
	}

	summaryContent, err := fs.ReadFile(summaryPath)
	if err != nil {
		return nil, nil, nil, nil, fmt.Errorf("Error reading SUMMARY.md: %v", err)
	}

	parser := core.NewSummaryParser()
	summary, err := parser.Parse(summaryContent)
	if err != nil {
		return nil, nil, nil, nil, fmt.Errorf("Error parsing SUMMARY.md: %v", err)
	}

	summaryManager := core.NewSummaryManager(summary)

	// Load .gitbook.yaml for redirects
	redirectManager := core.NewRedirectManager()
	gitbookPath := filepath.Join(docsPath, ".gitbook.yaml")
	if fs.Exists(gitbookPath) {
		content, err := fs.ReadFile(gitbookPath)
		if err != nil {
			return nil, nil, nil, nil, fmt.Errorf("Error reading .gitbook.yaml: %v", err)
		}
		_, err = redirectManager.LoadRedirects(content)
		if err != nil {
			return nil, nil, nil, nil, fmt.Errorf("Error loading redirects: %v", err)
		}
	}

	// Create link updater
	linkUpdater := &SimpleLinkUpdater{fs: fs}

	return summary, summaryManager, redirectManager, linkUpdater, nil
}

type SimpleLinkUpdater struct {
	fs       core.FileSystem
	filePath string
}

func (u *SimpleLinkUpdater) FindLinks(content []byte) []core.Link {
	return []core.Link{}
}

func (u *SimpleLinkUpdater) UpdateLinks(content []byte, pathChanges map[string]string) ([]byte, bool, int) {
	return content, false, 0
}

func (u *SimpleLinkUpdater) CalculateRelativePath(from, to string) string {
	return to
}

func (u *SimpleLinkUpdater) ResolveReferences(content []byte) map[string]string {
	return map[string]string{}
}

func pathToURL(path string) string {
	// Remove .md extension
	if len(path) > 3 && path[len(path)-3:] == ".md" {
		path = path[:len(path)-3]
	}

	// Convert to URL format
	url := "/" + path

	// Add leading slash
	if url != "" && url[0] != '/' {
		url = "/" + url
	}

	return url
}

func contains(s, substr string) bool {
	return len(s) >= len(substr) && (s == substr || len(s) > len(substr) && 
		(s[:len(substr)] == substr || s[len(s)-len(substr):] == substr || 
		len(s) > len(substr)+1 && contains(s[1:], substr)))
}

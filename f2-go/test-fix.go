package main

import (
	"fmt"
	"log"
	"os"
	"path/filepath"

	"github.com/f2-go/internal/core"
)

func main() {
	// Создаем временную директорию для теста
	testDir := "test-docs"
	if err := os.MkdirAll(testDir, 0755); err != nil {
		log.Fatal("Failed to create test directory:", err)
	}
	defer os.RemoveAll(testDir)

	// Создаем .gitbook.yaml с полной структурой
	gitbookContent := `root: .
structure:
  readme: README.md
  summary: SUMMARY.md
redirects:
  old-page: new-page.md`

	gitbookPath := filepath.Join(testDir, ".gitbook.yaml")
	if err := os.WriteFile(gitbookPath, []byte(gitbookContent), 0644); err != nil {
		log.Fatal("Failed to write .gitbook.yaml:", err)
	}

	fmt.Println("=== Created .gitbook.yaml ===")
	fmt.Println(string(gitbookContent))

	// Создаем менеджер redirects
	redirectManager := core.NewRedirectManager()
	
	// Загружаем конфигурацию
	config, err := redirectManager.LoadRedirects([]byte(gitbookContent))
	if err != nil {
		log.Fatal("Error loading redirects:", err)
	}

	fmt.Printf("\n=== Loaded config ===\n")
	fmt.Printf("Redirects: %+v\n", config.Redirects)

	// Добавляем новый redirect
	err = redirectManager.AddRedirect("another-old", "another-new.md")
	if err != nil {
		log.Fatal("Error adding redirect:", err)
	}

	fmt.Println("\n=== After adding new redirect ===")
	for from, to := range redirectManager.GetRedirects() {
		fmt.Printf("  %s -> %s\n", from, to)
	}

	// Сериализуем обратно
	serialized := redirectManager.Serialize()
	
	fmt.Println("\n=== Serialized result ===")
	fmt.Println(string(serialized))

	// Проверяем, содержит ли результат нужные поля
	resultStr := string(serialized)
	fmt.Println("\n=== Content check ===")
	fmt.Printf("Contains 'root: .': %v\n", contains(resultStr, "root: ."))
	fmt.Printf("Contains 'structure:': %v\n", contains(resultStr, "structure:"))
	fmt.Printf("Contains 'readme: README.md': %v\n", contains(resultStr, "readme: README.md"))
	fmt.Printf("Contains 'summary: SUMMARY.md': %v\n", contains(resultStr, "summary: SUMMARY.md"))
	fmt.Printf("Contains 'redirects:': %v\n", contains(resultStr, "redirects:"))

	// Сохраняем в файл
	if err := os.WriteFile(gitbookPath, serialized, 0644); err != nil {
		log.Fatal("Failed to write updated .gitbook.yaml:", err)
	}

	// Читаем обратно для проверки
	updatedContent, err := os.ReadFile(gitbookPath)
	if err != nil {
		log.Fatal("Failed to read updated .gitbook.yaml:", err)
	}

	fmt.Println("\n=== File content after save ===")
	fmt.Println(string(updatedContent))
}

func contains(s, substr string) bool {
	return len(s) >= len(substr) && (s == substr || len(s) > len(substr) && 
		(s[:len(substr)] == substr || s[len(s)-len(substr):] == substr || 
		len(s) > len(substr)+1 && contains(s[1:], substr)))
}

package main

import (
	"fmt"
	"log"
	"os"

	"github.com/f2-go/internal/core"
)

func main() {
	// Читаем тестовый файл
	content, err := os.ReadFile("test-gitbook.yaml")
	if err != nil {
		log.Fatal("Error reading test-gitbook.yaml:", err)
	}

	fmt.Println("=== Original .gitbook.yaml ===")
	fmt.Println(string(content))

	// Создаем менеджер redirects
	redirectManager := core.NewRedirectManager()
	
	// Загружаем конфигурацию
	config, err := redirectManager.LoadRedirects(content)
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
}

func contains(s, substr string) bool {
	return len(s) >= len(substr) && (s == substr || len(s) > len(substr) && 
		(s[:len(substr)] == substr || s[len(s)-len(substr):] == substr || 
		len(s) > len(substr)+1 && contains(s[1:], substr)))
}

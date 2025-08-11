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

	fmt.Println("\n=== Loaded redirects ===")
	for from, to := range config.Redirects {
		fmt.Printf("  %s -> %s\n", from, to)
	}

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
	
	fmt.Println("\n=== Serialized result (preserving full structure) ===")
	fmt.Println(string(serialized))
}

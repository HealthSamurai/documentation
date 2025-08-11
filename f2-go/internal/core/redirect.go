package core

import (
	"fmt"
	"sort"
	"strings"

	"gopkg.in/yaml.v3"
)

// redirectManager implements RedirectManager interface
type redirectManager struct {
	config *RedirectConfig
	fullConfig map[string]interface{} // Store the full YAML structure
}

// NewRedirectManager creates a new redirect manager
func NewRedirectManager() RedirectManager {
	return &redirectManager{
		config: &RedirectConfig{
			Redirects: make(map[string]string),
		},
	}
}

// LoadRedirects loads redirects from YAML content
func (m *redirectManager) LoadRedirects(content []byte) (*RedirectConfig, error) {
	// First try to unmarshal into a map to get the full YAML
	var fullConfig map[string]interface{}
	if err := yaml.Unmarshal(content, &fullConfig); err != nil {
		return nil, fmt.Errorf("invalid YAML: %w", err)
	}

	// Store the full config for later serialization
	m.fullConfig = fullConfig

	// Initialize empty config
	config := &RedirectConfig{
		Redirects: make(map[string]string),
	}

	// Extract redirects section if it exists
	if redirectsRaw, ok := fullConfig["redirects"]; ok {
		if redirectsMap, ok := redirectsRaw.(map[string]interface{}); ok {
			for k, v := range redirectsMap {
				if vStr, ok := v.(string); ok {
					config.Redirects[k] = vStr
				}
			}
		}
	}

	m.config = config
	return config, nil
}

// AddRedirect adds a new redirect
func (m *redirectManager) AddRedirect(from, to string) error {
	// Clean paths
	from = m.cleanPath(from)
	to = m.cleanPath(to)

	// Validate
	if from == "" || to == "" {
		return fmt.Errorf("redirect paths cannot be empty")
	}

	if from == to {
		return fmt.Errorf("cannot create self-redirect from %s to %s", from, to)
	}

	// Check if 'from' already exists as a target (prevent chains)
	for _, existingTarget := range m.config.Redirects {
		if existingTarget == from {
			return fmt.Errorf("cannot create redirect chain: %s is already a redirect target", from)
		}
	}

	// Check for circular redirects
	if m.HasCircularRedirect(from, to) {
		return fmt.Errorf("circular redirect detected")
	}

	// Add the redirect
	m.config.Redirects[from] = to
	return nil
}

// RemoveRedirectsTo removes all redirects pointing to a specific path
func (m *redirectManager) RemoveRedirectsTo(path string) error {
	path = m.cleanPath(path)
	
	toRemove := []string{}
	for from, to := range m.config.Redirects {
		if to == path {
			toRemove = append(toRemove, from)
		}
	}

	for _, from := range toRemove {
		delete(m.config.Redirects, from)
	}

	return nil
}

// HasCircularRedirect checks if adding a redirect would create a circular dependency
func (m *redirectManager) HasCircularRedirect(from, to string) bool {
	from = m.cleanPath(from)
	to = m.cleanPath(to)

	// Follow the redirect chain starting from 'to'
	visited := make(map[string]bool)
	current := to

	for {
		// If we've seen this before, there's a loop (but not necessarily involving 'from')
		if visited[current] {
			return false
		}
		visited[current] = true

		// If we reached 'from', we have a circular redirect
		if current == from {
			return true
		}

		// Follow the next redirect
		next, exists := m.config.Redirects[current]
		if !exists {
			// End of chain, no circular redirect
			return false
		}

		current = next

		// Safety check to prevent infinite loops
		if len(visited) > 1000 {
			return true // Assume circular if chain is too long
		}
	}
}

// Serialize converts redirects to YAML format
func (m *redirectManager) Serialize() []byte {
	if len(m.config.Redirects) == 0 {
		return []byte("redirects: {}\n")
	}

	// Sort keys for consistent output
	keys := make([]string, 0, len(m.config.Redirects))
	for k := range m.config.Redirects {
		keys = append(keys, k)
	}
	sort.Strings(keys)

	// Build YAML manually for clean output
	var result strings.Builder
	result.WriteString("redirects:\n")
	
	for _, from := range keys {
		to := m.config.Redirects[from]
		result.WriteString(fmt.Sprintf("  %s: %s\n", from, to))
	}

	return []byte(result.String())
}

// GetRedirects returns all redirects
func (m *redirectManager) GetRedirects() map[string]string {
	result := make(map[string]string)
	for k, v := range m.config.Redirects {
		result[k] = v
	}
	return result
}

// cleanPath removes leading slashes and ./ from paths
func (m *redirectManager) cleanPath(path string) string {
	path = strings.TrimSpace(path)
	path = strings.TrimPrefix(path, "/")
	path = strings.TrimPrefix(path, "./")
	path = strings.TrimPrefix(path, "/") // In case of //
	return path
}
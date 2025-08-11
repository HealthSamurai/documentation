package core

import (
	"fmt"
	"sort"
	"strings"

	"gopkg.in/yaml.v3"
)

// redirectManager implements RedirectManager interface
type redirectManager struct {
	config     *RedirectConfig
	fullConfig map[string]interface{} // Store the full YAML config
	orderedKeys []string // Preserve original order of redirect keys
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
	// Parse YAML using Node to preserve order
	var node yaml.Node
	if err := yaml.Unmarshal(content, &node); err != nil {
		return nil, fmt.Errorf("invalid YAML: %w", err)
	}
	
	// Also parse to regular map for easy access
	var fullConfig map[string]interface{}
	if err := yaml.Unmarshal(content, &fullConfig); err != nil {
		return nil, fmt.Errorf("invalid YAML: %w", err)
	}
	
	// Store the full config
	m.fullConfig = fullConfig

	// Initialize empty config
	config := &RedirectConfig{
		Redirects: make(map[string]string),
	}
	
	// Initialize ordered keys
	m.orderedKeys = make([]string, 0)

	// Walk through the YAML node to preserve order
	if node.Kind == yaml.DocumentNode && len(node.Content) > 0 {
		root := node.Content[0]
		if root.Kind == yaml.MappingNode {
			for i := 0; i < len(root.Content); i += 2 {
				keyNode := root.Content[i]
				valueNode := root.Content[i+1]
				
				if keyNode.Value == "redirects" && valueNode.Kind == yaml.MappingNode {
					// Process redirects in order
					for j := 0; j < len(valueNode.Content); j += 2 {
						redirectKey := valueNode.Content[j].Value
						redirectValue := valueNode.Content[j+1].Value
						config.Redirects[redirectKey] = redirectValue
						m.orderedKeys = append(m.orderedKeys, redirectKey)
					}
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
	// Add to ordered keys if it's a new key
	isNew := true
	for _, k := range m.orderedKeys {
		if k == from {
			isNew = false
			break
		}
	}
	if isNew {
		m.orderedKeys = append(m.orderedKeys, from)
	}
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
		// Remove from ordered keys
		newKeys := make([]string, 0, len(m.orderedKeys))
		for _, k := range m.orderedKeys {
			if k != from {
				newKeys = append(newKeys, k)
			}
		}
		m.orderedKeys = newKeys
	}

	return nil
}

// UpdateRedirectsTo updates all redirects pointing to oldPath to point to newPath
func (m *redirectManager) UpdateRedirectsTo(oldPath, newPath string) {
	oldPath = m.cleanPath(oldPath)
	newPath = m.cleanPath(newPath)
	
	for from, to := range m.config.Redirects {
		if to == oldPath {
			m.config.Redirects[from] = newPath
		}
	}
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
	// If we have the full config, serialize it with updated redirects
	if m.fullConfig != nil {
		// Note: We don't update m.fullConfig["redirects"] anymore
		// because we handle it directly in the yaml.Node construction below
		
		// Create ordered map to preserve field order
		orderedConfig := &yaml.Node{
			Kind: yaml.MappingNode,
		}
		
		// Add fields in desired order: root, structure, then redirects
		if root, ok := m.fullConfig["root"]; ok {
			orderedConfig.Content = append(orderedConfig.Content,
				&yaml.Node{Kind: yaml.ScalarNode, Value: "root"},
				&yaml.Node{Kind: yaml.ScalarNode, Value: root.(string)},
			)
		}
		
		if structure, ok := m.fullConfig["structure"]; ok {
			structNode := &yaml.Node{Kind: yaml.MappingNode}
			if structMap, ok := structure.(map[string]interface{}); ok {
				// Add structure fields in order: readme, summary
				if readme, ok := structMap["readme"]; ok {
					structNode.Content = append(structNode.Content,
						&yaml.Node{Kind: yaml.ScalarNode, Value: "readme"},
						&yaml.Node{Kind: yaml.ScalarNode, Value: readme.(string)},
					)
				}
				if summary, ok := structMap["summary"]; ok {
					structNode.Content = append(structNode.Content,
						&yaml.Node{Kind: yaml.ScalarNode, Value: "summary"},
						&yaml.Node{Kind: yaml.ScalarNode, Value: summary.(string)},
					)
				}
			}
			orderedConfig.Content = append(orderedConfig.Content,
				&yaml.Node{Kind: yaml.ScalarNode, Value: "structure"},
				structNode,
			)
		}
		
		if len(m.config.Redirects) > 0 {
			redirectsNode := &yaml.Node{Kind: yaml.MappingNode}
			
			// Use ordered keys if available, otherwise sort
			keys := m.orderedKeys
			if len(keys) == 0 {
				keys = make([]string, 0, len(m.config.Redirects))
				for k := range m.config.Redirects {
					keys = append(keys, k)
				}
				sort.Strings(keys)
			}
			
			// Add all redirects in the preserved order
			for _, k := range keys {
				if v, ok := m.config.Redirects[k]; ok {
					redirectsNode.Content = append(redirectsNode.Content,
						&yaml.Node{Kind: yaml.ScalarNode, Value: k},
						&yaml.Node{Kind: yaml.ScalarNode, Value: v},
					)
				}
			}
			
			orderedConfig.Content = append(orderedConfig.Content,
				&yaml.Node{Kind: yaml.ScalarNode, Value: "redirects"},
				redirectsNode,
			)
		}
		
		// Marshal using yaml.Node to preserve order with 2-space indent
		encoder := yaml.NewEncoder(nil)
		encoder.SetIndent(2) // Set 2-space indentation
		
		var buf strings.Builder
		encoder = yaml.NewEncoder(&buf)
		encoder.SetIndent(2)
		
		err := encoder.Encode(orderedConfig)
		if err == nil {
			result := buf.String()
			
			// Fix YAML complex key syntax first (before indent changes)
			// This happens with very long keys
			result = fixComplexKeys(result)
			
			// Add empty line before redirects if it exists
			if strings.Contains(result, "\nredirects:") {
				result = strings.Replace(result, "\nredirects:", "\n\nredirects:", 1)
			}
			
			return []byte(result)
		}
	}
	
	// Fallback to simple serialization
	return m.serializeRedirectsOnly()
}

// serializeRedirectsOnly serializes only the redirects section
func (m *redirectManager) serializeRedirectsOnly() []byte {
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

// fixComplexKeys converts YAML complex key syntax back to simple syntax
// Handles both original yaml output (before indent fix) and after
func fixComplexKeys(yaml string) string {
	lines := strings.Split(yaml, "\n")
	result := make([]string, 0, len(lines))
	
	for i := 0; i < len(lines); i++ {
		line := lines[i]
		// Check for complex keys - look for lines starting with "? " after spaces
		if strings.Contains(line, "? ") && strings.TrimSpace(line)[0] == '?' {
			// Get indentation of the ? line
			indent := line[:strings.Index(line, "?")]
			// Get the key (everything after "? ")
			key := strings.TrimPrefix(strings.TrimSpace(line), "? ")
			
			// Check if next line is the value (starts with ": " after same indent)
			if i+1 < len(lines) {
				nextLine := lines[i+1]
				if strings.Contains(nextLine, ": ") && strings.TrimSpace(nextLine)[0] == ':' {
					// Get the value (everything after ": ")
					value := strings.TrimPrefix(strings.TrimSpace(nextLine), ": ")
					// Combine into simple syntax with proper indentation
					result = append(result, indent+key+": "+value)
					i++ // Skip the next line since we've processed it
					continue
				}
			}
		}
		result = append(result, line)
	}
	
	return strings.Join(result, "\n")
}
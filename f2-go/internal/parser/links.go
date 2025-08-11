package parser

import (
	"bytes"
	"path/filepath"
	"regexp"
	"strings"

	"github.com/f2-go/internal/core"
)

var (
	// Regex patterns for different link types
	markdownLinkRegex = regexp.MustCompile(`\[([^\]]+)\]\(([^)]+)\)`)
	imageLinkRegex    = regexp.MustCompile(`!\[([^\]]*)\]\(([^)]+)\)`)
	referenceLinkRegex = regexp.MustCompile(`\[([^\]]+)\]\[([^\]]+)\]`)
	referenceDefRegex = regexp.MustCompile(`^\[([^\]]+)\]:\s*(.+?)(?:\s+"[^"]*")?$`)
	htmlLinkRegex     = regexp.MustCompile(`<a\s+(?:[^>]*?\s+)?href="([^"]+)"[^>]*>([^<]+)</a>`)
	codeBlockRegex    = regexp.MustCompile("(?s)```[^`]*```")
	inlineCodeRegex   = regexp.MustCompile("`[^`]+`")
)

// LinkParser handles parsing and updating of links in markdown files
type LinkParser struct{}

// NewLinkParser creates a new link parser
func NewLinkParser() core.LinkUpdater {
	return &LinkParser{}
}

// FindLinks finds all links in the content
func (p *LinkParser) FindLinks(content []byte) []core.Link {
	var links []core.Link
	
	// First, identify code blocks and inline code to skip
	codeRanges := p.findCodeRanges(content)
	
	lines := bytes.Split(content, []byte("\n"))
	currentPos := 0
	
	for lineNum, line := range lines {
		lineStr := string(line)
		lineStart := currentPos
		
		// Find markdown image links first (they have priority over regular links)
		for _, match := range imageLinkRegex.FindAllStringSubmatchIndex(lineStr, -1) {
			pos := lineStart + match[0]
			if !p.isInCodeRange(pos, codeRanges) {
				link := core.Link{
					Text:   p.extractText(lineStr, match, 2),
					URL:    p.extractText(lineStr, match, 4),
					Type:   core.LinkTypeImage,
					Line:   lineNum + 1,
					Column: match[0] + 1,
				}
				links = append(links, link)
			}
		}
		
		// Find regular markdown links
		for _, match := range markdownLinkRegex.FindAllStringSubmatchIndex(lineStr, -1) {
			pos := lineStart + match[0]
			if !p.isInCodeRange(pos, codeRanges) && !p.isImageLink(lineStr, match[0]) {
				url := p.extractText(lineStr, match, 4)
				link := core.Link{
					Text:   p.extractText(lineStr, match, 2),
					URL:    url,
					Type:   core.LinkTypeMarkdown,
					Line:   lineNum + 1,
					Column: match[0] + 1,
				}
				
				// Check for anchor
				if idx := strings.Index(url, "#"); idx != -1 {
					link.IsAnchor = true
					link.Anchor = url[idx+1:]
					link.URL = url[:idx]
				}
				
				links = append(links, link)
			}
		}
		
		// Find reference-style links
		for _, match := range referenceLinkRegex.FindAllStringSubmatchIndex(lineStr, -1) {
			pos := lineStart + match[0]
			if !p.isInCodeRange(pos, codeRanges) {
				link := core.Link{
					Text:   p.extractText(lineStr, match, 2),
					URL:    p.extractText(lineStr, match, 4),
					Type:   core.LinkTypeReference,
					Line:   lineNum + 1,
					Column: match[0] + 1,
				}
				links = append(links, link)
			}
		}
		
		// Find HTML links
		for _, match := range htmlLinkRegex.FindAllStringSubmatchIndex(lineStr, -1) {
			pos := lineStart + match[0]
			if !p.isInCodeRange(pos, codeRanges) {
				link := core.Link{
					Text:   p.extractText(lineStr, match, 4),
					URL:    p.extractText(lineStr, match, 2),
					Type:   core.LinkTypeHTML,
					Line:   lineNum + 1,
					Column: match[0] + 1,
				}
				links = append(links, link)
			}
		}
		
		currentPos += len(line) + 1 // +1 for newline
	}
	
	return links
}

// UpdateLinks updates links in content based on path changes
func (p *LinkParser) UpdateLinks(content []byte, pathChanges map[string]string) ([]byte, bool) {
	if len(pathChanges) == 0 {
		return content, false
	}
	
	result := string(content)
	changed := false
	
	// Get code ranges to skip
	codeRanges := p.findCodeRanges(content)
	
	// Process all link types
	changed = p.updateLinkType(
		&result, 
		imageLinkRegex, 
		pathChanges, 
		codeRanges, 
		4, // URL is in group 2 (index 4-5)
	) || changed
	
	changed = p.updateLinkType(
		&result, 
		markdownLinkRegex, 
		pathChanges, 
		codeRanges, 
		4, // URL is in group 2 (index 4-5)
	) || changed
	
	changed = p.updateHTMLLinks(
		&result, 
		pathChanges, 
		codeRanges,
	) || changed
	
	return []byte(result), changed
}

// CalculateRelativePath calculates relative path from one file to another
func (p *LinkParser) CalculateRelativePath(from, to string) string {
	// Handle absolute paths
	if strings.HasPrefix(to, "/") {
		return to
	}
	
	// Clean up ./ prefix
	to = strings.TrimPrefix(to, "./")
	
	// Get directories
	fromDir := filepath.Dir(from)
	
	// Calculate relative path
	rel, err := filepath.Rel(fromDir, to)
	if err != nil {
		return to // Fallback to original
	}
	
	return filepath.ToSlash(rel)
}

// ResolveReferences finds reference definitions in content
func (p *LinkParser) ResolveReferences(content []byte) map[string]string {
	refs := make(map[string]string)
	
	lines := bytes.Split(content, []byte("\n"))
	for _, line := range lines {
		lineStr := string(line)
		if matches := referenceDefRegex.FindStringSubmatch(lineStr); matches != nil {
			refName := strings.ToLower(matches[1])
			refURL := matches[2]
			refs[refName] = refURL
		}
	}
	
	return refs
}

// Helper methods

func (p *LinkParser) findCodeRanges(content []byte) [][]int {
	var ranges [][]int
	
	// Find code blocks
	for _, match := range codeBlockRegex.FindAllIndex(content, -1) {
		ranges = append(ranges, match)
	}
	
	// Find inline code
	for _, match := range inlineCodeRegex.FindAllIndex(content, -1) {
		ranges = append(ranges, match)
	}
	
	return ranges
}

func (p *LinkParser) isInCodeRange(pos int, ranges [][]int) bool {
	for _, r := range ranges {
		if pos >= r[0] && pos < r[1] {
			return true
		}
	}
	return false
}

func (p *LinkParser) isImageLink(line string, pos int) bool {
	return pos > 0 && line[pos-1] == '!'
}

func (p *LinkParser) extractText(s string, match []int, group int) string {
	if group >= len(match) {
		return ""
	}
	return s[match[group]:match[group+1]]
}

func (p *LinkParser) updateLinkType(
	content *string,
	regex *regexp.Regexp,
	pathChanges map[string]string,
	codeRanges [][]int,
	urlGroup int,
) bool {
	// Create replacements map to avoid offset issues
	replacements := make(map[string]string)
	
	for _, match := range regex.FindAllStringSubmatchIndex(*content, -1) {
		pos := match[0]
		if p.isInCodeRange(pos, codeRanges) {
			continue
		}
		
		urlStart := match[urlGroup]
		urlEnd := match[urlGroup+1]
		url := (*content)[urlStart:urlEnd]
		
		// Skip external URLs
		if strings.HasPrefix(url, "http://") || strings.HasPrefix(url, "https://") {
			continue
		}
		
		// Extract path without anchor
		path := url
		anchor := ""
		if idx := strings.Index(url, "#"); idx != -1 {
			path = url[:idx]
			anchor = url[idx:]
		}
		
		// Check if this path needs updating
		if newPath, ok := pathChanges[path]; ok {
			oldMatch := (*content)[match[0]:match[1]]
			newURL := newPath + anchor
			newMatch := strings.Replace(oldMatch, url, newURL, 1)
			replacements[oldMatch] = newMatch
		}
	}
	
	// Apply all replacements
	changed := false
	for old, new := range replacements {
		if strings.Contains(*content, old) {
			*content = strings.Replace(*content, old, new, -1)
			changed = true
		}
	}
	
	return changed
}

func (p *LinkParser) updateHTMLLinks(
	content *string,
	pathChanges map[string]string,
	codeRanges [][]int,
) bool {
	changed := false
	
	// For HTML links, we need a different approach due to attribute order
	for oldPath, newPath := range pathChanges {
		// Skip external URLs
		if strings.HasPrefix(oldPath, "http://") || strings.HasPrefix(oldPath, "https://") {
			continue
		}
		
		searchPattern := `href="` + oldPath + `"`
		replacePattern := `href="` + newPath + `"`
		
		if strings.Contains(*content, searchPattern) {
			*content = strings.ReplaceAll(*content, searchPattern, replacePattern)
			changed = true
		}
	}
	
	return changed
}
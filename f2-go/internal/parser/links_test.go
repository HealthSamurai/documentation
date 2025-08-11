package parser

import (
	"testing"

	"github.com/f2-go/internal/core"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestFindLinks(t *testing.T) {
	tests := []struct {
		name    string
		content string
		want    []core.Link
	}{
		{
			name: "simple markdown link",
			content: `This is a [link](path/to/file.md) in text.`,
			want: []core.Link{
				{Text: "link", URL: "path/to/file.md", Type: core.LinkTypeMarkdown, Line: 1, Column: 11},
			},
		},
		{
			name: "multiple links on same line",
			content: `Check [this](first.md) and [that](second.md) out.`,
			want: []core.Link{
				{Text: "this", URL: "first.md", Type: core.LinkTypeMarkdown, Line: 1, Column: 7},
				{Text: "that", URL: "second.md", Type: core.LinkTypeMarkdown, Line: 1, Column: 28},
			},
		},
		{
			name: "link with anchor",
			content: `See [section](doc.md#important-section) for details.`,
			want: []core.Link{
				{Text: "section", URL: "doc.md", Type: core.LinkTypeMarkdown, Line: 1, Column: 5, IsAnchor: true, Anchor: "important-section"},
			},
		},
		{
			name: "image link",
			content: `![alt text](../images/diagram.png)`,
			want: []core.Link{
				{Text: "alt text", URL: "../images/diagram.png", Type: core.LinkTypeImage, Line: 1, Column: 1},
			},
		},
		{
			name: "reference style link",
			content: `This is a [reference][ref] link.

[ref]: https://example.com`,
			want: []core.Link{
				{Text: "reference", URL: "ref", Type: core.LinkTypeReference, Line: 1, Column: 11},
			},
		},
		{
			name: "html link",
			content: `Click <a href="page.html">here</a> to continue.`,
			want: []core.Link{
				{Text: "here", URL: "page.html", Type: core.LinkTypeHTML, Line: 1, Column: 7},
			},
		},
		{
			name: "links in code blocks should be ignored",
			content: "```\n[link](should-be-ignored.md)\n```\n\nBut [this](should-be-found.md) is ok.",
			want: []core.Link{
				{Text: "this", URL: "should-be-found.md", Type: core.LinkTypeMarkdown, Line: 5, Column: 5},
			},
		},
		{
			name: "inline code should be ignored",
			content: "The `[link](ignored.md)` syntax, but [this](found.md) works.",
			want: []core.Link{
				{Text: "this", URL: "found.md", Type: core.LinkTypeMarkdown, Line: 1, Column: 39},
			},
		},
		{
			name: "multiline content",
			content: `First paragraph with [link1](file1.md).

Second paragraph with [link2](file2.md).

Third with [link3](file3.md) and [link4](file4.md).`,
			want: []core.Link{
				{Text: "link1", URL: "file1.md", Type: core.LinkTypeMarkdown, Line: 1, Column: 22},
				{Text: "link2", URL: "file2.md", Type: core.LinkTypeMarkdown, Line: 3, Column: 23},
				{Text: "link3", URL: "file3.md", Type: core.LinkTypeMarkdown, Line: 5, Column: 12},
				{Text: "link4", URL: "file4.md", Type: core.LinkTypeMarkdown, Line: 5, Column: 35},
			},
		},
		{
			name: "absolute and relative paths",
			content: `- [Root](/README.md)
- [Parent](../guide.md)
- [Current](./setup.md)
- [Child](subfolder/doc.md)
- [External](https://example.com)`,
			want: []core.Link{
				{Text: "Root", URL: "/README.md", Type: core.LinkTypeMarkdown, Line: 1, Column: 3},
				{Text: "Parent", URL: "../guide.md", Type: core.LinkTypeMarkdown, Line: 2, Column: 3},
				{Text: "Current", URL: "./setup.md", Type: core.LinkTypeMarkdown, Line: 3, Column: 3},
				{Text: "Child", URL: "subfolder/doc.md", Type: core.LinkTypeMarkdown, Line: 4, Column: 3},
				{Text: "External", URL: "https://example.com", Type: core.LinkTypeMarkdown, Line: 5, Column: 3},
			},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewLinkParser()
			links := parser.FindLinks([]byte(tt.content))

			assert.Equal(t, len(tt.want), len(links), "number of links")

			for i, want := range tt.want {
				if i >= len(links) {
					break
				}
				got := links[i]

				assert.Equal(t, want.Text, got.Text, "link %d text", i)
				assert.Equal(t, want.URL, got.URL, "link %d URL", i)
				assert.Equal(t, want.Type, got.Type, "link %d type", i)
				assert.Equal(t, want.Line, got.Line, "link %d line", i)
				assert.Equal(t, want.IsAnchor, got.IsAnchor, "link %d isAnchor", i)
				assert.Equal(t, want.Anchor, got.Anchor, "link %d anchor", i)
			}
		})
	}
}

func TestUpdateLinks(t *testing.T) {
	tests := []struct {
		name        string
		content     string
		pathChanges map[string]string
		want        string
		wantChanged bool
	}{
		{
			name: "update single link",
			content: `See [documentation](old/path.md) for details.`,
			pathChanges: map[string]string{
				"old/path.md": "new/location.md",
			},
			want:        `See [documentation](new/location.md) for details.`,
			wantChanged: true,
		},
		{
			name: "update multiple links",
			content: `Check [guide](docs/guide.md) and [reference](docs/ref.md).`,
			pathChanges: map[string]string{
				"docs/guide.md": "documentation/guide.md",
				"docs/ref.md":   "documentation/reference.md",
			},
			want:        `Check [guide](documentation/guide.md) and [reference](documentation/reference.md).`,
			wantChanged: true,
		},
		{
			name: "preserve anchors",
			content: `Jump to [section](file.md#section-1).`,
			pathChanges: map[string]string{
				"file.md": "moved/file.md",
			},
			want:        `Jump to [section](moved/file.md#section-1).`,
			wantChanged: true,
		},
		{
			name: "no changes needed",
			content: `Link to [unchanged](keep.md).`,
			pathChanges: map[string]string{
				"other.md": "moved.md",
			},
			want:        `Link to [unchanged](keep.md).`,
			wantChanged: false,
		},
		{
			name: "skip external links",
			content: `External [link](https://example.com) and [local](local.md).`,
			pathChanges: map[string]string{
				"local.md": "moved/local.md",
			},
			want:        `External [link](https://example.com) and [local](moved/local.md).`,
			wantChanged: true,
		},
		{
			name: "update image links",
			content: `![diagram](old/diagram.png)`,
			pathChanges: map[string]string{
				"old/diagram.png": "assets/diagram.png",
			},
			want:        `![diagram](assets/diagram.png)`,
			wantChanged: true,
		},
		{
			name: "preserve code blocks",
			content: "Text before\n```\n[link](code.md)\n```\nReal [link](real.md).",
			pathChanges: map[string]string{
				"code.md": "moved/code.md",
				"real.md": "moved/real.md",
			},
			want:        "Text before\n```\n[link](code.md)\n```\nReal [link](moved/real.md).",
			wantChanged: true,
		},
		{
			name: "update HTML links",
			content: `Click <a href="old.html">here</a> to continue.`,
			pathChanges: map[string]string{
				"old.html": "new.html",
			},
			want:        `Click <a href="new.html">here</a> to continue.`,
			wantChanged: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewLinkParser()
			result, changed := parser.UpdateLinks([]byte(tt.content), tt.pathChanges)

			assert.Equal(t, tt.wantChanged, changed, "changed flag")
			assert.Equal(t, tt.want, string(result), "updated content")
		})
	}
}

func TestCalculateRelativePath(t *testing.T) {
	tests := []struct {
		name string
		from string
		to   string
		want string
	}{
		{
			name: "same directory",
			from: "docs/guide.md",
			to:   "docs/reference.md",
			want: "reference.md",
		},
		{
			name: "subdirectory",
			from: "docs/guide.md",
			to:   "docs/api/methods.md",
			want: "api/methods.md",
		},
		{
			name: "parent directory",
			from: "docs/guides/intro.md",
			to:   "docs/reference.md",
			want: "../reference.md",
		},
		{
			name: "different branches",
			from: "docs/guides/advanced/tips.md",
			to:   "docs/api/v2/methods.md",
			want: "../../api/v2/methods.md",
		},
		{
			name: "from root",
			from: "README.md",
			to:   "docs/guide.md",
			want: "docs/guide.md",
		},
		{
			name: "to root",
			from: "docs/guide.md",
			to:   "README.md",
			want: "../README.md",
		},
		{
			name: "both at root",
			from: "README.md",
			to:   "LICENSE.md",
			want: "LICENSE.md",
		},
		{
			name: "deep nesting up and down",
			from: "a/b/c/d.md",
			to:   "x/y/z.md",
			want: "../../../x/y/z.md",
		},
		{
			name: "preserve absolute paths",
			from: "docs/guide.md",
			to:   "/absolute/path.md",
			want: "/absolute/path.md",
		},
		{
			name: "handle ./ prefix",
			from: "docs/guide.md",
			to:   "./docs/api.md",
			want: "api.md",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewLinkParser()
			result := parser.CalculateRelativePath(tt.from, tt.to)
			assert.Equal(t, tt.want, result)
		})
	}
}

func TestResolveReferences(t *testing.T) {
	tests := []struct {
		name    string
		content string
		want    map[string]string // reference name -> URL
	}{
		{
			name: "simple references",
			content: `Text with [link][ref1] and [another][ref2].

[ref1]: https://example.com
[ref2]: ../path/to/file.md`,
			want: map[string]string{
				"ref1": "https://example.com",
				"ref2": "../path/to/file.md",
			},
		},
		{
			name: "references with titles",
			content: `[link][ref]

[ref]: /path/to/doc.md "Optional Title"`,
			want: map[string]string{
				"ref": "/path/to/doc.md",
			},
		},
		{
			name: "case insensitive references",
			content: `[Link][REF]

[ref]: target.md`,
			want: map[string]string{
				"ref": "target.md",
			},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewLinkParser()
			refs := parser.ResolveReferences([]byte(tt.content))

			assert.Equal(t, len(tt.want), len(refs), "number of references")

			for key, wantURL := range tt.want {
				gotURL, exists := refs[key]
				require.True(t, exists, "reference %s should exist", key)
				assert.Equal(t, wantURL, gotURL, "reference %s URL", key)
			}
		})
	}
}
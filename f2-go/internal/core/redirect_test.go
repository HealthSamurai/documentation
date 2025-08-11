package core

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestLoadRedirects(t *testing.T) {
	tests := []struct {
		name    string
		content string
		want    *RedirectConfig
		wantErr bool
	}{
		{
			name: "simple redirects",
			content: `redirects:
  old-page: new-page.md
  another-old: another-new.md`,
			want: &RedirectConfig{
				Redirects: map[string]string{
					"old-page":    "new-page.md",
					"another-old": "another-new.md",
				},
			},
		},
		{
			name: "empty redirects",
			content: `redirects: {}`,
			want: &RedirectConfig{
				Redirects: map[string]string{},
			},
		},
		{
			name: "no redirects section",
			content: `other:
  key: value`,
			want: &RedirectConfig{
				Redirects: map[string]string{},
			},
		},
		{
			name: "complex paths",
			content: `redirects:
  guides/old-guide: tutorials/new-guide.md
  api/v1/methods: api/v2/methods.md
  deeply/nested/old/path: new/location/path.md`,
			want: &RedirectConfig{
				Redirects: map[string]string{
					"guides/old-guide":       "tutorials/new-guide.md",
					"api/v1/methods":         "api/v2/methods.md",
					"deeply/nested/old/path": "new/location/path.md",
				},
			},
		},
		{
			name:    "invalid yaml",
			content: `redirects: [not valid`,
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			manager := NewRedirectManager()
			config, err := manager.LoadRedirects([]byte(tt.content))

			if tt.wantErr {
				assert.Error(t, err)
				return
			}

			require.NoError(t, err)
			require.NotNil(t, config)
			assert.Equal(t, len(tt.want.Redirects), len(config.Redirects))

			for k, v := range tt.want.Redirects {
				assert.Equal(t, v, config.Redirects[k])
			}
		})
	}
}

func TestAddRedirect(t *testing.T) {
	tests := []struct {
		name        string
		initial     map[string]string
		from        string
		to          string
		want        map[string]string
		wantErr     bool
		errContains string
	}{
		{
			name:    "add new redirect",
			initial: map[string]string{},
			from:    "old-page",
			to:      "new-page.md",
			want: map[string]string{
				"old-page": "new-page.md",
			},
		},
		{
			name: "add to existing",
			initial: map[string]string{
				"existing": "target.md",
			},
			from: "another",
			to:   "destination.md",
			want: map[string]string{
				"existing": "target.md",
				"another":  "destination.md",
			},
		},
		{
			name: "override existing redirect",
			initial: map[string]string{
				"page": "old-target.md",
			},
			from: "page",
			to:   "new-target.md",
			want: map[string]string{
				"page": "new-target.md",
			},
		},
		{
			name: "prevent self-redirect",
			initial: map[string]string{},
			from:    "page.md",
			to:      "page.md",
			wantErr: true,
			errContains: "self-redirect",
		},
		{
			name: "empty from path",
			initial: map[string]string{},
			from:    "",
			to:      "target.md",
			wantErr: true,
			errContains: "empty",
		},
		{
			name: "empty to path",
			initial: map[string]string{},
			from:    "source",
			to:      "",
			wantErr: true,
			errContains: "empty",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			manager := NewRedirectManager()
			config := &RedirectConfig{Redirects: tt.initial}
			manager.(*redirectManager).config = config

			err := manager.AddRedirect(tt.from, tt.to)

			if tt.wantErr {
				assert.Error(t, err)
				if tt.errContains != "" {
					assert.Contains(t, err.Error(), tt.errContains)
				}
				return
			}

			require.NoError(t, err)
			assert.Equal(t, tt.want, config.Redirects)
		})
	}
}

func TestRemoveRedirectsTo(t *testing.T) {
	tests := []struct {
		name    string
		initial map[string]string
		path    string
		want    map[string]string
	}{
		{
			name: "remove single redirect",
			initial: map[string]string{
				"old1": "target.md",
				"old2": "other.md",
			},
			path: "target.md",
			want: map[string]string{
				"old2": "other.md",
			},
		},
		{
			name: "remove multiple redirects to same target",
			initial: map[string]string{
				"old1": "target.md",
				"old2": "target.md",
				"old3": "other.md",
			},
			path: "target.md",
			want: map[string]string{
				"old3": "other.md",
			},
		},
		{
			name: "no redirects to remove",
			initial: map[string]string{
				"old1": "target1.md",
				"old2": "target2.md",
			},
			path: "nonexistent.md",
			want: map[string]string{
				"old1": "target1.md",
				"old2": "target2.md",
			},
		},
		{
			name:    "empty redirects",
			initial: map[string]string{},
			path:    "any.md",
			want:    map[string]string{},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			manager := NewRedirectManager()
			config := &RedirectConfig{Redirects: tt.initial}
			manager.(*redirectManager).config = config

			err := manager.RemoveRedirectsTo(tt.path)
			require.NoError(t, err)
			assert.Equal(t, tt.want, config.Redirects)
		})
	}
}

func TestUpdateRedirectsTo(t *testing.T) {
	tests := []struct {
		name    string
		initial map[string]string
		oldPath string
		newPath string
		want    map[string]string
	}{
		{
			name: "update single redirect",
			initial: map[string]string{
				"old-url": "getting-started/run-aidbox-locally.md",
				"other":   "different.md",
			},
			oldPath: "getting-started/run-aidbox-locally.md",
			newPath: "readme/getting-started.md",
			want: map[string]string{
				"old-url": "readme/getting-started.md",
				"other":   "different.md",
			},
		},
		{
			name: "update multiple redirects pointing to same file",
			initial: map[string]string{
				"old-url-1": "getting-started/run-aidbox-locally.md",
				"old-url-2": "getting-started/run-aidbox-locally.md",
				"other":     "different.md",
			},
			oldPath: "getting-started/run-aidbox-locally.md",
			newPath: "readme/getting-started.md",
			want: map[string]string{
				"old-url-1": "readme/getting-started.md",
				"old-url-2": "readme/getting-started.md",
				"other":     "different.md",
			},
		},
		{
			name: "no matching redirects",
			initial: map[string]string{
				"old-url": "target.md",
				"other":   "different.md",
			},
			oldPath: "nonexistent.md",
			newPath: "new-location.md",
			want: map[string]string{
				"old-url": "target.md",
				"other":   "different.md",
			},
		},
		{
			name:    "empty redirects",
			initial: map[string]string{},
			oldPath: "old.md",
			newPath: "new.md",
			want:    map[string]string{},
		},
		{
			name: "with path normalization",
			initial: map[string]string{
				"old-url": "getting-started/run-aidbox-locally.md",
			},
			oldPath: "./getting-started/run-aidbox-locally.md",
			newPath: "/readme/getting-started.md",
			want: map[string]string{
				"old-url": "readme/getting-started.md",
			},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			manager := NewRedirectManager()
			config := &RedirectConfig{Redirects: tt.initial}
			manager.(*redirectManager).config = config

			manager.(*redirectManager).UpdateRedirectsTo(tt.oldPath, tt.newPath)
			assert.Equal(t, tt.want, config.Redirects)
		})
	}
}

func TestHasCircularRedirect(t *testing.T) {
	tests := []struct {
		name     string
		existing map[string]string
		from     string
		to       string
		want     bool
	}{
		{
			name:     "no existing redirects",
			existing: map[string]string{},
			from:     "a",
			to:       "b",
			want:     false,
		},
		{
			name: "direct circular redirect",
			existing: map[string]string{
				"b": "a",
			},
			from: "a",
			to:   "b",
			want: true,
		},
		{
			name: "indirect circular redirect",
			existing: map[string]string{
				"b": "c",
				"c": "a",
			},
			from: "a",
			to:   "b",
			want: true,
		},
		{
			name: "long chain circular redirect",
			existing: map[string]string{
				"b": "c",
				"c": "d",
				"d": "e",
				"e": "a",
			},
			from: "a",
			to:   "b",
			want: true,
		},
		{
			name: "no circular redirect",
			existing: map[string]string{
				"x": "y",
				"y": "z",
			},
			from: "a",
			to:   "b",
			want: false,
		},
		{
			name: "redirect to existing chain",
			existing: map[string]string{
				"b": "c",
				"c": "d",
			},
			from: "a",
			to:   "b",
			want: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			manager := NewRedirectManager()
			config := &RedirectConfig{Redirects: tt.existing}
			manager.(*redirectManager).config = config

			result := manager.HasCircularRedirect(tt.from, tt.to)
			assert.Equal(t, tt.want, result)
		})
	}
}

func TestSerializeRedirects(t *testing.T) {
	tests := []struct {
		name      string
		redirects map[string]string
		want      string
	}{
		{
			name:      "empty redirects",
			redirects: map[string]string{},
			want:      "redirects: {}\n",
		},
		{
			name: "single redirect",
			redirects: map[string]string{
				"old-page": "new-page.md",
			},
			want: `redirects:
  old-page: new-page.md
`,
		},
		{
			name: "multiple redirects sorted",
			redirects: map[string]string{
				"zebra":  "z.md",
				"alpha":  "a.md",
				"middle": "m.md",
			},
			want: `redirects:
  alpha: a.md
  middle: m.md
  zebra: z.md
`,
		},
		{
			name: "paths with special characters",
			redirects: map[string]string{
				"path/with/slashes":   "new/path.md",
				"path-with-dashes":    "another.md",
				"path_with_underscores": "third.md",
			},
			want: `redirects:
  path-with-dashes: another.md
  path/with/slashes: new/path.md
  path_with_underscores: third.md
`,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			manager := NewRedirectManager()
			config := &RedirectConfig{Redirects: tt.redirects}
			manager.(*redirectManager).config = config

			result := manager.Serialize()
			assert.Equal(t, tt.want, string(result))
		})
	}
}

func TestRedirectChainPrevention(t *testing.T) {
	// Test that we prevent creating redirect chains
	manager := NewRedirectManager()
	config := &RedirectConfig{
		Redirects: map[string]string{
			"a": "b.md",
		},
	}
	manager.(*redirectManager).config = config

	// Should prevent adding b.md -> c.md because a already redirects to b.md
	err := manager.AddRedirect("b.md", "c.md")
	assert.Error(t, err)
	if err != nil {
		assert.Contains(t, err.Error(), "chain")
	}
}

func TestCleanPath(t *testing.T) {
	tests := []struct {
		path string
		want string
	}{
		{"path.md", "path.md"},
		{"/path.md", "path.md"},
		{"./path.md", "path.md"},
		{"//path.md", "path.md"},
		{"path/.md", "path/.md"},
		{"", ""},
	}

	for _, tt := range tests {
		t.Run(tt.path, func(t *testing.T) {
			// This assumes cleanPath is exposed or we test through public methods
			// For now, we'll test through AddRedirect behavior
			manager := NewRedirectManager()
			config := &RedirectConfig{Redirects: map[string]string{}}
			manager.(*redirectManager).config = config

			// Add redirect with unclean path
			err := manager.AddRedirect(tt.path, "target.md")
			if tt.path == "" {
				assert.Error(t, err)
			} else {
				require.NoError(t, err)
				// Check that the path was cleaned
				_, exists := config.Redirects[tt.want]
				assert.True(t, exists)
			}
		})
	}
}
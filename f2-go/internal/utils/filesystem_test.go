package utils

import (
	"os"
	"path/filepath"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestMemoryFileSystem(t *testing.T) {
	fs := NewMemoryFileSystem()

	t.Run("WriteAndReadFile", func(t *testing.T) {
		content := []byte("Hello, World!")
		path := "test.txt"

		err := fs.WriteFile(path, content, 0644)
		require.NoError(t, err)

		readContent, err := fs.ReadFile(path)
		require.NoError(t, err)
		assert.Equal(t, content, readContent)
	})

	t.Run("FileExists", func(t *testing.T) {
		path := "exists.txt"
		assert.False(t, fs.Exists(path))

		err := fs.WriteFile(path, []byte("content"), 0644)
		require.NoError(t, err)

		assert.True(t, fs.Exists(path))
	})

	t.Run("CreateNestedFile", func(t *testing.T) {
		path := "deep/nested/dir/file.txt"
		content := []byte("nested content")

		err := fs.WriteFile(path, content, 0644)
		require.NoError(t, err)

		assert.True(t, fs.Exists(path))
		assert.True(t, fs.Exists("deep/nested/dir"))
	})

	t.Run("MoveFile", func(t *testing.T) {
		src := "source.txt"
		dst := "destination.txt"
		content := []byte("move me")

		err := fs.WriteFile(src, content, 0644)
		require.NoError(t, err)

		err = fs.MoveFile(src, dst)
		require.NoError(t, err)

		assert.False(t, fs.Exists(src))
		assert.True(t, fs.Exists(dst))

		movedContent, err := fs.ReadFile(dst)
		require.NoError(t, err)
		assert.Equal(t, content, movedContent)
	})

	t.Run("MoveFileToNestedDir", func(t *testing.T) {
		src := "file.txt"
		dst := "new/location/file.txt"
		content := []byte("move to nested")

		err := fs.WriteFile(src, content, 0644)
		require.NoError(t, err)

		err = fs.MoveFile(src, dst)
		require.NoError(t, err)

		assert.False(t, fs.Exists(src))
		assert.True(t, fs.Exists(dst))
	})

	t.Run("RemoveFile", func(t *testing.T) {
		path := "remove-me.txt"

		err := fs.WriteFile(path, []byte("temp"), 0644)
		require.NoError(t, err)
		assert.True(t, fs.Exists(path))

		err = fs.Remove(path)
		require.NoError(t, err)
		assert.False(t, fs.Exists(path))
	})

	t.Run("RemoveDirectory", func(t *testing.T) {
		dir := "remove-dir"
		file := filepath.Join(dir, "file.txt")

		err := fs.WriteFile(file, []byte("in dir"), 0644)
		require.NoError(t, err)

		err = fs.Remove(dir)
		require.NoError(t, err)
		assert.False(t, fs.Exists(dir))
		assert.False(t, fs.Exists(file))
	})

	t.Run("ReadDir", func(t *testing.T) {
		dir := "list-dir"
		files := []string{"a.txt", "b.txt", "c.txt"}

		for _, f := range files {
			err := fs.WriteFile(filepath.Join(dir, f), []byte(f), 0644)
			require.NoError(t, err)
		}

		entries, err := fs.ReadDir(dir)
		require.NoError(t, err)
		assert.Len(t, entries, len(files))

		names := make([]string, len(entries))
		for i, entry := range entries {
			names[i] = entry.Name()
		}
		assert.ElementsMatch(t, files, names)
	})

	t.Run("ReadNonExistentFile", func(t *testing.T) {
		_, err := fs.ReadFile("does-not-exist.txt")
		assert.Error(t, err)
		assert.True(t, os.IsNotExist(err))
	})

	t.Run("MoveNonExistentFile", func(t *testing.T) {
		err := fs.MoveFile("does-not-exist.txt", "destination.txt")
		assert.Error(t, err)
	})
}

func TestFileSystemConcurrency(t *testing.T) {
	fs := NewMemoryFileSystem()

	// Test concurrent writes to different files
	t.Run("ConcurrentWrites", func(t *testing.T) {
		done := make(chan bool, 10)

		for i := 0; i < 10; i++ {
			go func(id int) {
				path := filepath.Join("concurrent", "file"+string(rune('0'+id))+".txt")
				content := []byte("content " + string(rune('0'+id)))
				err := fs.WriteFile(path, content, 0644)
				assert.NoError(t, err)
				done <- true
			}(i)
		}

		for i := 0; i < 10; i++ {
			<-done
		}

		// Verify all files exist
		entries, err := fs.ReadDir("concurrent")
		require.NoError(t, err)
		assert.Len(t, entries, 10)
	})
}

func TestMkdirAll(t *testing.T) {
	fs := NewMemoryFileSystem()

	t.Run("CreateNestedDirectories", func(t *testing.T) {
		path := "a/b/c/d/e"
		err := fs.MkdirAll(path, 0755)
		require.NoError(t, err)

		assert.True(t, fs.Exists(path))
		assert.True(t, fs.Exists("a/b/c"))
		assert.True(t, fs.Exists("a"))
	})

	t.Run("CreateExistingDirectory", func(t *testing.T) {
		path := "existing"
		err := fs.MkdirAll(path, 0755)
		require.NoError(t, err)

		// Should not error on existing directory
		err = fs.MkdirAll(path, 0755)
		assert.NoError(t, err)
	})
}
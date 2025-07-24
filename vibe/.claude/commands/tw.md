# Technical Writing Template (/tw)

This template provides a structured approach for writing technical documentation for Aidbox, targeting developers and architects with basic technical knowledge and minimal knowledge of Aidbox.

## Writing Philosophy

You are writing **book-like documentation** that is interesting to read, following the PostgreSQL documentation style. Write the state of Aidbox, not trying to "sell" features.

## Quick Start Command

When starting a new documentation task, copy this template structure:

```markdown
<!-- First line: Target file for merging (e.g., For merging into: ./api-overview.md) -->
For merging into: [target-file.md]

# [Feature/API Name]

[1-3 paragraphs providing overview - book-like, interesting to read for developers]

## [Section Name]

[1-3 paragraphs for each section to make it feel readable, like a book]

[Content describing the state of the feature, not selling it]

```[language]
// Example code showing usage
```

[More content if needed]

See also:
- [Related Feature](link)
- [FHIR Specification](link) <!-- if applicable -->
- [PostgreSQL Feature](link) <!-- when mentioning PostgreSQL -->
```

## Documentation Workflow

### 1. Section-by-Section Approach
1. Write each section in a separate file: `/vibe/<section-name>.md`
2. First line must mention target file: `For merging into: ./api-overview.md`
3. Request feedback in `/vibe/editor-notes.md`
4. Wait for "EDITOR NOTES:" response
5. Only merge when editor confirms "done"

### 2. Editor Notes Process
```markdown
<!-- In /vibe/editor-notes.md -->
## Iteration [Number]: [Section Name]

[Summary of what was written]

Waiting for editor feedback...

EDITOR NOTES: [Editor's feedback will appear here]
```

### 3. Source Tracking
Maintain all read resources in `/vibe/sources-readed.txt`:
```
- /docs/api/rest-api/fhir-search
- /docs/modules/profiling-and-validation/README.md
- https://www.hl7.org/fhir/http.html
- https://www.postgresql.org/docs/current/ddl-basics.html
```

## Writing Rules Checklist

### Structure Rules
- [ ] **Book-like overview**: 1-3 paragraphs per section for readability
- [ ] **Header flexibility**: Can rewrite header names, but don't add/remove headers
- [ ] **Single H1**: Only one H1 (title), all sections use H2 (can use H3, H4)
- [ ] **No header for references**: "See also:" goes after content, before next header

### Content Rules
- [ ] **State, don't sell**: Describe what Aidbox does, not why it's great
- [ ] **FHIR mentions**: Note when features implement FHIR specifications
- [ ] **PostgreSQL links**: Link to PostgreSQL docs when mentioning its features
- [ ] **Code formatting**: Use `code` for operations like `$something`
- [ ] **First mention links**: Add reference when introducing new terms
- [ ] **List links**: Try to add relevant links to list items

### Style Prohibitions
- [ ] **NO bold patterns**: Never use "**Something**: some text"
- [ ] **NO emojis**: Unless explicitly requested
- [ ] **NO restoration**: If editor removes something, don't add it back

### Final Steps
- [ ] **Overview paragraph**: After H1, before first H2, explain page contents
- [ ] **SEO description**: Add one-line description at file start:
  ```yaml
  ---
  description: [One-line description for UI and SEO]
  ---
  ```

## Document Types

### API Overview Documentation
```markdown
For merging into: ./api-overview.md

## [API Category]

[1-3 paragraphs explaining what this category of APIs does, book-like style]

### [Specific API]

[Real-world problem this API solves, starting with concrete scenario]

[How the API works conceptually, using simple language]

```http
POST /endpoint
Content-Type: application/json

{
  "example": "request"
}
```

[More details about usage, edge cases, performance considerations]

See also:
- [FHIR HTTP API](https://www.hl7.org/fhir/http.html)
- [Related Aidbox Feature](../path/to/doc.md)
- [PostgreSQL Feature](https://postgresql.org/docs/current/feature.html)
```

### Module Documentation
```markdown
For merging into: ./modules/[module-name]/README.md

# [Module Name]

[Overview explaining the module's purpose and value]

## How It Works

[Conceptual explanation before implementation details]

### Configuration

[Configuration options and examples]

```yaml
modules:
  module-name:
    setting: value
```

See also:
- [Configuration Reference](../../reference/settings/modules.md)
- [Related Tutorial](../../tutorials/module-tutorials/example.md)
```

## Source Materials

### Required Reading
- `/docs/` - Main documentation directory
- `/vibe/styleguide.md` - Project style guide
- `/ai_docs/writing-documentation.md` - This document's source

### Example Sources
- FHIR Search: `/docs/api/rest-api/fhir-search`
- Validation: `/docs/modules/profiling-and-validation/README.md`
- FHIR HTTP API: https://www.hl7.org/fhir/http.html
- PostgreSQL style reference: https://www.postgresql.org/docs/current/ddl-basics.html

## Common Patterns

### FHIR Implementation Note
```markdown
Aidbox implements the [FHIR Search specification](https://www.hl7.org/fhir/search.html) 
with additional extensions for production healthcare environments.
```

### PostgreSQL Feature Reference
```markdown
This feature leverages PostgreSQL's [JSONB indexes](https://www.postgresql.org/docs/current/datatype-json.html#JSON-INDEXING) 
for efficient querying of nested FHIR resources.
```

### See Also Section
```markdown
See also:
- [FHIR Specification](https://www.hl7.org/fhir/spec.html)
- [Related Feature](../path/to/feature.md)
- [Tutorial](../../tutorials/example.md)
- [API Reference](../../reference/api.md)
```

## Notes

- This template follows `/ai_docs/writing-documentation.md` guidelines
- Always verify with `/vibe/styleguide.md` for detailed style rules
- Use web search to clarify technical topics when needed
- Track all sources in `/vibe/sources-readed.txt`


$ARGUMENTS

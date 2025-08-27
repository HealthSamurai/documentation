# 001. Search Dropdown Behavior and Visual Hierarchy

## Title
Search Dropdown Behavior and Visual Hierarchy

## Status
Accepted

## Context

The search dropdown in our documentation system uses Meilisearch to provide fast, relevant search results. The search results are organized in a hierarchical structure with levels from `lvl0` to `lvl6`, plus optional `content` fields. 

### Problems to Solve:
1. **Visual Hierarchy**: Users need to quickly understand the relationship between different levels of documentation
2. **Context Preservation**: When showing search results, users need to understand where in the documentation structure each result belongs
3. **Order Preservation**: Results from Meilisearch are ranked by relevance and this order must be preserved
4. **Duplication Avoidance**: Avoid showing redundant information (e.g., repeating parent level in subtitle when it's obvious from context)
5. **Efficient Space Usage**: Display maximum useful information in minimal space

### Hierarchy Levels:
- **lvl0**: Product/Section (e.g., "AIDBOX", "FHIR SEARCH")
- **lvl1**: Main page title (e.g., "API Overview", "SearchParameter")
- **lvl2**: Major section heading (e.g., "FHIR Search", "Search Parameter Types")
- **lvl3**: Subsection (e.g., "string", "token", "reference")
- **lvl4**: Sub-subsection (e.g., "Docker (cross-platform)", "Windows")
- **lvl5**: Further nesting
- **lvl6**: Code snippets or special content (displayed in monospace)
- **content**: Descriptive text for any level

## Decision

### 1. Grouping Logic

The grouping algorithm should be generalized for all hierarchy levels:

```
For results at level X:
- If all results share the same values for levels 0 through X-1:
  - Group them together
  - Show level X-1 as a separate header (if X > 1)
  - Show level X items as grouped children
- Otherwise:
  - Show them ungrouped
```

#### Examples:

**Case 1: Single page match**
```
Results: [lvl1: "API Overview"]
Display: Single ungrouped result
```

**Case 2: Page with sections**
```
Results: [
  {lvl1: "SearchParameter", lvl2: null},
  {lvl1: "SearchParameter", lvl2: "SearchParameter fields"},
  {lvl1: "SearchParameter", lvl2: "Search Parameter Types"}
]
Display: Grouped under "SearchParameter" header
```

**Case 3: Multiple lvl3 under same lvl2**
```
Results: [
  {lvl1: "SearchParameter", lvl2: "Search Parameter Types", lvl3: "string"},
  {lvl1: "SearchParameter", lvl2: "Search Parameter Types", lvl3: "token"},
  {lvl1: "SearchParameter", lvl2: "Search Parameter Types", lvl3: "reference"}
]
Display: 
- "Search Parameter Types" as header
- "string", "token", "reference" as grouped children
```

### 2. Visual Hierarchy

#### Icons and Styling:
- **lvl1 (main page)**: 📄 File icon, bold text, no padding
- **lvl2 (section)**: # Hash icon, normal weight, 10 units padding
- **lvl3 (subsection)**: Left border (no icon), 12 units padding
- **lvl4-lvl5 (deep nesting)**: Left border (no icon), 14 units padding
- **lvl6 (code/special)**: Monospace font in tinted background box

#### Display Rules:

**Title (Main Text):**
- For grouped items: Show the deepest available level (lvl5 > lvl4 > lvl3 > lvl2)
- For ungrouped items: Show lvl1 (or lvl0 if no lvl1)

**Subtitle (Context):**
- For grouped items:
  - lvl5: Show "lvl2 › lvl3 › lvl4" as breadcrumb
  - lvl4: Show "lvl2 › lvl3" as breadcrumb
  - lvl3: Don't show subtitle (parent is obvious from grouping)
  - lvl2: Don't show subtitle
- For ungrouped items: Show "lvl2 › lvl3 › lvl4 › lvl5" if any exist

**lvl0 Display:**
- Show in UPPERCASE, gray color
- Display within the lvl1 container (not as separate element)
- Only show when different from current product context

**Content:**
- Always display when available
- Show after title/subtitle
- Truncate to 100 characters with ellipsis
- Use smaller, muted text color
- Special case: Don't show if lvl6 is present (lvl6 takes precedence)

**lvl6 (Code/Special Content):**
- Display in monospace font
- Use tinted background (e.g., `bg-tint-3`)
- Show as inline-block element
- Takes precedence over regular content

### 3. Interaction Patterns

#### Keyboard Navigation:
- **Arrow Up/Down**: Navigate through results
- **Enter**: Open selected result
- **Escape**: Close dropdown
- **Ctrl/Cmd + K**: Focus search input

#### Highlight During Navigation:
- Selected item gets `bg-warning-2` background
- Smooth scroll into view for off-screen items
- Visual indication with index tracking

### 4. Search Strategy

#### Federated Search (maintained, not changed):
1. First query: Exact match on `hierarchy_lvl6` with weight 1.2
2. Second query: General search with weight 1.0
3. Results merged preserving relevance order

#### Highlighting:
- Use Meilisearch's `_formatted` fields
- Apply `<mark>` tags with warning background
- No additional client-side highlighting needed

### 5. Mobile Differences

#### Desktop:
- Width: `md:w-[32rem]` (32rem on medium+ screens)
- Shadow: `shadow-lg ring-1 ring-tint-subtle`
- Position: Absolute, aligned to search input

#### Mobile:
- Width: Full width of container
- Border: `border border-tint-6` (simpler border)
- Position: Full screen overlay
- Additional close button
- Larger touch targets

### 6. Edge Cases

#### Empty Query:
- Return empty div
- Don't show dropdown

#### No Results:
- Show "No results found for "{query}"" message
- Maintain dropdown styling for consistency

#### Single Result:
- Show without grouping
- Direct link without parent container

#### Results Without Content:
- Show title and subtitle only
- No empty content div

#### Very Long Content:
- Truncate at 100 characters
- Add ellipsis (...)
- Full content visible on hover (future enhancement)

## Consequences

### Positive:
1. **Clear Visual Hierarchy**: Users instantly understand document structure
2. **Efficient Space Usage**: No redundant information displayed
3. **Preserved Relevance**: Meilisearch ranking is maintained
4. **Intuitive Navigation**: Keyboard shortcuts match user expectations
5. **Responsive Design**: Optimized for both desktop and mobile
6. **Performance**: Minimal client-side processing, leveraging Meilisearch formatting

### Negative:
1. **Complexity**: Grouping logic is more complex than simple flat list
2. **Testing**: Multiple edge cases require comprehensive testing
3. **Maintenance**: Changes to hierarchy structure require code updates

### Neutral:
1. **Learning Curve**: Users need to understand the visual language (icons, indentation)
2. **Screen Reader Compatibility**: Requires proper ARIA labels (future enhancement)

## Implementation

### Component Structure:
```clojure
meilisearch-dropdown
├── group-results-by-page     ; Generalizes grouping for all levels
├── render-result-item         ; Handles individual item rendering
│   ├── determine-title        ; Deepest level logic
│   ├── build-subtitle         ; Context breadcrumb
│   ├── apply-padding          ; Visual hierarchy
│   └── render-icons           ; Icon selection
└── keyboard-navigation        ; Arrow keys and selection
```

### CSS Classes Used:
- Padding: `pl-10`, `pl-12`, `pl-14`, `px-3`
- Typography: `text-sm`, `text-xs`, `font-semibold`, `font-normal`, `font-mono`
- Colors: `text-tint-strong`, `text-tint-10`, `text-tint-9`, `bg-tint-3`, `bg-warning-2`
- Layout: `flex`, `items-center`, `gap-3`, `rounded-md`

## Examples

### Example 1: Simple Page Result
```clojure
Input: [{:lvl1 "Getting Started" :content "Quick start guide"}]
Output:
📄 Getting Started
   Quick start guide
```

### Example 2: Page with Sections
```clojure
Input: [
  {:lvl1 "SearchParameter" :lvl2 nil}
  {:lvl1 "SearchParameter" :lvl2 "Fields"}
  {:lvl1 "SearchParameter" :lvl2 "Types"}
]
Output:
[grouped container]
  📄 SearchParameter
  #  Fields
  #  Types
```

### Example 3: Nested Hierarchy
```clojure
Input: [
  {:lvl2 "Installation" :lvl3 "Docker" :lvl4 "Linux"}
  {:lvl2 "Installation" :lvl3 "Docker" :lvl4 "Windows"}
]
Output:
[grouped under "Installation"]
  # Installation
  │ Docker
  │ └─ Linux
  │ └─ Windows
```

### Example 4: With lvl0 and lvl6
```clojure
Input: [{
  :lvl0 "AIDBOX"
  :lvl1 "API Reference"
  :lvl2 "REST"
  :lvl3 "Create"
  :lvl6 "POST /Patient"
}]
Output:
AIDBOX (in gray, uppercase)
📄 API Reference
  # REST › Create
    `POST /Patient` (monospace, tinted bg)
```

## Future Enhancements

1. **Accessibility**: Add ARIA labels and keyboard announcements
2. **Preview on Hover**: Show more content on hover
3. **Recent Searches**: Cache and show recent searches
4. **Search Analytics**: Track popular searches for documentation improvements
5. **Fuzzy Matching**: Handle typos and variations
6. **Category Filters**: Allow filtering by lvl0/product
7. **Smart Grouping**: Group by semantic similarity, not just hierarchy

## References

- [Meilisearch Documentation](https://www.meilisearch.com/docs)
- [HTMX Documentation](https://htmx.org/docs/)
- [Tailwind CSS Classes](https://tailwindcss.com/docs)
- Original issue: Search dropdown UX improvements
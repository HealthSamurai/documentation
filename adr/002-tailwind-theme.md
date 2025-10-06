# 002. Tailwind v4 Semantic Theme Architecture

## Title
Tailwind v4 Semantic Theme Architecture and Color System Migration

## Status
Accepted

## Context

Our documentation application currently uses Tailwind v4 with a custom color system defined in `resources/public/app.css` using the `@theme` directive. However, the current implementation has several significant problems that affect maintainability, consistency, and developer experience.

### Current Problems

#### 1. Extensive Hardcoded Colors (40+ instances)

Throughout our Clojure UI components, colors are hardcoded as hex values in class attributes:

**Files with hardcoded colors:**
- `src/gitbok/ui/tags.clj`: 10+ hardcodes
  - `#EBEFF2B2`, `#353B50`, `#DDE1E8`, `#7E8291`, `#F6F7F9`, `#717684`, `#5a5d68`
- `src/gitbok/ui/landing_hero.clj`: 20+ hardcodes
  - `#F8F9FA`, `#E7E9EF`, `#DADDE7`, `#353B50`, `#D95640`, `#C94A36`
- `src/gitbok/ui/examples.clj`: 15+ hardcodes (same patterns)
- `src/gitbok/ui/main_navigation.clj`: 10+ hardcodes (same patterns)
- `src/gitbok/ui/layout.clj`: 5+ hardcodes

**Example of the problem:**
```clojure
;; Non-semantic, hard to maintain
[:div {:class "bg-[#F8F9FA] border border-[#E7E9EF] hover:border-[#DADDE7]"}]
```

This approach makes it:
- Difficult to apply consistent color changes
- Impossible to implement theming or dark mode
- Hard to understand the semantic purpose of colors
- Error-prone when colors need to be updated

#### 2. Non-Semantic Variable Names (50+ variables)

The current `@theme` block in `app.css` uses non-semantic naming:

```css
--color-tint-1: rgb(255, 255, 255);
--color-tint-2: rgb(251, 249, 249);
/* ... */
--color-tint-12: rgb(31, 28, 28);
```

Problems:
- Names like `tint-7` don't indicate purpose (Is it for text? Background? Border?)
- Developers must memorize arbitrary number mappings
- No clear relationship between variables and their use cases

#### 3. Unused Variables (35+ variables)

Analysis of the codebase reveals extensive unused color definitions:

**Unused palettes (to be removed):**
- Primary palette: `primary-1`, `primary-3` through `primary-8`, `primary-11`, `primary-12` (10 unused)
- Neutral palette: Almost all `neutral-1` through `neutral-12` except `neutral-9` (11 unused)
- Info palette: All `info-*` colors (4 unused)
- Success palette: All `success-*` colors (4 unused)
- Danger palette: All `danger-*` colors (4 unused)
- Warning palette: `warning-3`, `warning-9`, `warning-12` (3 unused, only `warning-2` used)
- Duplicate: `grey-12` (duplicate of `tint-12`)

**Total: ~35 unused variables** cluttering the theme definition.

#### 4. Pending Designer Changes

Our designer provided 7 color updates (documented in `migration-plan-ru.md`) that have not been applied:

| Current Color | Current Name | New Color | Purpose |
|---------------|--------------|-----------|---------|
| `#1F1C1C` | tint-12 | `#717684` | Main text (lighter, more gray) |
| `#766C69` | tint-11 | `#353B50` | Secondary text (darker, blue-gray) |
| `#987E78` | tint-9 | `#717684` | Muted text (same as new main text) |
| `#FBF9F9` | tint-2, tint-subtle | `#FDFDFE` | Code blocks (nearly white) |
| `#FAF7F6` | tint-3 | `#EBECEE` | Subtle borders (cooler, gray-blue) |

These changes shift the palette from warm browns to cooler grays, improving readability and modernizing the aesthetic.

#### 5. Inconsistent Color Temperature

Current theme mixes warm (tint system) and cool (some hardcoded) colors:
- Tint system: Warm browns (`#766C69`, `#987E78`)
- Hardcoded: Cool grays (`#F8F9FA`, `#E7E9EF`)
- This creates visual inconsistency

### Requirements for New System

Based on industry best practices for Tailwind v4 and semantic design systems:

1. **Semantic naming**: Colors should describe their purpose (e.g., `text-primary`, `surface-muted`)
2. **Minimal set**: Only colors actually used in the application
3. **Designer alignment**: Incorporate all 7 pending color changes
4. **Migration path**: Clear mapping from hardcodes to semantic names
5. **Maintainability**: Easy to update colors globally
6. **Tailwind v4 native**: Use `@theme` directive properly to auto-generate utilities

## Decision

We will implement a semantic color system with **17 carefully chosen variables** organized into 5 logical groups following Material Design's "on-color" pattern: Brand, On-Surface (text), Surface (backgrounds), Outline (borders), and Warning.

This structure follows Material Design principles and Tailwind v4 best practices, using semantic naming that avoids double-prefix utilities (e.g., `text-on-surface` instead of `text-text-primary`).

### New Theme Architecture

#### Brand Colors (4 variables)

Purpose: Brand identity, primary actions, active states

```css
--color-brand: #D95640;
/* Coral/red brand color
 * Used in: Primary buttons, active links, hover states for navigation, terminal prompt
 * Locations: ~20+ places across landing_hero.clj, left_navigation.clj, breadcrumb.clj
 * Utilities: bg-brand, text-brand, border-brand
 */

--color-on-brand: #FFFFFF;
/* Text/icons on brand backgrounds (Material Design pattern)
 * Used in: Button text, icon colors on brand backgrounds
 * Locations: landing_hero.clj (COPY button), button components
 * Utilities: text-on-brand
 */

--color-brand-hover: #CA4833;
/* Darker brand color for hover states
 * Used in: Button hovers, interactive element hovers
 * Locations: ~5+ places in landing_hero.clj, main_navigation.clj
 * Utilities: hover:bg-brand-hover
 */

--color-brand-light: #FFF8F6;
/* Very light brand tint for subtle backgrounds
 * Used in: Active navigation item backgrounds
 * Locations: summary.clj (active:hover:bg state)
 * Utilities: bg-brand-light
 */
```

#### On-Surface Colors (4 variables)

Purpose: Text and icons on surface backgrounds (Material Design "on-color" pattern)

```css
--color-on-surface: #717684;
/* Default text on surfaces - NEW FROM DESIGNER (was #1F1C1C)
 * Change: Lighter, more gray (from near-black to medium gray)
 * Used in: Medium-emphasis text, navigation labels, button text
 * Locations: 15+ places - main_navigation.clj (Login button), breadcrumb.clj
 * Replaces: tint-9, tint-10
 * Utilities: text-on-surface
 */

--color-on-surface-strong: #353B50;
/* High-emphasis text - NEW FROM DESIGNER (was #766C69)
 * Change: Darker, blue-gray tint (from warm brown to cool blue-gray)
 * Used in: Headings, titles, body text, descriptions
 * Locations: 50+ places - landing_hero.clj, examples.clj, layout.clj, tags.clj
 * Replaces: tint-11, tint-12, tint-strong, hardcoded #353B50
 * Utilities: text-on-surface-strong
 * NOTE: This is the primary body text color
 */

--color-on-surface-muted: #717684;
/* Low-emphasis text - NEW FROM DESIGNER (was #987E78)
 * Change: Cooler gray, same as on-surface (simplifies hierarchy)
 * Used in: Helper text, captions, figure captions, muted labels
 * Locations: 10+ places - app.css (@apply rules), breadcrumb.clj
 * Replaces: tint-9, neutral-9
 * Utilities: text-on-surface-muted
 */

--color-on-surface-placeholder: #7E8291;
/* Input placeholders, disabled states
 * Used in: Search input placeholders, disabled text, kbd shortcuts
 * Locations: main_navigation.clj (search), tags.clj (hover states)
 * Replaces: Hardcoded #7E8291
 * Utilities: text-on-surface-placeholder, placeholder:text-on-surface-placeholder
 */
```

#### Surface Colors (4 variables)

Purpose: Backgrounds for different UI surfaces

```css
--color-surface: #FFFFFF;
/* Pure white - main background
 * Used in: Page backgrounds, card backgrounds, modal backgrounds, search input
 * Locations: Everywhere - layout.clj, tabs.clj, cards.clj, main_navigation.clj
 * Replaces: tint-1, tint-base, white
 * Utilities: bg-surface
 */

--color-surface-subtle: #FDFDFE;
/* Nearly white - NEW FROM DESIGNER (was #FBF9F9)
 * Change: Even closer to white, cooler tone
 * Used in: Code block backgrounds, inline code backgrounds
 * Locations: app.css (@apply rules for article code blocks)
 * Replaces: tint-2, tint-subtle
 * Utilities: bg-surface-subtle
 */

--color-surface-alt: #F8F9FA;
/* Light gray background - FROM HARDCODES
 * Used in: Card backgrounds (with gradients), Zulip banner, footer background
 * Locations: 15+ places - landing_hero.clj (card gradients), examples.clj, tags.clj
 * Replaces: tint-3, hardcoded #F8F9FA (most common hardcode)
 * Utilities: bg-surface-alt
 * NOTE: Often used in gradients: bg-gradient-to-b from-white to-surface-alt
 */

--color-surface-hover: #F3F5F9;
/* Hover state background - cooler tone
 * Used in: Table row hovers, navigation item hovers, dropdown hovers, kbd shortcuts
 * Locations: app.css (table hover), main_navigation.clj, summary.clj
 * Replaces: tint-hover, hardcoded #F6F7F9, #F3F5F9
 * Utilities: hover:bg-surface-hover, bg-surface-hover
 */
```

#### Outline Colors (4 variables)

Purpose: Borders, dividers, and separators (using "outline" instead of "border" to avoid double-prefix)

```css
--color-outline: #E7E9EF;
/* Default outline/border color - FROM HARDCODES
 * Used in: Card borders, input borders, default UI element borders, filters panel
 * Locations: 15+ places - landing_hero.clj (cards), examples.clj, main_navigation.clj (search input)
 * Replaces: tint-6, hardcoded #E7E9EF (second most common hardcode)
 * Utilities: border-outline
 */

--color-outline-hover: #DADDE7;
/* Border hover state - FROM HARDCODES
 * Used in: Interactive element border hovers (cards, buttons)
 * Locations: 3+ places - landing_hero.clj, examples.clj, main_navigation.clj (Login button)
 * Replaces: tint-7, hardcoded #DADDE7, #DDE1E8
 * Utilities: hover:border-outline-hover, border-outline-hover
 */

--color-outline-subtle: #EBECEE;
/* Subtle borders - NEW FROM DESIGNER (was #FAF7F6)
 * Change: From warm off-white to cool gray
 * Used in: Table header backgrounds, tag backgrounds, ring decorations
 * Locations: app.css (table thead, inline code ring), meilisearch.clj (tags)
 * Replaces: tint-3, tint-4
 * Utilities: border-outline-subtle, ring-outline-subtle
 * NOTE: Sometimes used for decorative rings (ring-outline-subtle)
 */

--color-outline-strong: #EBE4E2;
/* Strong/emphasized borders
 * Used in: Emphasized borders, structural dividers, table cell borders
 * Locations: app.css (code blocks, tables), layout.clj (footer border)
 * Replaces: tint-6, tint-8
 * Utilities: border-outline-strong, divide-outline-strong
 */
```

#### Warning Colors (1 variable)

Purpose: Highlighting and attention

```css
--color-warning-bg: #FFF4EC;
/* Warning/highlight background (light orange)
 * Used in: Search result highlighting in Meilisearch
 * Locations: meilisearch.clj (mark highlighting), app.css (mark styling)
 * Replaces: warning-2
 * Utilities: bg-warning-bg
 * NOTE: Only warning color we actually use - others (warning-3, 9, 12) unused
 */
```

### Utility Class Generation

With Tailwind v4's `@theme` directive, each variable automatically generates utility classes:

**Example for `--color-text-primary: #717684;`:**
- `text-text-primary` - for text color
- `bg-text-primary` - for background color
- `border-text-primary` - for border color
- `fill-text-primary` - for SVG fills
- Plus hover, focus, active variants

This gives us full Tailwind utility support without manual configuration.

### Namespace Explanation

We use double-prefix pattern (`text-text-primary`, `bg-surface-muted`) because:
1. First prefix: Tailwind utility type (`text-`, `bg-`, `border-`)
2. Second prefix: Semantic category from variable name (`text-`, `surface-`, `border-`)

While this creates some redundancy (`text-text-*`), it maintains clarity and follows Tailwind v4 conventions.

**Alternative considered and rejected:** Using `--color-primary-text` would generate `text-primary-text`, which is less clear about the category structure.

## Migration Mapping

This section provides complete mapping tables for migrating from the current system to the new semantic system.

### Table 1: Designer Color Changes

These 7 changes update our color palette from warm browns to cooler grays:

| Old Color | Old CSS Variable | New Color | New CSS Variable | Visual Change | Impact | Usage Count |
|-----------|------------------|-----------|------------------|---------------|--------|-------------|
| `#1F1C1C` | `--color-tint-12`, `--color-tint-strong` | `#717684` | `--color-on-surface` | Near-black → Medium gray | Medium-emphasis text for navigation, labels | 15+ |
| `#766C69` | `--color-tint-11` | `#353B50` | `--color-on-surface-strong` | Warm brown → Cool blue-gray | **CRITICAL** - Primary body text gets darker and cooler | 50+ |
| `#987E78` | `--color-tint-9`, `--color-tint-10` | `#717684` | `--color-on-surface-muted` | Muted brown → Same as on-surface | Simplifies text hierarchy | 15+ |
| `#FBF9F9` | `--color-tint-2`, `--color-tint-subtle` | `#FDFDFE` | `--color-surface-subtle` | Off-white → Nearly pure white | Code blocks become almost white | 5+ |
| `#FAF7F6` | `--color-tint-3` | `#EBECEE` | `--color-outline-subtle` | Warm off-white → Cool gray | Table headers, tags get cooler tone | 3+ |

**Notes:**
- tint-9 and tint-10 both map to `on-surface-muted` (they were nearly identical)
- `#717684` is used for both `on-surface` and `on-surface-muted` (designer's choice)
- Material Design pattern: "on-surface" means text/icons on surface backgrounds
- These changes must be applied atomically (all at once) to maintain visual consistency

### Table 2: Hardcoded Colors → Semantic Variables

Complete mapping of all 40+ hardcoded hex colors to new semantic names:

| Hardcode | Occurrences | New Semantic Variable | Affected Files | Notes |
|----------|-------------|----------------------|----------------|-------|
| `#F8F9FA` | 15+ | `--color-surface-alt` | `landing_hero.clj`, `examples.clj`, `tags.clj` | Most common hardcode - card backgrounds, footer |
| `#E7E9EF` | 15+ | `--color-outline` | `landing_hero.clj`, `examples.clj`, `main_navigation.clj` | Second most common - default border color |
| `#DADDE7` | 3+ | `--color-outline-hover` | `landing_hero.clj`, `examples.clj` | Used for hover state borders |
| `#353B50` | 20+ | `--color-on-surface-strong` | `layout.clj`, `main_navigation.clj`, `landing_hero.clj` | Primary body text color |
| `#7E8291` | 5+ | `--color-on-surface-placeholder` | `main_navigation.clj`, `tags.clj` | Search placeholders, kbd shortcuts |
| `#EBEFF2B2` | 5+ | `--color-surface-alt` + `/70` | `tags.clj` | Note: B2 (70% opacity) → Tailwind v4 `/70` syntax |
| `#DDE1E8` | 5+ | `--color-outline-hover` | `tags.clj` | Similar to #DADDE7, maps to same variable |
| `#F6F7F9` | 5+ | `--color-surface-hover` | `tags.clj`, `main_navigation.clj` | Hover backgrounds, kbd shortcuts |
| `#717684` | 2+ | `--color-on-surface` | `main_navigation.clj` | NEW color from designer (Login button) |
| `#5a5d68` | 1 | `--color-on-surface-strong` | `tags.clj` | Language tag hover (close to on-surface-strong) |
| `#D95640` | 2+ | `--color-brand` | `landing_hero.clj` | Terminal prompt, COPY button |
| `#C94A36` | 1 | `--color-brand-hover` | `landing_hero.clj` | Button hover states |

### Table 3: Old Theme Variables → New Semantic Variables

Mapping existing `tint-*` variables to new semantic names:

| Old Variable | Old Value | Keep? | New Variable | New Value | Notes |
|--------------|-----------|-------|--------------|-----------|-------|
| `tint-1` | `#FFFFFF` | ✅ | `surface` | `#FFFFFF` | Pure white background |
| `tint-2` | `#FBF9F9` | ❌ | `surface-subtle` | `#FDFDFE` | **CHANGED** by designer |
| `tint-3` | `#FAF7F6` | ❌ | `surface-alt` / `outline-subtle` | `#F8F9FA` / `#EBECEE` | Split usage: backgrounds vs borders |
| `tint-4` | `#F6F1F0` | ❌ | `outline-subtle` | `#EBECEE` | **CHANGED** by designer (ring decorations) |
| `tint-5` | `#F2ECEA` | ❌ | (removed) | - | Not used enough to keep |
| `tint-6` | `#EBE4E2` | ✅ | `outline` / `outline-strong` | `#E7E9EF` / `#EBE4E2` | Split: default vs emphasized |
| `tint-7` | `#E0D7D5` | ❌ | `outline-hover` | `#DADDE7` | **CHANGED** to cooler tone |
| `tint-8` | `#E5E7EB` | ❌ | `outline-strong` | `#EBE4E2` | Table dividers, emphasized borders |
| `tint-9` | `#987E78` | ❌ | `on-surface-muted` | `#717684` | **CHANGED** by designer |
| `tint-10` | `#8C726D` | ❌ | `on-surface` | `#717684` | **CHANGED** by designer (merged with 9) |
| `tint-11` | `#766C69` | ❌ | `on-surface-strong` | `#353B50` | **CHANGED** by designer |
| `tint-12` | `#1F1C1C` | ❌ | `on-surface-strong` | `#353B50` | **CHANGED** by designer (now primary text) |
| `tint-base` | `#FFFFFF` | ✅ | `surface` | `#FFFFFF` | Duplicate of tint-1 |
| `tint-subtle` | `#FBF9F9` | ❌ | `surface-subtle` | `#FDFDFE` | **CHANGED** by designer |
| `tint-hover` | `#F3F5F9` | ✅ | `surface-hover` | `#F3F5F9` | No change |
| `tint-active` | `#EBE4E2` | ❌ | (removed) | - | Can use outline-strong |
| `tint-strong` | `#1F1C1C` | ❌ | `on-surface-strong` | `#353B50` | **CHANGED** by designer |

### Table 4: Variables to Remove (35 total)

These variables are defined in the theme but never used in the codebase:

**Primary palette (10 unused):**
- `primary-1` ✅ Keep as `brand-light` (used in summary.clj)
- `primary-2` ✅ Keep as `brand-light`
- `primary-3` through `primary-8` ❌ Remove (6 variables)
- `primary-9` ✅ Keep as `brand`
- `primary-10` ✅ Keep as `brand-hover`
- `primary-11`, `primary-12` ❌ Remove (2 variables)

**Neutral palette (11 unused):**
- All `neutral-1` through `neutral-12` ❌ Remove except:
- `neutral-9` ✅ Keep (used in figcaption - mapped to `on-surface-muted`)

**Semantic palettes (15 unused):**
- All `info-*` (2, 3, 9, 12) ❌ Remove (4 variables)
- All `success-*` (2, 3, 9, 12) ❌ Remove (4 variables)
- `warning-3`, `warning-9`, `warning-12` ❌ Remove (3 variables)
- `warning-2` ✅ Keep as `warning-bg`
- All `danger-*` (2, 3, 9, 12) ❌ Remove (4 variables)

**Duplicates (1):**
- `grey-12` ❌ Remove (exact duplicate of `tint-12`)

**Special variables:**
- `header-bg`, `header-border`, `header-text` ❌ Remove (can use semantic colors)

## Implementation Plan

This migration will be done in 5 phases to minimize risk and ensure nothing breaks.

### Phase 1: Add New Theme Variables (No Breaking Changes)

**Goal:** Add new semantic variables alongside existing ones.

**Actions:**
1. In `resources/public/app.css`, add new `@theme` section above existing variables:

```css
@import "tailwindcss";

@theme {
  /* ==========================================================================
     NEW SEMANTIC COLOR SYSTEM
     ========================================================================== */

  /* Brand Colors */
  --color-brand-primary: #D95640;
  --color-brand-primary-hover: #CA4833;
  --color-brand-light: #FFF8F6;

  /* Text Colors */
  --color-text-primary: #717684;
  --color-text-secondary: #353B50;
  --color-text-muted: #717684;
  --color-text-placeholder: #7E8291;

  /* Surface Colors */
  --color-surface-default: #FFFFFF;
  --color-surface-subtle: #FDFDFE;
  --color-surface-muted: #F8F9FA;
  --color-surface-hover: #F3F5F9;

  /* Border Colors */
  --color-border-default: #E7E9EF;
  --color-border-hover: #DADDE7;
  --color-border-subtle: #EBECEE;
  --color-border-strong: #EBE4E2;

  /* Warning Colors */
  --color-warning-bg: #FFF4EC;

  /* ==========================================================================
     OLD SYSTEM (TO BE DEPRECATED)
     Keep these temporarily for backwards compatibility
     ========================================================================== */

  /* Existing tint-*, primary-*, etc. variables stay unchanged for now */
}
```

2. Run `make tailwind` to generate new utility classes
3. Verify new utilities are available: `text-text-primary`, `bg-surface-muted`, etc.

**Testing:**
- No visual changes should occur
- Build should succeed
- Old classes still work

**Time estimate:** 30 minutes

### Phase 2: Migrate Clojure UI Components (Breaking Changes)

**Goal:** Replace all hardcoded colors with semantic utilities.

**Migration priority (by hardcode count):**

#### 2.1 tags.clj (10+ hardcodes)

**Before:**
```clojure
:default "bg-[#EBEFF2B2] text-[#353B50] hover:bg-[#DDE1E8] hover:text-[#7E8291]"
:language "bg-[#717684] text-white hover:bg-[#5a5d68]"
```

**After:**
```clojure
:default "bg-surface-muted/70 text-text-secondary hover:bg-border-hover hover:text-text-placeholder"
:language "bg-text-primary text-white hover:bg-brand-primary-hover"
```

**Note:** `/70` is Tailwind v4 syntax for 70% opacity (replaces B2 alpha channel)

#### 2.2 landing_hero.clj (20+ hardcodes)

**Card styles - Before:**
```clojure
(def card-base-styles
  "block rounded-lg bg-gradient-to-b from-white to-[#F8F9FA] border border-[#E7E9EF]")

(def card-hover-styles
  "hover:bg-white hover:bg-none hover:border-[#DADDE7]")
```

**After:**
```clojure
(def card-base-styles
  "block rounded-lg bg-gradient-to-b from-white to-surface-muted border border-border-default")

(def card-hover-styles
  "hover:bg-white hover:bg-none hover:border-border-hover")
```

**Text colors - Before:**
```clojure
[:p {:class "text-[#353B50] text-sm leading-[22.75px]"} "Get started..."]
```

**After:**
```clojure
[:p {:class "text-text-secondary text-sm leading-[22.75px]"} "Get started..."]
```

#### 2.3 examples.clj (15+ hardcodes)

Similar patterns - replace:
- `text-[#1F1C1C]` → `text-text-primary`
- `text-[#353B50]` → `text-text-secondary`
- `border-[#E7E9EF]` → `border-border-default`
- `bg-[#F8F9FA]` → `bg-surface-muted`

#### 2.4 main_navigation.clj (10+ hardcodes)

**Search input - Before:**
```clojure
:class "border border-[#E7E9EF] ... placeholder:text-[#7E8291]"
```

**After:**
```clojure
:class "border border-border-default ... placeholder:text-text-placeholder"
```

**Navigation links - Before:**
```clojure
:class "text-[#353B50] hover:text-primary-9"
```

**After:**
```clojure
:class "text-text-secondary hover:text-brand-primary"
```

#### 2.5 layout.clj (5+ hardcodes)

Replace footer link colors:
- `text-[#353B50]` → `text-text-secondary`
- `hover:text-primary-9` → `hover:text-brand-primary`

#### 2.6 Other UI Components

Systematic search and replace in remaining files:
- `src/gitbok/ui/breadcrumb.clj`
- `src/gitbok/ui/meilisearch.clj`
- `src/gitbok/ui/right_toc.clj`
- `src/gitbok/markdown/widgets/*.clj`

**Testing after each file:**
1. Run `make tailwind` to rebuild CSS
2. Restart server
3. Visually verify the affected pages
4. Check for any class typos or missing utilities

**Time estimate:** 2-3 hours

### Phase 3: Update app.css @apply Rules (Breaking Changes)

**Goal:** Migrate CSS rules that use old theme variables to new semantic names.

**Files:** `resources/public/app.css`

#### 3.1 Typography Rules

**Before:**
```css
article ul {
  @apply ml-8 text-tint-12;
}

article p:not(li p) {
  @apply my-2;
}

article strong {
  @apply font-bold;
}

article .card strong {
  @apply group-hover:font-bold group-hover:text-tint-12;
}
```

**After:**
```css
article ul {
  @apply ml-8 text-text-primary;
}

article p:not(li p) {
  @apply mt-2;  /* Changed: single-direction margin */
}

article strong {
  @apply font-bold;
}

article .card strong {
  @apply group-hover:font-bold group-hover:text-text-primary;
}
```

#### 3.2 Table Rules

**Before:**
```css
article table {
  @apply min-w-full bg-tint-base my-6;
}

article thead {
  @apply border-b border-tint-8 bg-tint-3;
}

article th {
  @apply px-4 py-2 text-sm text-tint-12 font-medium text-left;
}

article td {
  @apply px-4 py-1.5 text-sm leading-relaxed border-r border-tint-8 last:border-r-0 text-tint-12 text-left;
}
```

**After:**
```css
article table {
  @apply min-w-full bg-surface-default mt-6;  /* Changed: single-direction margin */
}

article thead {
  @apply border-b border-border-strong bg-border-subtle;
}

article th {
  @apply px-4 py-2 text-sm text-text-primary font-medium text-left;
}

article td {
  @apply px-4 py-1.5 text-sm leading-relaxed border-r border-border-strong last:border-r-0 text-text-primary text-left;
}
```

#### 3.3 Code Block Rules

**Before:**
```css
article code:not(pre code) {
  @apply py-[1px]! px-1.5! min-w-[1.625rem] ring-1 ring-inset ring-tint-4 bg-tint-2 rounded text-[.875em] inline break-words;
}

article pre code {
  @apply text-sm leading-relaxed block px-4 py-2 border border-tint-6 bg-tint-subtle text-tint-12 rounded-lg;
}
```

**After:**
```css
article code:not(pre code) {
  @apply py-[1px]! px-1.5! min-w-[1.625rem] ring-1 ring-inset ring-border-strong bg-surface-subtle rounded text-[.875em] inline break-words;
}

article pre code {
  @apply text-sm leading-relaxed block px-4 py-2 border border-border-strong bg-surface-subtle text-text-primary rounded-lg;
}
```

#### 3.4 Details/Summary Rules

**Before:**
```css
article details {
  @apply w-full bg-tint-base border border-tint-6;
}

article summary {
  @apply px-4 pr-10 py-4 text-tint-10;
}

article summary:hover {
  @apply text-tint-12;
}

article details[open] summary {
  @apply text-tint-strong;
}
```

**After:**
```css
article details {
  @apply w-full bg-surface-default border border-border-strong;
}

article summary {
  @apply px-4 pr-10 py-4 text-text-muted;
}

article summary:hover {
  @apply text-text-primary;
}

article details[open] summary {
  @apply text-text-primary;
}
```

#### 3.5 Navigation Rules

**Before:**
```css
#navigation a.active {
  @apply text-primary-9 font-bold;
}

#navigation details a.active:not(summary) {
  @apply border-l-1 border-primary-9;
}

#toc-container a.active {
  @apply text-primary-9 font-medium;
}
```

**After:**
```css
#navigation a.active {
  @apply text-brand-primary font-bold;
}

#navigation details a.active:not(summary) {
  @apply border-l-1 border-brand-primary;
}

#toc-container a.active {
  @apply text-brand-primary font-medium;
}
```

#### 3.6 Figure/Image Rules

**Before:**
```css
article figure {
  @apply flex flex-col items-center justify-center my-8;
}

article figure figcaption {
  @apply mt-3 text-sm text-center text-neutral-9;
}
```

**After:**
```css
article figure {
  @apply flex flex-col items-center justify-center mt-8;  /* Changed: single-direction margin */
}

article figure figcaption {
  @apply mt-3 text-sm text-center text-text-muted;
}
```

**Testing:**
1. Run `make tailwind`
2. Check all article content pages
3. Verify tables, code blocks, details/summary, images all render correctly
4. Check text hierarchy (primary, secondary, muted) is visually clear

**Time estimate:** 1-2 hours

### Phase 4: Remove Old Theme Variables (Breaking Changes)

**Goal:** Clean up the theme by removing all old, unused variables.

**Actions:**

1. Mark old variables for deletion (comment them out first for safety):

```css
@theme {
  /* NEW SEMANTIC SYSTEM - Active */
  --color-brand-primary: #D95640;
  /* ... all new variables ... */

  /* ==========================================================================
     OLD SYSTEM - DEPRECATED - REMOVE AFTER VERIFICATION
     ========================================================================== */

  /* COMMENTING OUT - Remove in next step if no issues
  --color-tint-1: rgb(255, 255, 255);
  --color-tint-2: rgb(251, 249, 249);
  ... etc ...
  */
}
```

2. Run `make tailwind` and test thoroughly (1-2 days of testing)

3. If no issues, permanently delete commented-out variables

4. Remove unused palette sections:
   - Primary (except 2, 9, 10)
   - Neutral (except 9)
   - Info (all)
   - Success (all)
   - Danger (all)
   - Warning (except 2)

5. Final `@theme` block should contain **only** the 16 new semantic variables plus:
   - Font families
   - Font sizes
   - Shadows
   - Border radius
   - Z-index values

**Testing:**
- Full regression test of all pages
- Check for any missed references to old variable names
- Verify no console errors about missing utilities

**Time estimate:** 30 minutes work + 1-2 days testing

### Phase 5: Update Documentation (Non-Breaking)

**Goal:** Document the new color system for future developers.

**Actions:**

1. Update this ADR with "Implementation Completed" status and date

2. Update `migration-plan-ru.md`:
   - Mark all 7 designer changes as ✅ Complete
   - Add reference to ADR 002

3. Archive `old-theme.txt`:
   - Rename to `old-theme-archived-YYYY-MM-DD.txt`
   - Add note at top: "This file is archived. See adr/002-tailwind-theme.md for current system."

4. Create `docs/theme-guide.md` (optional):
   - Quick reference for developers
   - When to use each color
   - Examples of common patterns

**Time estimate:** 30 minutes

### Total Migration Time Estimate

- Phase 1: 30 minutes
- Phase 2: 2-3 hours
- Phase 3: 1-2 hours
- Phase 4: 30 minutes + testing
- Phase 5: 30 minutes

**Total active work: 5-7 hours**
**Plus: 1-2 days testing buffer**

## Spacing Strategy

In addition to the color system migration, we're implementing a **single-direction margin** strategy for content spacing within `article` elements.

### Problem

Current code uses bi-directional margins (`my-*`, `py-*`):

```css
article p {
  @apply my-2;  /* margin-top AND margin-bottom */
}

article figure {
  @apply my-8;  /* margin-top AND margin-bottom */
}
```

**Issues with bi-directional spacing:**
1. **Margin collapse**: Top and bottom margins can collapse in unexpected ways
2. **Hard to debug**: When spacing looks wrong, unclear which element's margin is responsible
3. **Redundant**: Last element's bottom margin is unused (goes to container edge)
4. **Less control**: Can't easily adjust spacing between specific element pairs

### Solution: Top-Only Margins

Use only `mt-*` (margin-top) for vertical spacing:

```css
article p {
  @apply mt-2;  /* Only margin-top */
}

article figure {
  @apply mt-8;  /* Only margin-top */
}

article h2 {
  @apply mt-[1.05em];  /* Only margin-top */
}
```

### Benefits

1. **Predictable**: First element has no top margin, every subsequent element pushes down from previous
2. **Debuggable**: Spacing between A and B is determined by B's `mt-*` only
3. **Efficient**: No wasted bottom margins
4. **Flexible**: Easy to add special spacing for specific pairs (e.g., `h2 + p`)

### Scope

This strategy applies to:

**Article content elements** (defined in `resources/public/app.css`):
- Paragraphs: `article p`
- Headings: `article h1`, `article h2`, etc.
- Lists: `article ul`, `article ol`, `article li`
- Tables: `article table`
- Code blocks: `article pre`
- Figures: `article figure`
- Details/Summary: `article details`

**Markdown widgets** (defined in `src/gitbok/markdown/widgets/*.clj`):
- Headers (`headers.clj`)
- Cards (`cards.clj`)
- Tabs (`tabs.clj`)
- Stepper (`stepper.clj`)
- Images (`image.clj`)
- Big links (`big_links.clj`)

**Markdown core** (`src/gitbok/markdown/core.clj`):
- Hint blocks
- Embedded videos

### Implementation

#### In app.css

**Before:**
```css
article p:not(li p) {
  @apply my-2;
}

article figure {
  @apply flex flex-col items-center justify-center my-8;
}

article table {
  @apply min-w-full bg-tint-base my-6;
}

article pre {
  @apply my-4!;
}
```

**After:**
```css
article p:not(li p) {
  @apply mt-2;
}

article figure {
  @apply flex flex-col items-center justify-center mt-8;
}

article table {
  @apply min-w-full bg-surface-default mt-6;
}

article pre {
  @apply mt-4!;
}
```

#### In Clojure Components

Most spacing in `.clj` files uses padding (`pt-*`, `pb-*`, `px-*`) or gap utilities, which is fine. We only change top-level container spacing:

**Example - headers.clj:**
```clojure
;; BEFORE
:h2 "mt-[1.05em] text-4xl font-semibold text-tint-12 pb-2 mx-auto"

;; AFTER (pb-2 is fine, it's internal padding)
:h2 "mt-[1.05em] text-4xl font-semibold text-text-primary pb-2 mx-auto"
```

**Example - cards.clj:**
```clojure
;; BEFORE
[:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-4"}]

;; AFTER (gap-6 is fine, mt-4 is good single-direction)
[:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-4"}]
```

### What NOT to Change

Do not change:
- **Padding** (`pt-*`, `pb-*`, `py-*`): Padding is for internal spacing, keep as-is
- **Gap utilities** (`gap-*`): Flexbox/grid gaps are fine
- **Horizontal margins** (`ml-*`, `mr-*`, `mx-*`): Only vertical spacing uses single-direction
- **Component-internal spacing**: Only article content uses this pattern

## Consequences

### Positive

1. **Maintainability**: Changing a color requires updating 1 variable instead of 40+ hardcodes
2. **Consistency**: Same semantic name everywhere ensures visual consistency
3. **Designer alignment**: All 7 pending color changes applied, modern cooler palette
4. **Clarity**: Semantic names are self-documenting (`text-secondary` vs `tint-11`)
5. **Reduced bundle**: 35 unused variables removed, smaller CSS output
6. **Tailwind v4 best practices**: Follows official recommendations for semantic naming
7. **Theme-ready**: Foundation for future dark mode or multi-brand support
8. **Better text readability**: New lighter text color (#717684) improves readability
9. **Spacing predictability**: Single-direction margins eliminate debugging confusion
10. **Performance**: Fewer CSS variables = faster CSS processing

### Negative

1. **Migration effort**: 5-7 hours of active work to update all files
2. **Testing required**: Must verify all pages visually after migration
3. **Breaking changes**: Cannot roll back incrementally (phases 2-4 are breaking)
4. **Learning curve**: Team must learn new semantic names
5. **Double-prefix**: `text-text-primary` is slightly redundant (trade-off for clarity)
6. **Designer dependency**: Future color changes require designer input for semantic meaning
7. **Spacing refactor risk**: Changing margin direction might reveal edge cases

### Neutral

1. **No functional changes**: Same visual result after migration (intentionally)
2. **Theme flexibility**: Could support multiple themes in future, but not immediate benefit
3. **Utility bloat**: More utilities generated (text-*, bg-*, border-* for each variable), but tree-shaking removes unused
4. **Git diff size**: Large diff for Phase 2-3, but one-time cost

### Risks and Mitigations

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| Missed hardcodes cause visual bugs | Medium | Medium | Systematic grep search for remaining `#[0-9A-F]{6}` patterns |
| Color contrast issues with new text colors | Medium | High | Designer review + WCAG contrast checking |
| Breaking production without testing | Low | Critical | Test thoroughly in dev environment, gradual rollout |
| Team confusion about new names | Medium | Low | Create quick reference guide, pair programming for first few uses |
| Margin changes break layout | Low | Medium | Visual regression testing, careful review of spacing |

## Examples

### Example 1: Card Component

**Before (with hardcodes):**
```clojure
[:div {:class "block rounded-lg bg-gradient-to-b from-white to-[#F8F9FA]
               border border-[#E7E9EF] hover:border-[#DADDE7]
               transition-all duration-300"}
  [:h3 {:class "text-lg font-medium text-[#1F1C1C] group-hover:text-primary-9"}
   "Card Title"]
  [:p {:class "text-sm text-[#353B50]"}
   "Card description text"]]
```

**After (semantic Material Design):**
```clojure
[:div {:class "block rounded-lg bg-gradient-to-b from-white to-surface-alt
               border border-outline hover:border-outline-hover
               transition-all duration-300"}
  [:h3 {:class "text-lg font-medium text-on-surface-strong group-hover:text-brand"}
   "Card Title"]
  [:p {:class "text-sm text-on-surface-strong"}
   "Card description text"]]
```

**Benefits:**
- Clear intent: `to-surface-alt` vs `to-[#F8F9FA]`
- Material Design pattern: `on-surface` for text on surfaces
- No double-prefix utilities: `text-on-surface` instead of `text-text-primary`
- Easy to update globally
- Self-documenting color purpose

### Example 2: Navigation Link

**Before:**
```clojure
[:a {:class "text-[#353B50] hover:text-primary-9 transition-colors"
     :href "/docs"}
  "Documentation"]
```

**After:**
```clojure
[:a {:class "text-on-surface-strong hover:text-brand transition-colors"
     :href "/docs"}
  "Documentation"]
```

### Example 3: Table Styling (CSS)

**Before:**
```css
article thead {
  @apply border-b border-tint-8 bg-tint-3;
}

article th {
  @apply px-4 py-2 text-sm text-tint-12 font-medium text-left;
}

article table {
  @apply min-w-full bg-tint-base my-6;
}
```

**After:**
```css
article thead {
  @apply border-b border-outline-strong bg-surface-alt;
}

article th {
  @apply px-4 py-2 text-sm text-on-surface-strong font-medium text-left;
}

article table {
  @apply min-w-full bg-surface my-6;
}
```

**Changes:**
1. Border: `tint-8` → `outline-strong` (semantic)
2. Background: `tint-3` → `surface-alt` (semantic)
3. Text: `tint-12` → `on-surface-strong` (semantic + designer change)
4. Table background: `tint-base` → `surface` (semantic)

### Example 4: Tag Component with Opacity

**Before:**
```clojure
:default "bg-[#EBEFF2B2] text-[#353B50] hover:bg-[#DDE1E8] hover:text-[#7E8291]"
```

**After:**
```clojure
:default "bg-surface-alt/70 text-on-surface-strong hover:bg-outline-hover hover:text-on-surface-placeholder"
```

**Note:** `B2` hex alpha channel (70% opacity) → `/70` Tailwind v4 opacity modifier

## References

### External Resources
- [Tailwind CSS v4 Documentation](https://tailwindcss.com/docs)
- [Tailwind CSS @theme Directive](https://tailwindcss.com/docs/theme)
- [PenguinUI Theme](https://www.penguinui.com/theme) - Material Design inspired naming pattern (on-color, outline)
- [Material Design Color System](https://m3.material.io/styles/color/system/overview) - "On-color" pattern reference
- [How to Setup Semantic Tailwind Colors - Subframe](https://www.subframe.com/blog/how-to-setup-semantic-tailwind-colors)
- [Tailwind CSS v4 Semantic Colors - Veltify](https://veltify.org/blogs/the-importance-of-semantic-names-for-tailwind-css-colors)

### Internal Resources
- `migration-plan-ru.md` - Original designer color change specification (7 changes)
- `old-theme.txt` - Complete inventory of old color system (50+ variables)
- `adr/001-search-dropdown.md` - Previous ADR for format reference

### Related Issues
- Designer color updates (7 changes pending)
- Hardcoded color cleanup (40+ instances)
- Theme simplification (35+ unused variables)

---

**Document Version:** 1.0
**Last Updated:** 2025-01-13
**Authors:** Development Team
**Reviewers:** To be assigned

# Aidbox Database Documentation - Progress Log

## Project Overview
Writing comprehensive documentation about Aidbox Database for developers and architects with basic technical knowledge and minimal Aidbox knowledge.

## Progress Tracking

### ✅ Completed Tasks

1. **Analyzed Source Code** (aidbox-sources/*.clj)
   - Reviewed `core.clj` - Search implementation with JSONB
   - Reviewed `crud.clj` - CRUD operations and history management
   - Reviewed test files - Understanding implementation behavior
   - Created `implementation.md` summary

2. **Reviewed Legacy Documentation**
   - `database.md` - Existing database overview structure
   - `database.old.md` - Database schema details
   - `architecture.md` - High-level architecture overview

### 📝 Key Findings from Analysis

#### Source Code Insights:
- **Hybrid Storage Model**: Resources stored as JSONB with metadata columns
- **History Tracking**: Separate `*_history` tables for audit trail
- **Advanced Search**: Uses PostgreSQL JSONB functions (knife_extract_text, aidbox_text_search)
- **Transaction Management**: Global sequence for version tracking
- **Performance Features**: Lateral joins, optimized queries, deduplication

#### Legacy Documentation Structure:
- Good coverage of basic concepts
- Needs more depth on implementation details
- Missing advanced features explanation
- Requires better organization and flow

### 🎯 Documentation Plan

#### Structure for database.md:

1. **Introduction**
   - What is Aidbox Database?
   - Why PostgreSQL + JSONB?
   - Key Benefits

2. **Core Concepts**
   - Resource Storage Model
   - Table Structure
   - JSONB vs Relational Trade-offs

3. **Database Schema**
   - Resource Tables
   - History Tables
   - System Tables
   - Metadata Columns

4. **CRUD Operations**
   - Create with ID generation
   - Read with versioning
   - Update with history
   - Delete (soft deletes)
   - Bulk operations

5. **Search & Query**
   - FHIR Search to SQL
   - JSONB Query Patterns
   - Text Search Implementation
   - Performance Optimization

6. **Advanced Features**
   - Transaction Management
   - History & Versioning
   - Conditional Operations
   - Access Control

7. **Performance & Optimization**
   - Indexing Strategies
   - Query Optimization
   - Monitoring & Tuning

8. **Practical Examples**
   - Common Query Patterns
   - Direct SQL Access
   - Migration Scripts

### 🚀 Next Steps

1. ✅ Research PostgreSQL JSONB best practices
2. ✅ Write the main database.md documentation
3. ✅ Add practical examples and code snippets
4. 🔄 Review and refine for clarity

### 📊 Writing Guidelines

- Use simple, concise language
- Include practical examples
- Explain technical concepts clearly
- Build from basic to advanced topics
- Focus on developer understanding

### 📚 Additional Resources Reviewed

1. **PostgreSQL JSONB Documentation** (v17)
   - Binary storage format advantages
   - Comprehensive operator and function reference
   - Performance characteristics

2. **Architecture Weekly - JSONB Storage**
   - TOAST compression for documents > 2KB
   - Storage optimization strategies
   - Hybrid column/JSONB approach benefits

3. **FHIR Storage Specification**
   - Flexibility in storage approaches
   - Versioning requirements
   - Reference integrity considerations

### ✅ Documentation Created

**database.md** - Comprehensive overview including:
- Introduction to Aidbox's PostgreSQL/JSONB approach
- Core storage architecture with hybrid model
- Detailed schema explanation
- CRUD operations with SQL examples
- Search implementation and optimization
- Performance tuning strategies
- Advanced features and best practices
- Practical examples for developers

### 🎯 Final Steps
- Review for technical accuracy
- Ensure alignment with FHIR specifications
- Validate code examples
- Polish for readability


## Iteration 2

Editor notes:
* As top structure try to use ./legacy-docs/database.md file as a reference
* General: add more descriptions to chapters - 1/2 paragraphs
* General: For top level chapters like ## CRUD Operations Explained - add 2 paragraphs of overview of what is going to be covered in the chapter
* Chapters for Updating and Deleting resource - tell about how history is managed
* Do not hallucinate about access control (Aidbox does not provide row-level secrurity)
* Explain in more words how tranactions and sql can be used to do advanced operations
* Add section about JSONB and TOASTS (advanced topic)


### TODO:

* [✅] read editor notes and apply them

### Completed Changes:

1. **Structure**: Used legacy-docs/database.md structure as reference for top-level organization
2. **Chapter Descriptions**: Added 1-2 paragraph introductions to each major section
3. **Overview Paragraphs**: Added comprehensive 2-paragraph overviews for top-level chapters
4. **History Management**: Expanded Update and Delete sections with detailed history management explanations
5. **Access Control**: Removed row-level security content (Aidbox doesn't provide this)
6. **Transactions & SQL**: Added comprehensive "Transactions and Advanced SQL Operations" section with:
   - Transaction isolation levels explanation
   - Complex multi-resource operations examples
   - Conditional operations with CTEs
   - Batch processing patterns
   - Deadlock prevention strategies
7. **JSONB & TOAST**: Added advanced "JSONB and TOAST" section covering:
   - How TOAST works with JSONB
   - Performance implications
   - Optimization strategies
   - Best practices for TOAST management




## Iteration 3

### Editor Notes from editor-notes.md:
* Add comprehensive SQL on FHIR section based on:
  - Current Aidbox docs from legacy-docs/sql-on-fhir/
  - SQL on FHIR v2 specification

### Completed:
* [✅] Updated SQL on FHIR section with:
  - Comprehensive overview explaining the purpose and benefits
  - Detailed ViewDefinition examples with FHIRPath expressions
  - forEach functionality demonstration
  - Multi-resource analytics example (COVID-19 analysis)
  - Materialization options (view, materialized-view, table)
  - Best practices for SQL on FHIR
  - BI tool integration guidance
  - Real-world examples from legacy documentation

The section now provides a complete guide to using SQL on FHIR in Aidbox, from basic concepts to advanced analytics patterns.

## New Documentation Rewrite - Starting Fresh

### 2025-07-08 - Beginning new documentation

**Approach:**
- Writing book-like database overview following Refactoring UI style guide
- Each section as separate file, then merge into database.md
- Focus on developer experience and practical examples

**Progress:**

1. **"How data are stored in Aidbox?" section** ✅
   - Created `how-data-stored.md`
   - Problem-focused introduction
   - Clear PostgreSQL + JSONB explanation
   - Two-table architecture
   - Resource row anatomy with examples
   - Design benefits and rationale
   - Direct SQL examples
   - Submitted for editor review
   - **Updated based on feedback:**
     - Made introduction less conversational, more direct
     - Added PostgreSQL documentation links
     - Removed TOAST section (moved to performance)
     - Specified PostgreSQL clients (psql, pgAdmin, etc.)
     - Changed "Related Resources" to "See also:"

**Style decisions made:**
- More professional, less conversational tone per feedback
- Updated styleguide.md to reflect this change
- Concrete examples over abstract theory
- Short paragraphs and sections
- Real SQL queries and table examples
- Explanations of "why" not just "what"
- Added PostgreSQL documentation links where features mentioned

2. **"How data inserted, updated and deleted?" section** ✅
   - Created `crud-operations.md`
   - Problem-focused introduction with FHIR HTTP API reference
   - Complete CRUD operations with SQL examples
   - History management (removed audit terminology)
   - Transactions and batch operations with SERIALIZABLE isolation
   - FHIR transaction bundle examples
   - Bulk operations: API vs SQL distinction
   - Migration patterns for schema evolution
   - Direct SQL guidelines and performance considerations
   - Added PostgreSQL documentation links
   - **Updated based on feedback (Round 1):**
     - Added merge directive for database.md
     - Referenced FHIR HTTP API specification
     - Added transaction isolation level details
     - Included FHIR transaction bundle example
     - Enhanced bulk operations with API documentation
     - Removed "**Something**: text" formatting
     - Added CRUD internals reference
   - **Updated based on feedback (Round 2):**
     - Enhanced FHIR transaction bundle explanation with processing steps
     - Removed redundant Multi-Resource SQL Transactions section
     - Improved conditional operations explanation with FHIR context
     - Added proper links to bulk API and transaction documentation
     - Explained SQL as implementation of bulk APIs
     - Removed migrations and direct SQL guidelines sections
     - Simplified performance considerations

3. **"How to query data?" section** ✅
   - Created `query-data.md`
   - Problem-focused introduction about healthcare data analysis
   - Three main query approaches: FHIR search, SQL, SQL on FHIR
   - FHIR Search Implementation:
     - Reference, token, and string search with SQL translations
     - PostgreSQL JSONB functions with FHIR spec links
   - Direct SQL Access:
     - $sql endpoint with REST API examples
     - JSONB query patterns
     - Aidbox SQL Functions reference
   - SQL on FHIR:
     - ViewDefinition with FHIRPath expressions
     - Materialization options and BI integration
     - Multi-resource analytics examples
   - Added PostgreSQL documentation links
   - **Updated based on feedback:**
     - Added SQL on FHIR reference in introduction
     - Added FHIR search parameter links to specification
     - Added Aidbox SQL Functions documentation reference
     - Removed complex analytics, bulk export, and performance sections
     - Simplified for overview-level documentation

4. **"Which PostgreSQL can be used with Aidbox?" section** ✅
   - Created `postgresql-requirements.md`
   - Single paragraph overview covering:
     - PostgreSQL version requirements (12+ minimum, 17/16/15 supported)
     - Deployment compatibility (cloud, self-hosted, on-premises)
     - Automatic database management features
     - AidboxDB distribution
   - Reference to full requirements documentation
   - Submitted for editor review

**FINAL DELIVERABLE:** 

**database.md** - Complete comprehensive database overview ✅
- All sections merged with proper header hierarchy (H1 title, H2 sections, H3+ subsections)
- Maintained distributed "See also" references throughout
- Preserved all PostgreSQL documentation links
- Professional overview-level documentation ready for use
- **Added per requirements #14 & #15:**
  - Enhanced page overview after H1 explaining all content covered
  - SEO description frontmatter for UI and meta tags

**Individual sections completed:**
- How data are stored in Aidbox? ✅
- How data inserted, updated and deleted? ✅  
- How to query data? ✅
- Which PostgreSQL can be used with Aidbox? ✅

**Skipped per request:**
- How to optimize performance? (skipped)

**Documentation approach:**
- Followed Refactoring UI style guide (updated for professional tone)
- Problem-focused introductions with concrete examples
- Technical accuracy with PostgreSQL links
- Overview level (not tutorial) explaining Aidbox's state
- Ready for integration into Aidbox documentation

---
*Project Completed: 2025-07-08*
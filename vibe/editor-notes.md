# Editor Notes

## 2025-07-08

### Iteration 1: "How data are stored in Aidbox?"

**Summary of changes made based on feedback:**
1. Made introduction less conversational and more professional
2. Updated styleguide.md to use more direct, professional tone
3. Fixed line about spreading resources (now: "multiple columns, or even multiple tables")
4. Added specific PostgreSQL client examples (psql, pgAdmin, DBeaver)
5. Changed "Related Resources" header to "See also:" format
6. Added PostgreSQL documentation link for JSONB
7. Removed TOAST section (will cover in performance section)

**Section completed:** `how-data-stored.md` ✅

EDITOR NOTES:

### Iteration 2: "How data inserted, updated and deleted?"

I've written the CRUD operations section in `crud-operations.md`. Please review:

**What I've covered:**
- Opening with problem statement about healthcare data audit requirements
- Two-paragraph overview of the section content
- Resource lifecycle and status transitions
- Detailed CRUD operations with SQL examples:
  - Create with ID generation and version tracking
  - Read including versioned reads
  - Update with automatic history archival
  - Delete as soft operation with audit trail
- History management:
  - Complete audit trail queries
  - Point-in-time queries
  - Change detection
- Transactions and batch operations:
  - Multi-resource transactions
  - Conditional operations with CTEs
- Bulk operations:
  - COPY for high-performance import
  - Bulk updates and deletes
- Migration patterns for schema evolution
- Direct SQL guidelines and best practices
- Performance considerations

**Technical details included:**
- PostgreSQL feature links (ACID, CTEs, COPY command)
- Global sequence for transaction ordering
- Duplicate prevention optimization
- History table structure and usage
- Transaction isolation examples

**Questions:**
1. Should I add more details about transaction isolation levels?
2. Is the bulk operations section comprehensive enough?
3. Should I include examples of FHIR-specific operations (like batch/transaction bundles)?
4. Do you want more detail on the duplicate prevention mechanism?

### Iteration 2 Summary: "How data inserted, updated and deleted?"

**Changes made based on feedback:**

1. **Added merge directive:** `<!-- Merge into: database.md -->`
2. **Updated introduction:** Added FHIR HTTP API reference and note about SQL access referring to Query section
3. **Removed audit terminology:** Changed all "audit" references to "history" throughout
4. **Added transaction isolation details:** Explained SERIALIZABLE default and x-max-isolation-level header
5. **Added FHIR transaction bundle example:** Included JSON example showing how FHIR bundles map to PostgreSQL transactions
6. **Enhanced bulk operations section:** 
   - Added Aidbox Bulk API overview with specific operations
   - Clarified that SQL bulk operations have no API equivalent
   - Added reference to bulk API documentation
7. **Removed "**Something**: text" formatting** throughout the document
8. **Added CRUD internals reference:** Note that examples are simplified
9. **Updated "See also" section:** Added references to CRUD internals, bulk API, and batch transaction docs
10. **Removed duplicate prevention mention** per feedback

**Section completed:** `crud-operations.md` ✅

### Iteration 2b Summary: "How data inserted, updated and deleted?" - Second Round

**Changes made based on second round of feedback:**

1. **Enhanced FHIR Transaction Bundle explanation:**
   - Added step-by-step explanation of how Aidbox processes bundles
   - Included reference resolution example with `urn:uuid:` references
   - Explained the relationship to PostgreSQL transactions

2. **Removed Multi-Resource SQL Transactions section** - was redundant

3. **Improved Conditional Operations section:**
   - Explained what conditional operations are and why they're needed
   - Added FHIR HTTP API reference
   - Explained Aidbox-specific implementation differences
   - Added link to conditional create documentation
   - Improved SQL example with more realistic use case

4. **Enhanced Bulk Operations section:**
   - Added proper links to bulk API documentation and transaction bundles
   - Explained that SQL bulk operations are the implementation of Aidbox bulk APIs
   - Clarified the relationship between API and SQL operations

5. **Removed Migrations section** - not needed in overview

6. **Removed Direct SQL Guidelines section** - simplified for overview

7. **Simplified Performance Considerations** - removed bullet formatting per style guide

**Section completed:** `crud-operations.md` ✅

### Iteration 2c Summary: "How data inserted, updated and deleted?" - Third Round

**Changes made based on third round of feedback:**

1. **Distributed "See also" links throughout sections:**
   - Added Database Schema reference after Resource Lifecycle section
   - Added Batch Transaction reference after FHIR Transaction Bundles section
   - Added Bulk API reference after SQL Bulk Operations section
   - Removed centralized "See also" section at end of file

**Section completed:** `crud-operations.md` ✅

EDITOR NOTES:

### Iteration 3: "How to query data?"

I've written the query data section in `query-data.md`. Please review:

**What I've covered:**
- Problem-focused introduction about healthcare data analysis requirements
- Two-paragraph overview of the three query approaches
- FHIR Search Implementation:
  - Reference, token, and string search examples with SQL translations
  - PostgreSQL JSONB functions and operators used
  - Performance optimization and _explain parameter
- Direct SQL Access:
  - $sql endpoint usage with examples
  - JSONB query patterns and operators
  - Complex analytics with multi-resource joins
  - Bulk data export with $dump-sql
- SQL on FHIR:
  - ViewDefinition basics with FHIRPath expressions
  - Multi-resource analytics examples
  - Materialization options (view, materialized view, table)
  - Business intelligence integration
- Query Performance Optimization:
  - Indexing strategies for JSONB
  - Efficient query patterns
  - Performance monitoring tools

**Technical details included:**
- PostgreSQL documentation links for JSONB functions
- Real SQL examples for each search type
- Complete ViewDefinition example with forEach
- Performance comparison of different query approaches
- References to relevant Aidbox documentation

**Questions:**
1. Should I add more details about specific FHIR search parameters?
2. Is the SQL on FHIR section comprehensive enough for analytics use cases?
3. Should I include more performance tuning examples?
4. Do you want examples of specific BI tool integrations?

### Iteration 3b Summary: "How to query data?" - Second Round

**Changes made based on feedback:**

1. **Added SQL on FHIR reference** in introduction text
2. **Added FHIR search parameter references:**
   - Reference searches link to FHIR spec
   - Token searches link to FHIR spec  
   - String searches link to FHIR spec
3. **Added Aidbox SQL Functions reference** after JSONB Query Patterns section
4. **Removed sections per feedback:**
   - Complex Analytics section
   - Bulk Data Export section
   - Query Performance Optimization section
   - Query Patterns subsection
   - Monitoring Query Performance subsection

**Section completed:** `query-data.md` ✅

EDITOR NOTES:

### Iteration 4: "Which PostgreSQL can be used with Aidbox?"

I've written a concise PostgreSQL requirements section in `postgresql-requirements.md`:

**What I've covered:**
- Single paragraph overview covering:
  - Minimum PostgreSQL version requirement (12+) with JSON path support rationale
  - Currently supported versions (17, 16, 15)
  - Deployment compatibility (cloud, self-hosted, on-premises)
  - Automatic database management features
  - AidboxDB distribution mention
- Reference to full requirements documentation

**Section completed:** `postgresql-requirements.md` ✅

### Final: All sections merged into database.md

I've successfully merged all completed sections into a comprehensive `database.md` file:

**Structure:**
1. **Database Overview** - Brief introduction (H1 title)
2. **How data are stored in Aidbox?** (H2)
   - The PostgreSQL + JSONB Approach (H3)
   - Two Tables for Every Resource (H3)
   - The Anatomy of a Resource Row (H3)
   - Why This Design Wins (H3)
   - Direct SQL Access (H3)
3. **How data inserted, updated and deleted?** (H2)
   - CRUD Operations with Built-in History (H3)
   - History Management (H3)
   - Transactions and Batch Operations (H3)
   - Bulk Operations (H3)
4. **How to query data?** (H2)
   - FHIR Search Implementation (H3)
   - Direct SQL Access (H3)
   - SQL on FHIR (H3)
5. **Which PostgreSQL can be used with Aidbox?** (H2)

**Key formatting:**
- Single H1 header for title
- All major sections start with H2
- Subsections use H3 and H4 as appropriate
- Maintained all "See also" references distributed throughout sections
- Preserved all PostgreSQL documentation links
- Kept merge directive comments removed from final version

**Documentation completed:** `database.md` ✅

EDITOR NOTES:

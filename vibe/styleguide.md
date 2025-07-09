# Refactoring UI Writing Style Guide

A comprehensive guide to writing technical documentation in the style of Steve Schoger and Adam Wathan's Refactoring UI.

## Core Writing Principles

### 1. Start with a Problem, Not Theory
Always lead with the problem or pain point before introducing the solution. Present the challenge clearly and directly.

**Instead of:** "Visual hierarchy is the arrangement of elements in order of importance..."

**Write:** "Complex data models stored in relational databases require extensive normalization and multiple joins, making queries slow and maintenance difficult."

### 2. Be Clear and Professional
Write in a direct, professional tone that respects the reader's expertise. Avoid overly casual language while maintaining clarity.

**Good phrases:**
- "This approach provides..."
- "The implementation uses..."
- "This design enables..."
- "Consider the following scenario..."

### 3. Use Concrete Examples Over Abstract Concepts
Always ground concepts in real-world scenarios that readers can visualize.

**Instead of:** "Implement proper spacing in your interface."

**Write:** "For example, say you're building a flight booking service. You could start with a feature like 'searching for a flight'."

## Structural Patterns

### 1. The Problem-Solution Arc
Each section follows this structure:
1. **Present the problem** (with a relatable scenario)
2. **Explain why it happens** (brief context)
3. **Show the solution** (practical steps)
4. **Provide specific examples**

### 2. Use Short, Punchy Sections
- Keep paragraphs to 2-3 sentences
- Use frequent subheadings
- Break up text with visual examples
- One main idea per section

### 3. Progressive Disclosure
Start simple, then add complexity:
- First show the basic principle
- Then introduce edge cases
- Finally, discuss advanced techniques

## Language and Tone

### 1. Action-Oriented Headlines
Use imperative mood that tells readers exactly what to do:
- "Start with too much white space"
- "Don't use grey text on colored backgrounds"
- "Limit your choices"
- "Choose a personality"

### 2. Strategic Use of Emphasis
- **Bold** for introducing new concepts or key takeaways
- *Italics* sparingly for subtle emphasis
- • Bullet points for actionable lists
- Avoid excessive formatting

### 3. Clear Technical Concepts
Use precise technical language that accurately describes concepts:
- "Schema-less storage for evolving data models"
- "Binary JSON for efficient querying"
- "Normalized metadata with document storage"
- "Direct SQL access to FHIR resources"

## Technical Explanations

### 1. Show, Don't Just Tell
When explaining a concept:
1. State the principle briefly
2. Show a bad example
3. Show a good example
4. Explain the difference

### 2. Use Specific Numbers
Be precise with measurements and values:
- "16px is a great number to start with..."
- "jumping from 12px to 16px is an increase of 33%"
- "you want 8-10 shades to choose from"

### 3. Explain the "Why"
Always connect technical decisions to user experience:

**Instead of:** "Use HSL color format."

**Write:** "Hex and RGB are the most common formats for representing color on the web, but they're not the most useful. HSL fixes this by representing colors using attributes the human-eye intuitively perceives..."

## Common Phrases and Transitions

### Opening Phrases
- "This section explains..."
- "The following approach..."
- "To implement this feature..."
- "This design addresses..."
- "The limitation of this approach is..."

### Transitional Phrases
- "So how do you actually..."
- "But what if..."
- "This works great, but..."
- "Similarly..."
- "On top of that..."

### Concluding Phrases
- "The key benefit is..."
- "This approach provides..."
- "Best practice recommends..."
- "Performance testing confirms..."

## Writing Don'ts

### 1. Avoid Academic Language
- No passive voice when active works better
- No unnecessarily complex vocabulary
- No lengthy theoretical explanations

### 2. Don't Over-Explain
- Trust the reader's intelligence
- Let examples do the heavy lifting
- Keep explanations concise

### 3. Avoid Absolutism
Use qualifying language appropriately:
- "often" instead of "always"
- "usually" instead of "must"
- "might want to" instead of "should"

## Practical Examples Structure

When providing examples, follow this format:

1. **Set the context**: "Say you're building a comment system..."
2. **Describe the feature**: "You know that one day, you'd like users to be able to..."
3. **Present the problem**: "You get deep into implementation only to discover..."
4. **Provide the insight**: "The thing is, a comment system with no attachments would still have been better than..."
5. **Give the takeaway**: "Design the smallest useful version you can ship..."

## Quick Reference Checklist

Before publishing, ensure your documentation:

- [ ] Starts with a relatable problem
- [ ] Uses conversational tone with contractions
- [ ] Includes specific, real-world examples
- [ ] Breaks complex ideas into digestible chunks
- [ ] Uses imperative headlines
- [ ] Provides concrete numbers and measurements
- [ ] Explains the "why" behind recommendations
- [ ] Avoids academic or overly technical language
- [ ] Includes memorable, quotable phrases
- [ ] Ends sections with clear takeaways

## Sample Documentation Template

```markdown
## [Imperative Action Headline]

[Opening with a problem/question that hooks the reader]

[Brief explanation of why this matters, 1-2 sentences]

### [Specific Technique or Solution]

[Concrete example with real scenario]

[Show the wrong way with explanation]

[Show the right way with benefits]

[Additional context or edge cases if needed]

[Memorable closing statement or rule of thumb]
```

Remember: The goal is to make complex design decisions feel approachable and actionable. Write documentation that designers and developers actually want to read.

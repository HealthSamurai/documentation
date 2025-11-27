---
title: "Agentic FHIR: A New Way to Build Implementation Guides"
slug: "agentic-fhir-implementation-guide-development"
published: "2025-08-27"
author: "Aleksandr Kislitsyn"
reading-time: "7 minutes"
tags: []
category: "FHIR"
teaser: "Discover how AI and Aidbox make building FHIR Implementation Guides faster, easier, and accessible to both developers and healthcare experts."
image: "cover.png"
---

FHIR Implementation Guides (IGs) are comprehensive [specifications](https://build.fhir.org/implementationguide.html) that explain how to use FHIR for specific healthcare needs — from national patient profiles to specialized clinical workflows. They define which resources to use, what codes to include, and how systems should behave so that data can move smoothly between organizations.

Today, developing an IG is still a highly technical task. It requires creating `CodeSystems`, `ValueSets`, `StructureDefinitions`, and `Extensions`, then publishing them as a cohesive, validated specification. The process is complex and often slows down projects.

This article looks at a new approach: using AI agents to generate and test FHIR Implementation Guides directly from plain language. The goal is to make guide development faster, easier, and more accessible — not only for technical specialists, but also for clinicians, researchers, and policy makers who understand the healthcare context but don’t want to write JSON code.

## **The FHIR IG Development Pain Points**

Anyone who's worked with FHIR knows the struggle. Creating canonical resources — the building blocks of healthcare interoperability — is complex. JSON files are verbose, the constraints are intricate, and the learning curve is steep enough to make experienced developers question their career choices.

Because of this complexity, the community created tools to simplify the process. One of them is [FHIR Shorthand (FSH)](https://build.fhir.org/ig/HL7/fhir-shorthand/reference.html)a more compact and readable language compared to raw JSON. Still it requires developers to learn yet another syntax and understand the nuances of FHIR's underlying architecture.

**The Traditional IG Publishing Pipeline**

Even with FSH, building an IG is still a complex and error-prone process. The typical workflow looks like this:

1. **Write FSH files** - Define resources in FHIR Shorthand syntax
2. **Run SUSHI** - Compile FSH files into FHIR JSON resources
3. **Fix compilation errors** - Debug syntax issues and constraint violations
4. [**Run IG Publisher**](https://confluence.hl7.org/spaces/FHIR/pages/35718627/IG+Publisher+Documentation) - Generate HTML documentation and perform final validation
5. **Iterate on errors** - Address validation failures and republish
6. **Manual testing** - Verify resources work in target systems  
   

This multi-step pipeline requires deep knowledge of multiple tools: FSH syntax, SUSHI compilation, IG Publisher configuration, and FHIR validation rules. The result is a process that creates barriers that exclude healthcare domain experts from direct participation.

So, what alternatives are there to this conventional, tool-heavy approach? Recent advances in AI may offer a way to rethink how IGs are developed and tested.

## **The AI-Powered Solution**

What if we could eliminate this translation layer? Imagine healthcare experts describing their requirements in plain English and having AI generate production-ready FHIR resources automatically.

Recent advances in Large Language Models (LLMs) make it possible. Modern AI can:

- **Understand domain context**: Read natural language descriptions of healthcare requirements
- **Generate structured output**: Create valid FHIR JSON directly from specifications
- **Apply best practices**: Ensure generated resources follow FHIR conventions and implementation guidelines
- **Iterate rapidly**: Allow quick refinements based on feedback without manual JSON editing

The key insight is that AI can bridge the gap between human intent and technical implementation — exactly what we need in FHIR Implementation Guide development. This means reducing the technical overhead and letting both developers and healthcare experts contribute directly.

## **Closing the Loop: Instant Validation with Aidbox**

Generating FHIR resources is only half the battle. The real magic happens when AI agents can immediately test their creations against a live FHIR server, creating a rapid feedback loop that enables continuous refinement.

Aidbox FHIR server makes this possible by providing:

- **Instant resource validation**: AI-generated resources are immediately validated against FHIR specifications
- **Real-time feedback**: Validation errors are caught instantly, allowing the AI agent to correct issues right away
- **A live testing environment**: Resources can be created, updated, and tested in real time without waiting for deployment
- **Standards compliance**: Ensures all resources meet FHIR R4+ requirements before being finalized

This creates a powerful development cycle:

1. **AI generates** FHIR resources from natural language
2. **Aidbox validates** the resources instantly via REST API
3. **AI refines** based on validation feedback
4. **Process repeats** until perfect compliance is achieved

The result is sub-minute feedback, faster development, while ensuring clinical accuracy and technical compliance.

## **Example IG Development Project:** [**Github**](https://github.com/Aidbox/examples/tree/main/developer-experience/agentic-coding-ig-development)

To see how this works in practice, let’s look at an example project.

### **Collaborative AI-Human Development Workflow**

Initial AI Prompt:

> Create an MD file for a CodeSystem that includes biological sex codes: 1 for Male, 2 for Female, 3 for Intersex, 93 for 'Not reported' when Civil Registry updates occur, and 99 for Unknown cases.

The AI generates a structured markdown file like[src/CS/CSSexoBiologico.MD](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/src/CS/CSSexoBiologico.MD), and you iterate on it until it looks right. Once you're satisfied with the markdown specification, you simply ask the AI to "create a FHIR resource for the CSSexoBiologico code system" and it converts it to FHIR JSON resource in the target/ folder: [target/CSSexoBiologico.json.](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/target/CSSexoBiologico.json)

### **Instant Testing with Aidbox Integration**

Here’s where validation comes in. The AI can test the generated resources right away using Claude Code's [commands](https://docs.anthropic.com/en/docs/claude-code/slash-commands) and .http files.

Using Claude Code, you can simply run:

```javascript
/test-cs CSSexoBiologico
```

This triggers the custom Claude command defined in[.claude/commands/test-cs.md](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/.claude/commands/test-cs.md), which:

1. Examines the CodeSystem structure from `src/CS/CSSexoBiologico.MD`
2. Validates FHIR compliance against the generated JSON in `target/CSSexoBiologico.json`
3. Starts the Aidbox FHIR server if needed (`docker-compose up -d`)
4. Creates or runs comprehensive test suites in test/CS/
5. Reports validation results and recommendations

Generated Test File([test/CS/test-CSSexoBiologico.http](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/test/CS/test-CSSexoBiologico.http)):

```json
@fhirServer = http://localhost:8080/fhir
@auth = Basic basic:secret

### Create CSSexoBiologico CodeSystem
PUT {{fhirServer}}/CodeSystem/CSSexoBiologico
Authorization: {{auth}}
Content-Type: application/json

< ../../target/CSSexoBiologico.json

### Test $lookup operation for code "1" (Male)
GET {{fhirServer}}/CodeSystem/$lookup?system=https://interoperability.testcompany.cl/CodeSystem/CSSexoBiologico&code=1
Authorization: {{auth}}

### Test $lookup operation for code "2" (Female)
GET {{fhirServer}}/CodeSystem/$lookup?system=https://interoperability.testcompany.cl/CodeSystem/CSSexoBiologico&code=2
Authorization: {{auth}}

... Other tests ...

### Test $lookup operation for invalid code "999"
GET {{fhirServer}}/CodeSystem/$lookup?system=https://interoperability.testcompany.cl/CodeSystem/CSSexoBiologico&code=999
Authorization: {{auth}}

### Verify CodeSystem was created
GET {{fhirServer}}/CodeSystem/CSSexoBiologico
Authorization: {{auth}}

### Delete CSSexoBiologico CodeSystem
DELETE {{fhirServer}}/CodeSystem/CSSexoBiologico
Authorization: {{auth}}
```

This setup allows developers and domain experts to quickly validate resources, see how they behave on a live server, and refine them without leaving the workflow.

### **Scaling to a Full Implementation Guide**

The same AI-powered approach can be applied to all FHIR resource types needed for a complete Implementation Guide:

- **ValueSets**: AI generates comprehensive terminology sets and validates them using $expand and $validate-code operations to test code inclusion and exclusion rules ( see[test-vs command](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/.claude/commands/test-vs.md) | [test file](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/test/VS/test-VSIdentidadGenero.http)**).**
- **Profiles**: AI creates constrained patient resources with extensions, then validates all profile constraints, including cardinality, data types, and terminology bindings ( see [test-profile command](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/.claude/commands/test-profile.md) | [test file](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/test/Profile/test-PacienteCl.http)).
- **Extensions**: AI defines reusable data elements with proper binding strengths and validates their integration into base resources.

Each resource type follows the same pattern: describe requirements in natural language → AI generates markdown → convert to FHIR JSON → instant validation in Aidbox.

This makes it possible to scale from a single `CodeSystem` to a complete, production-ready Implementation Guide.

### **Publishing the Implementation Guide**

Once all resources are validated, the final step is publishing the complete guide. This usually involves:

- [publish-ig command](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/.claude/commands/publish-ig.md) – Generates the HTML documentation
- [view-ig command](https://github.com/Aidbox/examples/blob/main/developer-experience/agentic-coding-ig-development/.claude/commands/view-ig.md) – Preview the published IG locally

Together, these steps produce a full set of documentation and resources, ready for use and distribution.

### **The Complete AI-Powered Workflow**

At this point, the entire cycle comes together:

1. **AI generates** markdown documentation from natural language
2. **AI converts** markdown to valid FHIR JSON resources
3. **AI creates** comprehensive **test suites** for all resource types
4. **Aidbox validates** resources in real-time via REST API
5. **AI refines** based on validation feedback
6. **IG Publisher generates** final documentation

### **Caveats and the Road Ahead**

AI-supported IG development is still an emerging practice. While it can already reduce repetitive work and shorten feedback loops, there are areas that require careful attention:

- Clinical accuracy – AI can generate technically valid resources, but human experts must ensure they reflect the correct clinical and policy requirements.
- Complex constraints – not all advanced rules or invariants are easy to capture from plain language prompts. Some still require expert input.
- Community alignment – an Implementation Guide is only valuable when trusted and adopted by its community. AI can help create IGs faster, but consensus-building remains a human task.

Looking ahead, we see a future where healthcare experts and developers work side by side with AI agents: experts describe the intent, AI produces the first draft, and both refine the result together. This would make Implementation Guide development more inclusive, faster, and closer to real-world needs.

### **Want to see this in practice?**

All the source code is available in the[agentic-coding-ig-development](https://github.com/Aidbox/examples/tree/main/developer-experience/agentic-coding-ig-development) repository. You can explore the workflows, run the commands, and test how AI-driven IG development works step by step.

We believe this approach is only the beginning. By combining domain expertise, modern AI, and tools like Aidbox, Implementation Guides can be developed faster, with less overhead, and with broader participation from the healthcare community.

Try it yourself with [Aidbox](https://health-samurai.webflow.io/fhir-server): spin up a FHIR server, run the validation cycle, and see how AI can generate and test resources in minutes. Get started today and share your experience — your feedback will help shape the future of AI-powered FHIR development.

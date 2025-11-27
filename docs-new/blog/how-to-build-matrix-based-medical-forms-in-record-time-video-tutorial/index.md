---
title: "How to build matrix-based medical forms in record time: Video Tutorial"
slug: "how-to-build-matrix-based-medical-forms-in-record-time-video-tutorial"
published: "2024-07-19"
author: "Maria Ryzhikova"
reading-time: "5 min read"
tags: ["medical forms", "form builder", "fhir", "sdc"]
category: "System Design"
teaser: "Learn how to create efficient, user-friendly matrix forms using the Aidbox Public Form Builder. See real-world examples like PHQ-2/PHQ-9 and GAD-7 in action."
image: "cover.png"
---

Matrix-based medical forms are gaining popularity in healthcare for their ability to improve data collection and streamline processes. If you're an engineer in healthcare IT or medical informatics, it's essential to understand how to design and implement these forms. Mastering this can optimize clinical workflows and enhance patient care.

In this blog post, Health Samurai's product manager [Maria Ryzhikova](https://www.linkedin.com/in/maria-ryzhikova-55983079/) will show you how to create efficient, user-friendly matrix-style medical forms using the [Aidbox Public Form Builder](https://hubs.li/Q02Hf4wG0). She'll use examples like the PHQ-2/PHQ-9 and GAD-7 to demonstrate how these forms can streamline depression and anxiety screening processes in clinical settings.

The post will cover:

1. How to combine PHQ-2 and PHQ-9 into a dynamic matrix form using the "enable-when" rule.
2. Implementing automatic score calculations and interpretations.
3. Using the matrix choice widget for intuitive display and reduced cognitive load.
4. Customizing forms for both clinician use and patient self-assessment.
5. Applying similar principles to create an efficient GAD-7 form.

## Enter the Matrix (Forms)

Digital forms are designed to:

- Speed up and simplify data collection
- Perform smart calculations, interpret data, and gather additional information
- Be user-friendly, reducing the cognitive load on clinicians

But the real magic lies in a creative process! It’s not just about the tools but also the decisions made by the form designer.

Today, we're excited to introduce you to a type of [medical form that works](https://www.health-samurai.io/forms-product) best in a table format: matrix forms. These forms are usually a set of questions with the same set of answers. Most often, such forms include calculation of the total score.

You could present them as separate drop-down fields but grouping them into a matrix makes the form more intuitive, reduces space, and saves time. This decreases cognitive load, helping doctors or patients quickly find the right answers because the list of answers is in front of your eyes.

If a doctor fills out the form, we can display a field that automatically calculates the total score. After completing the form, the doctor can see the score interpretation based on the range, giving them all the necessary information at a glance.

We’ve created two forms, PHQ-2/9 and GAD-7, that are available in our [Public Form Gallery](https://hubs.li/Q02Hf4wG0).

## What are PHQ-2 and PHQ-9?

The **PHQ-2** is a brief screening tool that assesses the frequency of depressed mood and lack of interest over the past two weeks. It consists of the first two questions from the longer PHQ-9 form. If a patient scores higher than 2 on the PHQ-2, it is recommended to proceed with the full **PHQ-9** or a clinical evaluation. The **PHQ-9** is a more comprehensive tool that includes nine questions based on DSM-IV criteria for depression. It helps in diagnosing and monitoring the severity of depression.

### Combining PHQ-2 and PHQ-9 into a Dynamic Form

To enhance efficiency, we have combined the PHQ-2 and PHQ-9 into one dynamic form using matrix design. This approach leverages the "enable-when" rule, which makes additional fields appear based on the PHQ-2 score. If the PHQ-2 score is higher than 2, the form dynamically reveals the additional seven questions from the PHQ-9.

### Key Features of the Dynamic Matrix Form

1. **Automatic Calculations**: Each answer has a score, which is automatically calculated and displayed to the clinician. Tooltips explain the calculation formula, ensuring transparency.
2. **Score Interpretation**: The total PHQ-9 score is calculated using a predefined expression and shown in a read-only mode. The interpretation of the score is displayed based on the range, helping clinicians make informed decisions quickly.
3. **Matrix Choice Widget**: This widget allows the form to be displayed in a table format, making it more intuitive and space-efficient. It reduces cognitive load and helps users find answers quickly.
4. **Patient Self-Assessment**: The total score and interpretation fields can be hidden for patient self-assessment, ensuring privacy and reducing anxiety.

The PHQ-9 total score falls within

- 1-4 then - Minimal depression
- 5-9 then - Mild depression
- 10-14 then - Moderate depression
- 15-19 then - Moderately severe depression
- 20-27 then - Severe depression

When the form is sent to the patient to fill out for self-assessment, then the scores and interpretation fields can be hidden.

****

## What is the GAD-7?

The **GAD-7** (Generalized Anxiety Disorder-7) is a brief measure that helps in diagnosing and assessing the severity of generalized anxiety disorder. It is a self-administered [questionnaire that patients can complete quickly](https://www.health-samurai.io/forms-product), making it highly practical for both clinical and research settings.

### Key Features of the GAD-7 Form

1. **Seven Targeted Questions**: The GAD-7 includes seven questions that cover key anxiety symptoms such as feeling nervous, being unable to stop worrying, and having trouble relaxing. Each question is rated based on the frequency of symptoms over the past two weeks.
2. **Scoring System**: The responses are scored on a scale from 0 to 3:
   - 0: Not at all
   - 1: Several days
   - 2: More than half the days
   - 3: Nearly every dayThe total score ranges from 0 to 21, with higher scores indicating greater anxiety severity.
3. **Automatic Score Calculation**: Using a matrix choice widget, the GAD-7 form can automatically calculate the total score. This feature simplifies the process for clinicians, ensuring quick and accurate assessments.
4. **Score Interpretation**: The GAD-7 scores are interpreted as follows:
   - 0-4: Minimal anxiety
   - 5-9: Mild anxiety
   - 10-14: Moderate anxiety
   - 15-21: Severe anxietyThese interpretations help clinicians determine the appropriate level of care or further evaluation needed.
5. **User-Friendly Design**: The matrix format of the GAD-7 makes it easy to use and understand. It reduces the cognitive load on patients, allowing them to complete the form efficiently.

## **Practical Applications**

So there you have it! Digital matrix forms are changing the game in healthcare data collection. They're fast, smart, and user-friendly - everything we need to keep pushing healthcare tech forward.

> Have we met the expectations from digital forms? [Watch the video](https://youtu.be/Dhdg6oGmyew?feature=shared) to see both forms in action and use our [Public Form Builder](https://hubs.li/Q02Hf4wG0) to customize and share these forms.

Creating matrix-based medical forms can drastically enhance the efficiency and accuracy of data collection in healthcare settings. By leveraging structured formats, these forms facilitate quick data entry and easy retrieval, ensuring that healthcare professionals have access to critical information when they need it most.

To experiment with building matrix-based medical forms yourself, try the [free version of Aidbox](https://www.health-samurai.io/aidbox#run). It offers all the necessary tools without any feature limitations, providing a perfect platform for development and testing.

*Author*:   
[**Maria Ryzhikova**](https://www.linkedin.com/in/maria-ryzhikova-55983079/),   
Product Manager at Health Samurai

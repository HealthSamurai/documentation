---
title: "Moving Cardiovascular Disease Detection to the Cloud "
slug: "how-heartsmart-is-virtualizing-cardiovascular-disease-detection"
published: "2018-02-08"
author: "Anthony J. Chan"
reading-time: "6 min read"
tags: []
category: "System Design"
teaser: "We helped HeartSmart develop a machine learning platform that helps with cardiovascular disease detection by processing clinical data in the cloud. This post explains how the new healthcare technology is changing provider workflows, lowering costs, and improving care delivery efficiency. "
image: "cover.jpg"
---

*Published on March 3, 2018 by Anthony J. Chan*

We spoke with [HeartSmart](https://heartsmartnow.com/) CEO [Michael McCann](https://www.linkedin.com/in/michael-mccann-62b86669/) to learn how his company is virtualizing cardiovascular disease detection.

**The Tell-Tale Heart**

Undetected cardiovascular disease is the greatest health risk Americans face today, as heart disease accounts for [25% of deaths](https://www.cdc.gov/heartdisease/facts.htm) in the U.S. And for most people, the first symptom of heart disease comes in the form of a heart attack or stroke. Strokes and heart disease have catastrophic effects on both the patient and the healthcare system, costing the U.S. nearly [$1 billion a day in medical costs and lost productivity](https://www.cdcfoundation.org/pr/2015/heart-disease-and-stroke-cost-america-nearly-1-billion-day-medical-costs-lost-productivity).

Luckily, when signs of heart disease are detected early, patients can take action to prevent heart attacks and strokes from developing. Early detection followed by intervention efforts upstream can prevent downstream disease development and help control healthcare costs and outcomes.

Traditional [cardiovascular disease diagnosis](https://www.mayoclinic.org/diseases-conditions/coronary-artery-disease/diagnosis-treatment/drc-20350619) is both time-consuming and capital and labor intensive as it requires large ultrasound machines and skilled technicians to operate them. But using expensive scanning equipment with a team of skilled staff was the only way carotid scans ultrasounds could be performed.

That was until the [HeartSmart](https://heartsmartnow.com/) platform came along.

HeartSmart developed a full stack platform that allows doctors to easily administer something called the [HeartSmart IMT*plus* test](https://heartsmartnow.com/about-heartsmart-imtplus/) that identifies early signals of cardiovascular diseases, which leads to preventative and sometimes life-saving surgical interventions.

**The HeartSmart IMT*plus* Test**

Most practitioners rely on software embedded in ultrasound machines to do the processing of carotid scans. The HeartSmart IMT*plus* Test disrupts this workflow by providing a proprietary software that processes the scanned images in the cloud. The test enables physicians and other healthcare practitioners to easily perform ultrasound scans of the carotid arteries, and then upload the scans to the cloud for analysis and reporting by the HeartSmart lab. The HeartSmart analysis determines if there is excessive atherosclerosis or plaque in the arteries, indicating the presence of cardiovascular disease.

**Handling Imaging and Video Files**

Carotid scans are historically stored in the [DICOM](http://dicom.nema.org/Dicom/about-DICOM.html) format. These image and video files take up a lot of storage memory, so HeartSmart customized their solution to enable recorded image and video files to be uploaded to the cloud instead of being stored locally. Each doctor has a separate user account on the cloud, and it’s HIPAA compliant.

The HeartSmart solution records and virtualizes ultrasound scans in doctor office, allowing even non-technical personnel to perform the scans.

**Where did the IMT test come from?**

TheHeartSmart IMT test was developed by Dr. Jacques Barth, a cardiologist and endocrinologist by training with a Ph.D. in medical imaging. Dr. Barth was a researcher in cardiovascular disease and co-author of the first study that demonstrated that atherosclerosis could be reversed with diet and exercise (The Leiden Intervention Study published in 1985). He was also a pioneer in developing carotid IMT measurement while at the USC Keck School of Medicine and the Jet Propulsions Laboratory.

[Carotid IMT](https://www.cedars-sinai.edu/Patients/Programs-and-Services/Womens-Heart-Center/Services/Carotid-Intima-Media-Thickness-Test.aspx) came along 10 years ago in the clinical setting, and Dr. Barth was one of the first people to see the value of carotid IMT in clinical practice long before most physicians had even heard of it. He recognized that primary care physicians were in the best position to assess their patients’ cardiovascular health and provided a means that would go far beyond the assessment of conventional risk factors protect their patients from heart attacks and strokes.

**A New Way to Detect**

For decades, doctors had to buy expensive ultrasound machines for carotid scans, which costs between $14,000 to $18,000 dollars. Not only are the machines expensive, but doctors have to hire technicians to operate the machines.

HeartSmart provides a full stack solution that includes a probe and a cloud platform that enables doctors to do the ultrasound scans in their office *without specialized personnel*, as the technology is easy to use and uploads all image and video scans to the HeartSmart cloud for analysis and processing. With HeartSmart’s solution, the operating costs are cut by more than 50%, from $14,000+ to $6,900 (the price of the HearSmart bundle which includes a configured laptop, probe and access to the proprietary software).

HeartSmart also lowers labor costs as doctors won’t need ultrasound technicians to operate the equipment. The new price point lowers the barriers to entry and enables more doctors to participate in diagnostic carotid scans. HeartSmart is the only imaging modality that doctors do in office without specialized personnel. It also eliminates the need to schedule patients around the availability of a mobile ultrasound service, making it more convenient for patients. With the help of the cloud, the technology can be deployed to anywhere in the world, which is especially beneficial in places where where ultrasound imaging services are not readily available (HeartSmart currently has clients in India and in three countries in the Middle East).

**Benefits of Using the Cloud:**

- Customer management for different users (isolate info for people who need it)
- Training is far easier because physicians can access online videos from anywhere
- No need for processing or local storage of large DICOM files
- Doctors can get a bundled package which includes a configured laptop, probe and access to the proprietary cloud software that acts as a turnkey solution, upending the need for in-personal installation and setup costs
- Seamless installation, setup and training experience. The installation and setup time is cut down dramatically due to the efficiency of using the cloud
- Highly scalable because it digitizes the carotid scan process and leverages modern cloud technologies

**Who are HeartSmart’s Customers?**

HeartSmart’s target customers continue to be primary care physicians who are focused on a more integrative and preventive approach to their patients’ health. But the HeartSmart IMT test is also ideal for other healthcare practitioners such as naturopaths and chiropractors who have expanded their scope to a holistic framework to manage the overall health and wellness of their patients.

On feedback from HeartSmart customers, CEO [Michael McCann](https://www.linkedin.com/in/michael-mccann-62b86669/) says:

*“Our initial feedback from customers is that they are delighted with the system and its ease of use. The image quality from our probe is excellent and the commands are highly intuitive. They also love the HeartSmart IMT Report which makes it easy for the patients to understand the state of their cardiovascular health. Doctors also appreciate that our technology has greatly reduced the upfront cost, and this has enabled our company to accelerate customer acquisition.”*

**The Road Ahead**

HeartSmart has secured funding from a group of investors who are all involved in the healthcare space, and the company is on a mission to give all healthcare practitioners the ability to identify the presence of cardiovascular disease before their patients experience an adverse event, and to motivate patients to follow physician recommendations to prevent themselves from having a heart attack or stroke.

In the coming months, HeartSmart will undertake a program to enhance its analysis software to provide additional information not currently included in most carotid IMT testing. This will enable the company to expand in the international markets and provide potential licensing opportunities in major markets where the test is not currently available. And after the next level of development, the HeartSmart platform will begin to provide detailed quantification of plaque levels.

Please reach out to Mike at [mmccann@heartsmartnow.com](mailto:mmccann@heartsmartnow.com) with any questions.

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.

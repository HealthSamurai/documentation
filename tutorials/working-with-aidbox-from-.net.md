# Working with Aidbox from .NET

### Prepare project

1. Start VisualStudio.
2. Create project appropriate type \(for example, “Console App”\):

![](../.gitbook/assets/1.PNG)

3. Open “Project”-&gt;”Manage NuGet Packages…”:

![](../.gitbook/assets/2.PNG)

4. Enter in search “hl7.fhir.stu3”:

![](../.gitbook/assets/3.PNG)

5. Click install:

![](../.gitbook/assets/4.PNG)

6. Click “Ok”:

![](../.gitbook/assets/5.PNG)

7. Click “I Accept”.

![](../.gitbook/assets/6.PNG)

8. Your project is ready for working with aidbox.

### Sample project

In this [project](https://github.com/VyacheslavMik/AidboxDemo) we create patient in the box in aidbox via [.NET API for HL7 FHIR](https://github.com/ewoutkramer/fhir-net-api). Next we read newly created patient from the demo box. Important to remember that currently Aidbox doesn't support XML format and we need explicitly set it to JSON.

If you build and run application, you can see something like this:  


![](../.gitbook/assets/7.PNG)


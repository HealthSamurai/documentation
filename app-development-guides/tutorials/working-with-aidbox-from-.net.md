# Working with Aidbox from .NET

### Prepare the project

1. Start VisualStudio.
2. Create the project appropriate type \(for example, “Console App”\):

![](../../.gitbook/assets/1.PNG)

3. Open “Project”-&gt;”Manage NuGet Packages…”:

![](../../.gitbook/assets/2.PNG)

4. Enter in search “hl7.fhir.stu3”:

![](../../.gitbook/assets/3.PNG)

5. Click install:

![](../../.gitbook/assets/4.PNG)

6. Click “Ok”:

![](../../.gitbook/assets/5.PNG)

7. Click “I Accept”.

![](../../.gitbook/assets/6.PNG)

8. Your project is ready for working with aidbox.

### Sample project

In this [project](https://github.com/Aidbox/AidboxDemo) we create a patient in the box in aidbox via [.NET API for HL7 FHIR](https://github.com/ewoutkramer/fhir-net-api). Next we read a newly created patient from the demo box. It's important to remember that currently [Aidbox](https://www.health-samurai.io/aidbox) doesn't support XML format and we need explicitly set it to JSON.

If you build and run application, you can see something like this:  


![](../../.gitbook/assets/7.PNG)


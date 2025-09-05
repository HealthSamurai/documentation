---
description: Follow these steps to set up a Java project with Aidbox
---

# Use Aidbox with Java

In this guide we will use the [HAPI FHIR Java client library](https://github.com/hapifhir/hapi-fhir) to create a patient resource in Aidbox.

## Prerequisites

- Run Aidbox locally by following the instructions in the [Run Aidbox locally](run-aidbox-locally.md) guide
- Java
- Maven

## Steps

1. Create a Client and an Access Policy.
   
   Navigate to [REST Console](http://localhost:8080/ui/console#/rest) in Aidbox UI and execute the following requests:
   
   ```json
   POST /fhir/Client
   content-type: application/json
   accept: application/json
   
   {
     "secret": "secret",
     "grant_types": [
       "basic"
     ],
     "id": "basic",
     "resourceType": "Client"
   }
   ```
   
   ```json
   POST /fhir/AccessPolicy
   content-type: application/json
   accept: application/json
   
   {
     "link": [
       {
         "reference": "Client/basic"
       }
     ],
     "engine": "allow",
     "id": "basic-policy",
     "resourceType": "AccessPolicy"
   }
   ```

2. Create pom.xml:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
   	<modelVersion>4.0.0</modelVersion>
   
   	<groupId>example.app</groupId>
   	<artifactId>aidbox-hapi-client</artifactId>
   	<version>0.0.1-SNAPSHOT</version>
   	<name>aidbox-hapi-client</name>
   
   	<properties>
   		<hapi-fhir.version>8.2.0</hapi-fhir.version>
   	</properties>
   
   	<dependencies>
   		<dependency>
   			<groupId>ca.uhn.hapi.fhir</groupId>
   			<artifactId>hapi-fhir-structures-r4</artifactId>
   			<version>${hapi-fhir.version}</version>
   		</dependency>
   		<dependency>
   			<groupId>ca.uhn.hapi.fhir</groupId>
   			<artifactId>hapi-fhir-client</artifactId>
   			<version>${hapi-fhir.version}</version>
   		</dependency>
   	</dependencies>
   
   	<build>
   		<plugins>
   			<plugin>
   				<groupId>org.codehaus.mojo</groupId>
   				<artifactId>exec-maven-plugin</artifactId>
   				<version>3.1.0</version>
   				<configuration>
   					<mainClass>com.example.AidboxHapiClient</mainClass>
   				</configuration>
   			</plugin>
   		</plugins>
   	</build>
   </project>
   ```

3. Create **src/main/java/com/example/AidboxHapiClient.java**:
   ```
   package com.example;
   
   import ca.uhn.fhir.context.FhirContext;
   import ca.uhn.fhir.rest.client.api.IGenericClient;
   import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
   import ca.uhn.fhir.rest.api.MethodOutcome;
   import org.hl7.fhir.r4.model.*;
   
   import java.util.Date;
   
   /**
    * Example Java application that uses HAPI FHIR client to create a patient in Aidbox
    */
   public class AidboxHapiClient {
       
       public static void main(String[] args) {
           try {
               
               FhirContext ctx = FhirContext.forR4();
               
               IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/fhir");
               
               // Add basic authentication
               BasicAuthInterceptor authInterceptor = new BasicAuthInterceptor("basic", "secret");
               client.registerInterceptor(authInterceptor);
               
               // Create a simple patient
               Patient patient = new Patient();
               patient.addName().setFamily("Doe").addGiven("John");
               patient.setBirthDate(new Date(90, 0, 15)); // January 15, 1990
               patient.setGender(Enumerations.AdministrativeGender.MALE);
               
               
               System.out.println("Creating patient: " + patient.getNameFirstRep().getNameAsSingleString());
               
               // Create the patient on the server
               MethodOutcome outcome = client.create()
                       .resource(patient)
                       .execute();
               
               if (outcome.getCreated()) {
                   System.out.println("Patient created successfully!");
                   System.out.println("Patient ID: " + outcome.getId().getIdPart());
               } else {
                   System.out.println("Patient creation may have failed or was updated");
               }
               
               // Retrieve the created patient to verify
               Patient createdPatient = client.read()
                       .resource(Patient.class)
                       .withId(outcome.getId().getIdPart())
                       .execute();
               
               System.out.println("Retrieved patient: " + createdPatient.getNameFirstRep().getNameAsSingleString());
               System.out.println("Patient birth date: " + createdPatient.getBirthDate());
               System.out.println("Patient gender: " + createdPatient.getGender());
               
           } catch (Exception e) {
               System.err.println("Error creating patient: " + e.getMessage());
               e.printStackTrace();
               System.exit(1);
           }
       }
   }
   ```

4. Build and run the application
   ```bash
   mvn clean compile exec:java
   ```

## Next steps

* Learn more about [Aidbox Access Control](../access-control/access-control.md)
* See [Source code](https://github.com/Aidbox/examples/tree/main/developer-experience/aidbox-hapi-client)

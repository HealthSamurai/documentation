# ISiK

In this tutorial we will guide you how to setup ISiK Stufe 2 FHIR Implementation Guide.&#x20;

## Setup Aidbox with ISiK Stufe 2 IG

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects.&#x20;

There's an [existing guide](https://docs.aidbox.app/getting-started/run-aidbox-locally-with-docker) for this process. Adhere to this guide, but note a variation when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:

```
git clone \
  --branch=isik-stufe-2 \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

The git template project contains ISiK FHIR IG preconfigured via .env file.

# SMART Client Authorization

SMART Defines Two Patterns For Client Authorization:

1. Authorizes a user-facing app to connect to Aidbox, enabling it to access "launch context" like a selected patient, based on the user's session in an EHR or choice at launch. This also delegates the user’s permissions to the app.

{% content-ref url="smart-app-launch.md" %}
[smart-app-launch.md](smart-app-launch.md)
{% endcontent-ref %}

2. Authorizes a backend service to connect to Aidbox, enabling interaction with an EHR without direct user involvement, using pre-assigned permissions.

{% content-ref url="smart-backend-services.md" %}
[smart-backend-services.md](smart-backend-services.md)
{% endcontent-ref %}

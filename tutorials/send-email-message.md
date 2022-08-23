# Mailgun integration

1. Create Mailgun account ([Mailgun Documentation](https://documentation.mailgun.com/en/latest/))

2. Add environment variables to the docker-compose.yaml

| Env name                | Sample value                      |
| ----------------------- | --------------------------------- |
| BOX_PROVIDER_MAILGUN__PROVIDER_URL | `https://api.mailgun.net/v3/{{DOMAIN}}/messages`     |
| BOX_PROVIDER_MAILGUN__PROVIDER_TYPE | `mailgun`     |                         |
| BOX_PROVIDER_MAILGUN__PROVIDER_FROM | `Sender's Name <noreply@{{DOMAIN}}>`      |
| BOX_PROVIDER_MAILGUN__PROVIDER_USERNAME | `api` | 
| BOX_PROVIDER_MAILGUN__PROVIDER_PASSWORD | `<password>` | 

3. Creating `NotificationTemplate` resource

NotificationTemplate - a resource that stores the body of a mail message

``` yaml
PUT /NotificationTemplate/notification-template-1
content-type: text/yaml
accept: text/yaml

template: <b>Hello world! Mustache value from Notification.providerData.payload - {{foo.bar}}</b>
```

4. Creating `Notification` resource

``` yaml
PUT /Notification/notification-1
content-type: text/yaml
accept: text/yaml

provider: 'mailgun-provider'         # Provider id
providerData:
  to: recipient@example.com          # Email of recipient
  subject: My subject of the message # subject of email
  template:                          # Notification template 
    id: notification-template-1
    resourceType: NotificationTemplate
  payload:                           # Вata that can be used in the template
    foo:
      bar: zaz
```

5. Call notify method

``` yaml
POST /Notification/notification-1/$send
```
### Example of email sent from the template above
![image](https://user-images.githubusercontent.com/43318093/186179607-8a51b6a0-f6dd-4913-84f8-856620eec533.png)



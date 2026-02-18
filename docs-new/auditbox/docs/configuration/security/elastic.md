# Securing ElasticSearch

Auditbox uses ElasticSearch as its primary active storage, so it's
extremely important to make sure it's configured in a secure manner.

## Adding a restricted user

In order to ensure principal of least privilege, it's highly
recommended to use a user with a custom role that has only the
permissions needed for Auditbox to operate.

### Create role

ou can create a custom role in a few ways, depending on your setup:
- [Using API call](https://www.elastic.co/docs/api/doc/elasticsearch/operation/operation-security-put-role)
- [Using ECK operator](https://www.elastic.co/docs/deploy-manage/users-roles/cluster-or-deployment-auth/defining-roles#managing-custom-roles)

Whichever solution you'll choose, the required permissions are as
such:

```json
{
  {
    "cluster": [
      "manage",
      "manage_ilm",
      "manage_index_templates"
    ],
    "indices": [
      {
        "names": [
          "auditevent-*"
        ],
        "privileges": [
          "manage",
          "maintenance",
          "create_index",
          "create_doc",
          "index",
          "read"
        ]
      },
      {
        "names": [
          "auditbox-idp",
          "auditbox-sessions"
        ],
        "privileges": [
          "manage",
          "maintenance",
          "create_index",
          "index",
          "read",
          "delete",
          "create"
        ]
      }
    ]
  }
}
```

### Create user for the role

In order to use newly defined role, you need a user with that role
attached. Just as with creating roles, there's a few options:
- [Using API call](https://www.elastic.co/docs/api/doc/elasticsearch/operation/operation-security-put-user)
- [Using ECK operator](https://www.elastic.co/docs/deploy-manage/users-roles/cluster-or-deployment-auth/file-based#add-users)

The only requirement is that user has the role defined previously
attached.

### Adjust Auditbox settings

On Auditbox side, you only need to change a `AUDITBOX_ES_AUTH`
variable so that it reflects the credentials for newly created user:

```yaml
AUDITBOX_ES_AUTH: "${username}:${password}"
```

### Reload the app

After reloading the application, the user will now be used, and the
setup is complete.

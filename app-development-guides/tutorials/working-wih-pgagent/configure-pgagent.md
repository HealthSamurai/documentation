# Configure pgagent

Для запуска pgagent вы должны указать переменную среды в deploy конфигурации `ENABLE_PGAGENT: "true”` Пример конфигурации k8s

```text
kind: Deployment
...
spec:
  containers:
      env:
        # Define the environment variable
        - name: ENABLE_PGAGENT 
          value: true
...
```

После перезапуска pod процесс pgagent тоже перезапустится

При старте pod в k8s запутстися pgagent под стандартным пользователем postgres Для того чтобы запустить pgagent от другого пользователя нужно указать допонительные переменные среды

```text
PGAGENT_USER:      "another user"
PGAGENT_PASSWORD:  "password"
```

Пример конфигурации k8s

```text
kind: Deployment
...
spec:
  containers:
      env:
        # Define the environment variable
        - name: ENABLE_PGAGENT 
          value: true
        - name: PGAGENT_USER 
          value: "another user" 
        - name: PGAGENT_PASSWORD 
          value: "password" #разумеется пароли лучше предвать через ресурс secret а не напрямую
...
```

Теперь в контейнере с базой запущен pgagent запущен под другим пользователем

Troubleshoot

> Если job pgagent запущен от другого пользователя не забудьте дать новому пользователю права на нужные таблицы


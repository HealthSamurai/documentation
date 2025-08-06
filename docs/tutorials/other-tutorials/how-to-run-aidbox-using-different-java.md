# How to Run Aidbox Using a Different Java Version

Starting from version 2506 Aidbox uses the following approach for building `aidboxone` image:

1. The latest LTS version of Java is used for building the application jar - for 2506, it's Java 21
2. The latest version of Java is used for running the application - for 2506 it's 24

That gives you the ability to run the Aidbox on any version of Java between LTS version and the latest version due to JAVA backward compatibility by building your custom Docker image.

## Objectives <a href="#objectives" id="objectives"></a>

* Build a Docker image from `aidboxone:2506` and run it using Java 21

## Before you begin <a href="#before-you-begin" id="before-you-begin"></a>

* Run Aidbox locally using Docker by following [this](../../getting-started/run-aidbox-locally.md) guide.

## Build and run custom Docker Image

1. Navigate to the `aidbox` directory when the `docker-compose.yaml` is located.
2.  Create the file with the name `Dockerfile` and the following context:\\

    ```docker
    # For extracting aidbox.jar from the original Aidbox image
    FROM healthsamurai/aidboxone:2506 as aidbox-extract

    FROM bellsoft/liberica-openjre-alpine-musl:21

    # Install packages
    RUN apk add --update --no-cache curl git openssh
    RUN apk add nodejs npm openssl --update-cache --repository http://dl-cdn.alpinelinux.org/alpine/edge/main --allow-untrusted
    RUN apk add libsodium
    RUN apk upgrade
    RUN apk --purge del apk-tools

    # Add app user
    ARG APPLICATION_USER=aidbox
    RUN adduser  -u 1000 -D $APPLICATION_USER

    # Configure working directory
    RUN mkdir /app && \
        chown -R $APPLICATION_USER /app

    USER 1000

    # Copy the original jar and script
    COPY --chown=1000:1000 --from=aidbox-extract /aidbox.jar /aidbox.jar
    COPY --chown=1000:1000 --from=aidbox-extract /aidbox.sh /aidbox.sh

    RUN chmod +x /aidbox.sh

    ENV MAIN=aidboxone.core

    ENTRYPOINT ["/aidbox.sh"]
    ```
3.  Make the following changes to `docker-compose.yaml`\


    ```yaml
      aidbox:
        # image: healthsamurai/aidboxone:edge
        # pull_policy: always
        build:
          context: .
          dockerfile: Dockerfile
    ```
4. Run `docker compose up` command. It will build the image from `Dockerfile` and run the Docker compose.
5.  Run the following command to verify the aidbox container is running using Java 21:\\

    `docker compose exec aidbox java -version`\
    \
    The output should be:\\

    ```
    % docker compose exec aidbox java -version         
    openjdk version "21.0.7" 2025-04-15 LTS
    OpenJDK Runtime Environment (build 21.0.7+9-LTS)
    OpenJDK 64-Bit Server VM (build 21.0.7+9-LTS, mixed mode)
    ```

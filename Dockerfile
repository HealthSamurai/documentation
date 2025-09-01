FROM clojure:tools-deps AS builder
WORKDIR /srv
COPY . /srv/

RUN --mount=target=/root/.m2,type=cache,sharing=locked \
    make uberjar

FROM bellsoft/liberica-openjre-alpine-musl:24 AS final

# Install git for lastmod generation
RUN apk add --no-cache git

COPY --from=builder /srv/target/gitbok.jar /gitbok.jar

# Declare volume mount point for documentation
VOLUME ["/docs-volume"]

RUN adduser -u 1000 -D user
USER 1000
ENTRYPOINT ["java", "-jar", "/gitbok.jar"]

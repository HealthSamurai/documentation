FROM clojure:tools-deps AS builder
WORKDIR /srv
COPY . /srv/

RUN --mount=target=/root/.m2,type=cache,sharing=locked \
    make uberjar

FROM bellsoft/liberica-openjre-alpine-musl:24 AS final
COPY --from=builder /srv/target/gitbok.jar /gitbok.jar
RUN adduser -u 1000 -D user
USER 1000
ENTRYPOINT ["java", "-Xms1g", "-Xmx2g", "/gitbok.jar"]

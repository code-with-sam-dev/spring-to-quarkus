# Spring Boot to Quarkus

Two payment services, one in Spring Boot 4.1.1 and one in Quarkus 3.39.5, both
on Java 25. Every claim in the video is reproduced by a script in `scripts/`.

## The finding

The same property gates the same bean in both apps. Build each once with
`payments.fraud-check.enabled=false`, then start the artifact with the property
set to `true`:

    ./scripts/verify-hook.sh

    Spring   GET /fraud-check  {"fraudCheck":"present"}
    Quarkus  GET /fraud-check  {"fraudCheck":"absent"}

Spring evaluates `@ConditionalOnProperty` when the application starts.
Quarkus decides `@IfBuildProperty` during the build, and the runtime value has
no effect on which beans exist. The Quarkus startup log says nothing about it.

Verified on 2026-09-23, Java 25.0.4.

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

## The second finding: who owns the transaction

    docker compose up -d        # Postgres on 5435
    (cd spring-payments && ./mvnw test)
    (cd quarkus-payments && ./mvnw test)

`PaymentService.record` has no `@Transactional` in Spring, and Spring Data's
`save()` writes the row anyway, because it opens its own transaction. The same
method translated line for line to Panache's `persist()` throws:

    jakarta.persistence.TransactionRequiredException: Transaction is not active,
    consider adding @Transactional to your method to automatically activate one.

Add `@Transactional` and the row is written.

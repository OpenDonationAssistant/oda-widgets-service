# PROJECT KNOWLEDGE BASE

**Generated:** 2026-09-11
**Commit:** 0a5c374
**Branch:** master

## OVERVIEW

Micronaut 5.1.1 microservice (Java 25) managing streamer donation widgets for the OpenDonationAssistant ecosystem. Handles CRUD over HTTP, config propagation over RabbitMQ, persistence in PostgreSQL (Flyway-migrated), metrics via Micrometer/Prometheus, JWT/Keycloak auth.

## STRUCTURE

```
oda-widgets-service/
├── pom.xml                          # Maven build, Micronaut 5.1.1, JDK 25
├── Dockerfile                       # fedora:41, expects pre-built native binary
├── local.sh                         # dev deploy: native-image → podman → k3s
├── .github/workflows/maven.yml      # delegates to oda-libraries reusable workflow
├── .mvn/jvm.config                  # compiler --add-exports for ErrorProne/NullAway
├── aot-jar.properties               # Micronaut AOT config (jar packaging)
├── aot-native-image.properties      # Micronaut AOT config (native-image)
├── src/main/java/.../
│   ├── Application.java             # @Factory main; env wiring; Rabbit bindings
│   ├── widget/                      # PRIMARY bounded context (see widget/AGENTS.md)
│   └── template/                    # SECONDARY bounded context (see template/AGENTS.md)
├── src/main/resources/
│   ├── application.yml              # core config (metrics, serde, security/JWKS, flyway)
│   ├── application-standalone.yml   # default env (JDBC/RabbitMQ env var wiring)
│   ├── application-allinone.yml     # debug/test profile
│   └── db/migration/V1..V6__*.sql   # Flyway migrations (schema: widget)
└── src/test/                        # JUnit 5 + Mockito + Instancio
```

## WHERE TO LOOK

| Task | Location | Notes |
|------|----------|-------|
| Add new widget type | `widget/model/`, `WidgetProperty.of()`, `Widget.of()` | Create model class + wire in factory |
| Add REST endpoint | `widget/api/` (interface) + `widget/commands/` or `widget/view/` (impl) | API contracts in api/, controllers implement them |
| Add RabbitMQ listener | `widget/eventbus/` | Extend `@RabbitListener`, add binding |
| Add DB migration | `src/main/resources/db/migration/` | Naming: `V<n>__description.sql` |
| Change config schema | `widget/model/properties/` | Generic property wrappers (`FontProperty`, etc.) |
| Modify template CRUD | `template/` | Same layered pattern as widget |
| Add metrics | `widget/metrics/WidgetMetrics` | Micrometer counters, referenced in tests |

## CODE MAP

| Symbol | Type | Location | Role |
|--------|------|----------|------|
| `Application` | `@Factory` main | `Application.java` | Bootstrap, RabbitMQ wiring, env config |
| `Widget` | Domain aggregate | `widget/model/Widget.java` | Core domain; self-persistence + event emission |
| `WidgetRepository` | Domain service | `widget/repository/WidgetRepository.java` | Facade over JDBC repo + event sender |
| `WidgetData` | `@MappedEntity` | `widget/repository/WidgetData.java` | Persistence record (9-arg constructor) |
| `WidgetController` | REST controller | `widget/view/WidgetController.java` | GET/PATCH/DELETE + reorder |
| `WidgetCommandListener` | RabbitMQ listener | `widget/eventbus/WidgetCommandListener.java` | Applies `WidgetUpdateCommand` patches |
| `WidgetChangedEventSender` | `@RabbitClient` | `widget/eventbus/WidgetChangedEventSender.java` | Publishes config changes |
| `WidgetProperty.of()` | Factory | `widget/model/WidgetProperty.java` | Maps property names → typed classes |
| `WidgetMetrics` | Micrometer | `widget/metrics/WidgetMetrics.java` | Counter constants (ADDED/UPDATED/DELETED) |
| `Template` | Domain | `template/Template.java` | Showcase templates, same pattern as Widget |
| `TemplateData` | `@MappedEntity` | `template/repository/TemplateData.java` | Template persistence record |

## CONVENTIONS

- **Nullability**: JSpecify annotations (`@NonNull`/`@Nullable`) enforced by NullAway as compile errors
- **2-space indentation**, Google-Java-Format style, `var` for locals
- **Package per feature**: `widget/`, `template/` with layers: `api/`, `commands/`, `view/`, `model/`, `repository/`, `eventbus/`, `metrics/`
- **API interface pattern**: REST contracts in `*Api` interfaces (OpenAPI-annotated), controllers implement them
- **Records for DTOs** (`*Dto`) and persistence (`*Data`); `@Serdeable` on serialized types
- **RabbitMQ naming**: queue constants as `public static final String QUEUE_NAME`; bindings as `public static final List<Exchange> BINDINGS`
- **Test pattern**: metrics counters as test assertions; JSON fixtures loaded via classpath
- **Env vars**: SCREAMING_SNAKE_CASE with `${VAR:default}` syntax (e.g., `JDBC_URL`, `RABBITMQ_HOST`)

## ANTI-PATTERNS (THIS PROJECT)

- **Unchecked casts** widespread in `WidgetProperty.of()`, `Widget.getConfig()`, `PaymentAlertProperty` — runtime `ClassCastException` risk on malformed configs
- **`serde.serialization.inclusion: ALWAYS`** — nulls always serialized in API responses
- **JDK version mismatch**: pom targets JDK 25; `local.sh` pins GraalVM JDK 21
- **`WidgetCommandListener`** applies each property patch separately calling `.save()` per property → N events per command instead of one batch
- **Silent migration path**: `UpdateController.runUpdate()` + `Widget.save("migration")` suppresses `WidgetChangedEvent` notifications
- **Property name collisions** documented in `WidgetProperty.java:65` (`layout` → paymentalerts/toplist) and `:108` (`backgroundImage` → goal/reel)
- **CI externalized**: build/publish logic lives in `oda-libraries` reusable workflow, not reviewable from this repo
- **Dockerfile** expects host-built native binary; not reproducible from source

## COMMANDS

```bash
# Build (jar)
./mvnw clean package

# Build (native image)
./mvnw clean package -Dpackaging=native-image

# Run tests
./mvnw test

# Local deploy (native → podman → k3s)
bash local.sh
```

## NOTES

- Two inbound channels: HTTP (Micronaut controllers) + RabbitMQ (listeners/handlers)
- `Application.java` is `@Factory` — bootstrap fused with RabbitMQ bean wiring
- Default env is hard-pinned to `standalone` by `@ContextConfigurer`
- `GET /widgets` is `IS_ANONYMOUS`; all other endpoints are `IS_AUTHENTICATED`
- `template/listener/TemplateCommandListener.java` is a zero-byte placeholder (never implemented)
- Flyway schema is `widget` (both widget and template tables)
- No JS/TS/Python tooling — quality enforced via Maven/SonarCloud/ErrorProne/NullAway

# TEMPLATE BOUNDED CONTEXT

Secondary domain: showcase templates for widgets. Same layered pattern as widget context.

## STRUCTURE

```
template/
├── Template.java            # Domain aggregate (same pattern as Widget)
├── api/                     # REST + OpenAPI interfaces (3 files)
│   ├── TemplateApi.java           # GET /templates
│   ├── CreateTemplateApi.java     # POST /templates/commands/create
│   └── DeleteTemplateApi.java     # POST /templates/commands/delete-template
├── commands/                # Command controllers (2 files)
│   ├── CreateTemplate.java
│   └── DeleteTemplate.java
├── view/                    # TemplateController + TemplateDto
├── repository/              # Data access (3 files)
│   ├── TemplateData.java          # @MappedEntity record
│   ├── TemplateDataRepository.java
│   └── TemplateRepository.java
├── eventbus/                # DeletedTemplateEvent (RabbitMQ event)
└── listener/                # TemplateCommandListener.java (EMPTY — never implemented)
```

## WHERE TO LOOK

| Task | Location | Notes |
|------|----------|-------|
| Modify template CRUD | `commands/` + `view/` | Same pattern: api/ interface → controller impl |
| Change template schema | `repository/TemplateData` | Record with wither methods |
| Add template event | `eventbus/` | `DeletedTemplateEvent` published on delete |

## CONVENTIONS

- Same pattern as widget: `Template` wraps `TemplateData` + repository + event sender
- `@Serdeable` on domain and DTO types
- Templates are scoped to owner via `getOwnerId(Auth)` from `BaseController`

## ANTI-PATTERNS

- `listener/TemplateCommandListener.java` is a **zero-byte placeholder** — never implemented, no content
- `DumpConfigs` in widget context replays config events for all widgets including templates

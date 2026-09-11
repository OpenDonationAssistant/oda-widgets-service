# WIDGET BOUNDED CONTEXT

Primary domain: streamer donation widgets (payment alerts, donation goals, top lists, reels, roulette, etc.).

## STRUCTURE

```
widget/
├── api/                  # REST + OpenAPI contract interfaces (6 files)
│   ├── WidgetApi.java         # GET/PATCH/DELETE /widgets
│   ├── AddWidgetApi.java      # POST /widgets/commands/add
│   ├── ToggleWidgetApi.java   # POST /widgets/commands/toggle
│   ├── AddTagApi.java         # POST /widgets/commands/add-tag
│   ├── DumpConfigsApi.java    # POST /widgets/commands/dump-configs
│   └── UpdateApi.java         # POST /update (excluded from OpenAPI)
├── commands/             # Command controllers implementing api/ interfaces (5 files)
├── view/                 # WidgetController + WidgetDto (2 files)
├── model/                # Domain models (Widget aggregate + type-specific properties)
│   ├── Widget.java            # Core aggregate; self-persistence + event emission
│   ├── WidgetProperty.java    # Factory: maps property names → typed classes
│   ├── Utils.java             # Border/color map builders
│   ├── properties/            # Generic property wrappers (16 files)
│   ├── paymentalert/          # Payment-alerts-specific properties
│   ├── donationgoal/          # Donation-goal-specific properties
│   ├── donaton/               # Donaton-specific properties
│   ├── reel/                  # Reel-specific properties
│   ├── roulette/              # Roulette-specific properties
│   ├── toplist/               # Toplist-specific properties
│   └── horizontalevents/      # Horizontal-events-specific properties
├── repository/           # Data access layer (3 files)
│   ├── WidgetData.java        # @MappedEntity record (9-arg constructor)
│   ├── WidgetDataRepository.java  # Micronaut Data @JdbcRepository
│   └── WidgetRepository.java  # Domain facade (self-persistence + events)
├── eventbus/             # RabbitMQ consumers/producers (4 files)
│   ├── WidgetCommandListener.java       # Applies WidgetUpdateCommand patches
│   ├── WidgetConfigRequestListener.java # RPC: returns widget config
│   ├── SetWidgetConfigRequestHandler.java # RPC: sets widget config
│   ├── CreateWidgetRequestHandler.java   # RPC: creates widget
│   └── WidgetChangedEventSender.java     # @RabbitClient: publishes changes
└── metrics/              # WidgetMetrics.java (Micrometer counters)
```

## WHERE TO LOOK

| Task | Location | Notes |
|------|----------|-------|
| Add new widget type model | `model/<type>/` + `WidgetProperty.of()` + `Widget.of()` | Create property classes, wire in factories |
| Add REST endpoint | `api/` (interface) → `commands/` or `view/` (impl) | Contract-first: interface has `@OpenAPIDefinition` |
| Add RabbitMQ handler | `eventbus/` | Extend `@RabbitListener`, add to `Application.rabbitConfiguration()` |
| Modify persistence | `repository/WidgetData` + `WidgetDataRepository` | Micronaut Data derived queries |
| Add property type | `model/properties/` | Generic wrappers: `FontProperty`, `BorderProperty`, etc. |

## CONVENTIONS

- **Widget aggregate**: `Widget` wraps `WidgetData` + repository + event sender; calls `.save()` to self-persist + emit events
- **Type dispatch**: `Widget.of()` dispatches `"payment-alerts"` → `PaymentAlertsWidget`; all others → generic `Widget`
- **Property factory**: `WidgetProperty.of(name, value)` maps string names → typed property classes (many cases commented out)
- **Command controllers** implement API interfaces; HTTP annotations live on the interface, not the controller
- **Security**: all endpoints require `IS_AUTHENTICATED` except `GET /widgets` (anonymous)

## ANTI-PATTERNS

- **`WidgetCommandListener`** calls `.save()` per property patch → N events per command instead of batch
- **`WidgetProperty.of()`** has ~130 lines commented-out switch cases with documented name collisions
- **Unchecked casts** in `Widget.getConfig()`, `Widget.getValue()`, `PaymentAlertProperty` — runtime `ClassCastException` risk
- **`WidgetRepository.convert()`** only has `default` switch branch → never builds `PaymentAlertsWidget` via repo path
- **Dead code**: `UpdateController` has dormant `mediaWidgetUpdate()`, `alignmentUpdate()`, `fontUpdate()` helpers

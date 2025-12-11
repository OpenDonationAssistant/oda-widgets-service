package io.github.opendonationassistant.widget.model;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.WidgetChangedEvent;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.events.widget.WidgetConfig;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import io.micronaut.serde.annotation.Serdeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.zalando.problem.Problem;

@Serdeable
public class Widget {

  private final ODALogger log = new ODALogger(this);
  private final WidgetData data;
  private final WidgetDataRepository repository;
  private final WidgetChangedNotificationSender notificationSender;
  protected final Map<String, WidgetProperty<?>> index = new HashMap<>();

  public Widget(
    WidgetData data,
    WidgetDataRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    this.data = data;
    this.repository = repository;
    this.notificationSender = notificationSender;
    this.properties().forEach(it -> index.put(it.name(), it));
  }

  public String id() {
    return data.id();
  }

  public Map<String, Object> getConfig() {
    var config = data.config();
    if (config == null || config.isEmpty()) {
      return Map.of("properties", List.of());
    }
    return config;
  }

  public String type() {
    return data.type();
  }

  @SuppressWarnings("unchecked")
  protected List<Map<String, Object>> props() {
    var config = getConfig();
    List<Object> properties = (List<Object>) config.getOrDefault(
      "properties",
      List.<Object>of()
    );
    return properties.stream().map(it -> (Map<String, Object>) it).toList();
  }

  public Widget runUpdate(Update update) {
    var condition = update.condition();
    if (
      condition.widgetClass() != null &&
      !condition.widgetClass().isInstance(this)
    ) {
      return this;
    }
    final Widget updated = updateProperties(
      this.properties()
        .stream()
        .map(prop -> prop.update(update))
        .map(prop -> Map.of("name", prop.name(), "value", prop.value()))
        .toList()
    );
    repository.update(updated.data());
    return updated;
  }

  @SuppressWarnings("unchecked")
  protected <C> List<WidgetProperty<C>> properties() {
    return this.props()
      .stream()
      .map(entry -> {
        if (entry.get("name") == null || entry.get("value") == null) {
          throw Problem.builder().withTitle("Invalid widget property").build();
        }
        return (WidgetProperty<C>) WidgetProperty.of(
          (String) entry.get("name"),
          entry.get("value")
        );
      })
      .toList();
  }

  public boolean hasNoProperties() {
    return props().isEmpty();
  }

  public Optional<Map<String, Object>> getProperty(String name) {
    return props()
      .stream()
      .filter(prop -> name.equals(prop.get("name")))
      .findFirst();
  }

  public <T> Optional<T> getValue(String name) {
    return getProperty(name)
      .map(prop -> prop.get("value"))
      .map(value -> (T) value);
  }

  public Widget removeProperty(String name) {
    return updateProperties(
      props()
        .stream()
        .filter(prop -> !name.equals(((Map<String, Object>) prop).get("name")))
        .toList()
    );
  }

  public Widget updateProperty(String name, @Nullable Object value) {
    log.info(
      "Updating property",
      Map.of("property", name, "oldValue", getValue(name), "newValue", value)
    );
    return updateProperties(
      props()
        .stream()
        .map(property ->
          name.equals(property.get("name"))
            ? Map.<String, Object>of("name", name, "value", value)
            : property
        )
        .toList()
    );
  }

  public Widget setName(String name) {
    return new Widget(data.withName(name), repository, notificationSender);
  }

  public Widget setSortOrder(int sortOrder) {
    return new Widget(
      data.withSortOrder(sortOrder),
      repository,
      notificationSender
    );
  }

  public io.github.opendonationassistant.events.widget.Widget asDto() {
    return new io.github.opendonationassistant.events.widget.Widget(
      data.id(),
      data.type(),
      data.sortOrder(),
      data.name(),
      data.enabled(),
      data.ownerId(),
      new WidgetConfig(
        ((List<Object>) getConfig()
            .getOrDefault("properties", List.of())).stream()
          .map(prop -> {
            var property = (Map<String, Object>) prop;
            return new io.github.opendonationassistant.events.widget.WidgetProperty(
              Optional.ofNullable((String) property.get("name")).orElse(""),
              Optional.ofNullable((String) property.get("displayName")).orElse(
                ""
              ),
              Optional.ofNullable((String) property.get("type")).orElse(""),
              (Object) property.get("value")
            );
          })
          .toList()
      )
    );
  }

  public WidgetData data() {
    return this.data;
  }

  public Widget save() {
    repository.update(data);
    notificationSender.send(
      data.type(),
      new WidgetChangedEvent("updated", asDto())
    );
    return this;
  }

  public Widget toggle() {
    var updatedData = data.withEnabled(!data.enabled());
    var result = new Widget(updatedData, repository, notificationSender);
    repository.update(updatedData);
    notificationSender.send(
      data.type(),
      new WidgetChangedEvent("toggled", result.asDto())
    );
    return result;
  }

  public Widget delete() {
    var result = new Widget(
      data.withDeleted(true),
      repository,
      notificationSender
    );
    result.save();
    notificationSender.send(
      data.type(),
      new WidgetChangedEvent("deleted", result.asDto())
    );
    return result;
  }

  private Widget updateProperties(List<Map<String, Object>> updatedProperties) {
    var updatedConfig = new HashMap<String, Object>();
    updatedConfig.putAll(getConfig());
    updatedConfig.put("properties", updatedProperties);
    return withConfig(updatedConfig);
  }

  public Widget withConfig(Map<String, Object> config) {
    var updatedData = data.withConfig(config);
    return new Widget(updatedData, repository, notificationSender);
  }
}

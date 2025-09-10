package io.github.opendonationassistant.widget.repository;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.WidgetChangedEvent;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.events.widget.WidgetConfig;
import io.github.opendonationassistant.events.widget.WidgetProperty;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

@Serdeable
@MappedEntity("widget")
public class Widget {

  private final ODALogger log = new ODALogger(this);
  private WidgetData data;
  private WidgetDataRepository repository;
  private WidgetChangedNotificationSender notificationSender;

  public Widget(
    WidgetData data,
    WidgetDataRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    this.data = data;
    this.repository = repository;
    this.notificationSender = notificationSender;
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

  private List<Map<String, Object>> properties() {
    var config = getConfig();
    List<Object> properties = (List) config.getOrDefault(
      "properties",
      List.<Object>of()
    );
    return properties.stream().map(it -> (Map<String, Object>) it).toList();
  }

  public boolean hasNoProperties() {
    return properties().isEmpty();
  }

  public Optional<Map<String, Object>> getProperty(String name) {
    return properties()
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
      properties()
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
      properties()
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
            return new WidgetProperty(
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
    var result = new Widget(
      data.withEnabled(data.enabled()),
      repository,
      notificationSender
    );
    repository.update(data);
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

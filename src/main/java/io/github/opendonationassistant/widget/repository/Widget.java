package io.github.opendonationassistant.widget.repository;

import io.github.opendonationassistant.events.widget.WidgetProperty;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Serdeable
@MappedEntity("widget")
public class Widget extends WidgetData {

  public Map<String, Object> getConfig() {
    var config = super.getConfig();
    if (config == null || config.isEmpty()) {
      return Map.of("properties", List.of());
    }
    return config;
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
    var updatedProperties = properties()
      .stream()
      .filter(prop -> !name.equals(((Map<String, Object>) prop).get("name")))
      .toList();
    var updatedConfig = new HashMap<String, Object>();
    updatedConfig.putAll(getConfig());
    updatedConfig.put("properties", updatedProperties);
    return withConfig(updatedConfig);
  }

  public Widget updateProperty(String name, Object value) {
    final List<Map<String, Object>> updatedProperties = properties()
      .stream()
      .map(property ->
        name.equals(property.get("name"))
          ? Map.<String, Object>of("name", name, "value", value)
          : property
      )
      .toList();
    var updatedConfig = new HashMap<String, Object>();
    updatedConfig.putAll(getConfig());
    updatedConfig.put("properties", updatedProperties);
    return withConfig(updatedConfig);
  }

  private Widget withConfig(Map<String, Object> config) {
    var result = new Widget();
    result.setId(getId());
    result.setName(getName());
    result.setType(getType());
    result.setOwnerId(getOwnerId());
    result.setSortOrder(getSortOrder());
    result.setEnabled(getEnabled());
    result.setConfig(config);
    return result;
  }

  public io.github.opendonationassistant.events.widget.Widget asDto() {
    var converted = new io.github.opendonationassistant.events.widget.Widget(
      getId(),
      getType(),
      getSortOrder(),
      getName(),
      getEnabled(),
      getOwnerId(),
      new io.github.opendonationassistant.events.widget.WidgetConfig(
        ((List<Object>) getConfig()
            .getOrDefault("properties", List.of())).stream()
          .map(prop -> {
            var property = (Map<String, Object>) prop;
            return new WidgetProperty(
              (String) property.get("name"),
              (String) property.get("displayName"),
              (String) property.get("type"),
              (Object) property.get("value")
            );
          })
          .toList()
      )
    );
    return converted;
  }
}

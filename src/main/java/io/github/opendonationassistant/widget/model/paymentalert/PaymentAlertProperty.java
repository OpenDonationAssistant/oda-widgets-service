package io.github.opendonationassistant.widget.model.paymentalert;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.widget.model.Update;
import io.github.opendonationassistant.widget.model.WidgetProperty;
import io.github.opendonationassistant.widget.model.properties.AlignmentProperty;
import io.github.opendonationassistant.widget.model.properties.FontProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class PaymentAlertProperty
  extends WidgetProperty<List<Map<String, Object>>> {

  private final ODALogger log = new ODALogger(this);

  private List<PaymentAlert> alerts = new ArrayList<>();

  public PaymentAlertProperty(String name, List<Map<String, Object>> value) {
    super(name, value);
    value.stream().map(alert -> new PaymentAlert(alert)).forEach(alerts::add);
  }

  @SuppressWarnings("unchecked")
  public PaymentAlertProperty(String name, Object value) {
    this(
      name,
      ((List<Object>) value).stream()
        .map(item -> (Map<String, Object>) item)
        .toList()
    );
  }

  @Override
  public PaymentAlertProperty update(Update update) {
    log.debug("Running updateFn", Map.of("propertyName", name()));
    var updatedAlerts = alerts()
      .stream()
      .map(alert -> alert.update(update))
      .map(alert -> alert.config)
      .toList();
    return new PaymentAlertProperty(name(), updatedAlerts);
  }

  public List<PaymentAlert> alerts() {
    return alerts;
  }

  public static class PaymentAlert {

    private final Map<String, Object> config;
    private final Map<String, WidgetProperty<?>> index = new HashMap<>();

    public PaymentAlert(Map<String, Object> config) {
      this.config = config;
      this.properties().forEach(prop -> index.put(prop.name(), prop));
    }

    public Optional<FontProperty> font() {
      return Optional.ofNullable((FontProperty) this.index.get("font"));
    }

    public Optional<FontProperty> headerFont() {
      return Optional.ofNullable((FontProperty) this.index.get("headerFont"));
    }

    public Optional<AlignmentProperty> headerAlignment() {
      return Optional.ofNullable(
        (AlignmentProperty) this.index.get("headerAlignment")
      );
    }

    public Optional<AlignmentProperty> messageAlignment() {
      return Optional.ofNullable(
        (AlignmentProperty) this.index.get("messageAlignment")
      );
    }

    @SuppressWarnings("unchecked")
    protected <C> List<WidgetProperty<C>> properties() {
      return (
        (List<Object>) this.config.getOrDefault("properties", List.<Object>of())
      ).stream()
        .map(item -> (Map<String, Object>) item)
        .flatMap(entry -> {
          var name = (String) entry.get("name");
          var value = entry.get("value");
          if (name == null || value == null) {
            return Stream.of();
          }
          return Stream.of((WidgetProperty<C>) WidgetProperty.of(name, value));
        })
        .toList();
    }

    public PaymentAlert update(Update update) {
      var updatedConfig = new HashMap<>(this.config);
      var properties =
        this.properties()
          .stream()
          .map(prop -> prop.update(update))
          .map(prop -> Map.of("name", prop.name(), "value", prop.value()))
          .toList();
      updatedConfig.put("properties", properties);
      return new PaymentAlert(updatedConfig);
    }
  }
}

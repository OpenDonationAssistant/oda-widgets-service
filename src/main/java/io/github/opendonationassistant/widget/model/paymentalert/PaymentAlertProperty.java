package io.github.opendonationassistant.widget.model.paymentalert;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import io.github.opendonationassistant.widget.model.properties.FontProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class PaymentAlertProperty
  extends WidgetProperty<List<Map<String, Object>>> {

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
  }
}

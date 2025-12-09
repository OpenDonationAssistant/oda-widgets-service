package io.github.opendonationassistant.widget.model.properties;

import java.util.Map;
import java.util.Optional;

import io.github.opendonationassistant.widget.model.WidgetProperty;

public class FontProperty extends WidgetProperty<Map<String, Object>> {

  public FontProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }

  public String family() {
    return Optional.ofNullable(this.value().get("family"))
      .map(it -> (String) it)
      .orElse("");
  }
}

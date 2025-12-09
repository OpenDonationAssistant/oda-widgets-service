package io.github.opendonationassistant.widget.model.properties;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.Map;

public class RoundingProperty extends WidgetProperty<Map<String, Object>> {

  public RoundingProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }
}

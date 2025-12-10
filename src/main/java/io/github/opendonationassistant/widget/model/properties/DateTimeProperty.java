package io.github.opendonationassistant.widget.model.properties;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.Map;

public class DateTimeProperty extends WidgetProperty<Map<String, Object>> {

  @SuppressWarnings("unchecked")
  public DateTimeProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }
}

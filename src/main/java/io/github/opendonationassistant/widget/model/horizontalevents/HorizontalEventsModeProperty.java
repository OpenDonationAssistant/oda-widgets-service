package io.github.opendonationassistant.widget.model.horizontalevents;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.Map;

public class HorizontalEventsModeProperty
  extends WidgetProperty<Map<String, Object>> {

  @SuppressWarnings("unchecked")
  public HorizontalEventsModeProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }
}

package io.github.opendonationassistant.widget.model.properties;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.Map;

public class TextColorProperty extends WidgetProperty<Map<String, Object>> {

  public TextColorProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }
}

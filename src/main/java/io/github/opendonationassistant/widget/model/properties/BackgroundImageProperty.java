package io.github.opendonationassistant.widget.model.properties;

import java.util.Map;

import io.github.opendonationassistant.widget.model.WidgetProperty;

public class BackgroundImageProperty
  extends WidgetProperty<Map<String, Object>> {

  public BackgroundImageProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }
}

package io.github.opendonationassistant.widget.model.properties;

import io.github.opendonationassistant.widget.model.WidgetProperty;

public class BooleanProperty extends WidgetProperty<Boolean> {

  public BooleanProperty(String name, Object value) {
    super(name, (Boolean) value);
  }
}

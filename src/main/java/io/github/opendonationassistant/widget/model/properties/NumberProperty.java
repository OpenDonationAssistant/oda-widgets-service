package io.github.opendonationassistant.widget.model.properties;

import io.github.opendonationassistant.widget.model.WidgetProperty;

public class NumberProperty extends WidgetProperty<Integer> {

  public NumberProperty(String name, Object value) {
    super(name, (Integer)value);
  }
}

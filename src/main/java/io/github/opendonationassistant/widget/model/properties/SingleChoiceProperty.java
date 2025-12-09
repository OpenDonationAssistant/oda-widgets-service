package io.github.opendonationassistant.widget.model.properties;

import io.github.opendonationassistant.widget.model.WidgetProperty;

public class SingleChoiceProperty extends WidgetProperty<String> {

  public SingleChoiceProperty(String name, Object value) {
    super(name, (String) value);
  }
}

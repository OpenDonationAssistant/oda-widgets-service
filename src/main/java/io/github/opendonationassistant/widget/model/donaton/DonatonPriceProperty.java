package io.github.opendonationassistant.widget.model.donaton;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.Map;

public class DonatonPriceProperty extends WidgetProperty<Map<String, Object>> {

  @SuppressWarnings("unchecked")
  public DonatonPriceProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }
}

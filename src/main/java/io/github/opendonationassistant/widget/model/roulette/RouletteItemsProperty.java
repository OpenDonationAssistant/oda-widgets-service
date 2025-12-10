package io.github.opendonationassistant.widget.model.roulette;

import java.util.List;
import java.util.Map;

import io.github.opendonationassistant.widget.model.WidgetProperty;

public class RouletteItemsProperty
  extends WidgetProperty<List<Map<String, Object>>> {

  @SuppressWarnings("unchecked")
  public RouletteItemsProperty(String name, Object value) {
    super(name, (List<Map<String, Object>>) value);
  }
}

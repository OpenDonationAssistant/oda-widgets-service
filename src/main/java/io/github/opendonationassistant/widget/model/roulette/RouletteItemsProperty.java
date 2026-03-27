package io.github.opendonationassistant.widget.model.roulette;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.List;
import java.util.Map;

public class RouletteItemsProperty
  extends WidgetProperty<List<Map<String, Object>>> {

  @SuppressWarnings("unchecked")
  public RouletteItemsProperty(String name, Object value) {
    super(name, (List<Map<String, Object>>) value);
  }
}

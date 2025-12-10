package io.github.opendonationassistant.widget.model.toplist;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.Map;

public class DonatersTopListCarouselProperty
  extends WidgetProperty<Map<String, Object>> {

  public DonatersTopListCarouselProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }
}

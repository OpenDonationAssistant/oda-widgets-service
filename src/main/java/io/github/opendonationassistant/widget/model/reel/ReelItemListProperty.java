package io.github.opendonationassistant.widget.model.reel;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.List;

public class ReelItemListProperty extends WidgetProperty<List<String>> {

  @SuppressWarnings("unchecked")
  public ReelItemListProperty(String name, Object value) {
    super(name, (List<String>) value);
  }
}

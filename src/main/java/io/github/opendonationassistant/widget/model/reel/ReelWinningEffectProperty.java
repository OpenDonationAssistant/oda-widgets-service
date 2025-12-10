package io.github.opendonationassistant.widget.model.reel;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.Map;

public class ReelWinningEffectProperty
  extends WidgetProperty<Map<String, Object>> {

  @SuppressWarnings("unchecked")
  public ReelWinningEffectProperty(String name, Object value) {
    super(name, (Map<String, Object>) value);
  }
}

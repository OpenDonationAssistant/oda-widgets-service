package io.github.opendonationassistant.widget.model.donationgoal;

import io.github.opendonationassistant.widget.model.WidgetProperty;
import java.util.List;
import java.util.Map;

public class DonationGoalProperty
  extends WidgetProperty<List<Map<String, Object>>> {

  public DonationGoalProperty(String name, Object value) {
    super(name, (List<Map<String, Object>>) value);
  }
}

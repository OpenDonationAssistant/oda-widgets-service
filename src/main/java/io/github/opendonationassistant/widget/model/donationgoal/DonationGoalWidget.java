package io.github.opendonationassistant.widget.model.donationgoal;

import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.model.Widget;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;

public class DonationGoalWidget extends Widget {

  public DonationGoalWidget(
    WidgetData data,
    WidgetDataRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    super(data, repository, notificationSender);
  }
}

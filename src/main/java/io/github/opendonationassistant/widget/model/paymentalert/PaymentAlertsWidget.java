package io.github.opendonationassistant.widget.model.paymentalert;

import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.model.Widget;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import java.util.List;
import java.util.Map;

public class PaymentAlertsWidget extends Widget {

  public PaymentAlertsWidget(
    WidgetData data,
    WidgetDataRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    super(data, repository, notificationSender);
  }

  public PaymentAlertProperty alertProperty() {
    return (PaymentAlertProperty) this.index.getOrDefault(
        "alerts",
        new PaymentAlertProperty("alerts", List.<Map<String, Object>>of())
      );
  }
}

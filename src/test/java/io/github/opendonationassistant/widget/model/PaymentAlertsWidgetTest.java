package io.github.opendonationassistant.widget.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.model.paymentalert.PaymentAlertsWidget;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class PaymentAlertsWidgetTest {

  final ODALogger log = new ODALogger(this);
  WidgetDataRepository repository = mock(WidgetDataRepository.class);
  WidgetChangedNotificationSender notificationSender = mock(
    WidgetChangedNotificationSender.class
  );

  @Test
  public void testReadingWidget() throws IOException {
    var config = (Map<String, Object>) ObjectMapper.getDefault()
      .readValue(
        getClass()
          .getClassLoader()
          .getResourceAsStream("paymentalertswidget.json"),
        Argument.ofInstance(new HashMap<String, Object>())
      );
    var data = new WidgetData(
      "id",
      "payment-alerts",
      2,
      "name",
      "testuser",
      Optional.ofNullable(config).orElse(Map.of()),
      true,
      false
    );
    var widget = new PaymentAlertsWidget(data, repository, notificationSender);
    assertEquals(1, widget.alertProperty().alerts().size());
    assertEquals(
      "steelfish-outline",
      widget.alertProperty().alerts().getFirst().headerFont().get().family()
    );
  }
}

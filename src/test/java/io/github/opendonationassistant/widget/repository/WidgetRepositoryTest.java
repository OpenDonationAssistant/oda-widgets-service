package io.github.opendonationassistant.widget.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import io.github.opendonationassistant.widget.eventbus.WidgetChangedEventSender;
import io.github.opendonationassistant.widget.metrics.WidgetMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest(environments = "allinone")
public class WidgetRepositoryTest {

  @Inject
  WidgetDataRepository repository;

  WidgetChangedEventSender notificationSender = mock(
    WidgetChangedEventSender.class
  );

  @Test
  public void testCreatingWidget() {
    var registry = new SimpleMeterRegistry();
    var metrics = new WidgetMetrics(registry);
    new WidgetRepository(repository, notificationSender, metrics).create(
      WidgetRepository.PAYMENT_ALERTS_TYPE,
      0,
      "name",
      "testuser"
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.ADDED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          WidgetRepository.PAYMENT_ALERTS_TYPE,
          WidgetMetrics.RECIPIENT_ID_TAG,
          "testuser"
        )
        .count()
    );
  }
}

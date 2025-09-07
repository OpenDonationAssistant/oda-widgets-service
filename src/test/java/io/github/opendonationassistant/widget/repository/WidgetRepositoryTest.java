package io.github.opendonationassistant.widget.repository;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest(environments = "allinone")
public class WidgetRepositoryTest {

  @Inject
  WidgetDataRepository repository;

  WidgetChangedNotificationSender notificationSender = mock(
    WidgetChangedNotificationSender.class
  );

  @Test
  public void testCreatingWidget() {
    new WidgetRepository(repository, notificationSender).create(
      WidgetRepository.PAYMENT_ALERTS_TYPE,
      0,
      "name",
      "testuser"
    );
  }
}

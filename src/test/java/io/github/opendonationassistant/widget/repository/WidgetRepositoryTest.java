package io.github.opendonationassistant.widget.repository;

import static org.mockito.Mockito.mock;

import io.github.opendonationassistant.widget.eventbus.WidgetChangedEventSender;
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
    new WidgetRepository(repository, notificationSender).create(
      WidgetRepository.PAYMENT_ALERTS_TYPE,
      0,
      "name",
      "testuser"
    );
  }
}

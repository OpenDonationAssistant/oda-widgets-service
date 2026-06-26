package io.github.opendonationassistant.widget.eventbus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.opendonationassistant.widget.eventbus.WidgetConfigRequestListener.WidgetConfigRequest;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.Test;

@MicronautTest(environments = "allinone")
public class WidgetConfigRequestListenerTest {

  @Inject
  WidgetDataRepository dataRepository;

  @Inject
  WidgetRepository repository;

  @Test
  public void testQueryingOneWidget() {
    dataRepository.save(
      new WidgetData(
        "id",
        "payment-alerts",
        0,
        "widgetname",
        "testuser",
        Map.of(),
        true,
        false
      )
    );
    dataRepository.save(
      new WidgetData(
        "id2",
        "payment-alerts",
        0,
        "widgetname",
        "testuser",
        Map.of(),
        true,
        false
      )
    );

    WidgetConfigRequestListener listener = new WidgetConfigRequestListener(
      repository
    );

    var actual = listener.handle(new WidgetConfigRequest("id", null));
    assertNotNull(actual);
    assertTrue(actual.size() == 1);
    assertEquals("id", actual.get(0).id());
    assertEquals("payment-alerts", actual.get(0).type());
    assertEquals(0, actual.get(0).sortOrder());
    assertEquals("widgetname", actual.get(0).name());
    assertEquals("testuser", actual.get(0).ownerId());
  }

  @Test
  public void testReturnEmptyListWhenNoWidgets() {
    WidgetConfigRequestListener listener = new WidgetConfigRequestListener(
      repository
    );
    var actual = listener.handle(new WidgetConfigRequest("id", null));
    assertNotNull(actual);
    assertTrue(actual.isEmpty());
    actual = listener.handle(new WidgetConfigRequest(null, "payment-alerts"));
    assertNotNull(actual);
    assertTrue(actual.isEmpty());
    actual = listener.handle(new WidgetConfigRequest(null, null));
    assertNotNull(actual);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testQueryingAllWidgetsByType() {
    dataRepository.save(
      new WidgetData(
        "id1",
        "payment-alerts",
        0,
        "widgetname",
        "testuser",
        Map.of(),
        true,
        false
      )
    );
    dataRepository.save(
      new WidgetData(
        "id2",
        "payment-alerts",
        0,
        "widgetname",
        "testuser",
        Map.of(),
        true,
        false
      )
    );
    dataRepository.save(
      new WidgetData(
        "id2",
        "donationgoal",
        0,
        "widgetname",
        "testuser",
        Map.of(),
        true,
        false
      )
    );

    WidgetConfigRequestListener listener = new WidgetConfigRequestListener(
      repository
    );

    var actual = listener.handle(
      new WidgetConfigRequest(null, "payment-alerts")
    );
    assertNotNull(actual);
    assertTrue(actual.size() == 2);
    assertEquals("id1", actual.get(0).id());
    assertEquals("id2", actual.get(1).id());
  }
}

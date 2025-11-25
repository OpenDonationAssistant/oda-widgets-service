package io.github.opendonationassistant.widget.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.model.Widget;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(InstancioExtension.class)
public class WidgetTest {

  WidgetDataRepository repository = mock(WidgetDataRepository.class);
  WidgetChangedNotificationSender notificationSender = mock(
    WidgetChangedNotificationSender.class
  );

  @Test
  public void test(@Given WidgetData data) {
    var widget = new Widget(
      data.withConfig(Map.of()),
      repository,
      notificationSender
    );
    var expected = Map.of("properties", List.of());
    var actual = widget.getConfig();
    assertEquals(expected, actual);
  }

  @Test
  public void testReturnProperty(@Given WidgetData data) {
    var expected = property("targetname", "targetvalue");
    var widget = new Widget(
      data.withConfig(
        Map.of(
          "properties",
          List.of(expected, property("wrongname", "wrongvalue"))
        )
      ),
      repository,
      notificationSender
    );
    var actual = widget.getProperty("targetname");
    assertTrue(actual.isPresent());
    assertEquals(expected, actual.get());
  }

  @Test
  public void testReturnValue(@Given WidgetData data) {
    var widget = new Widget(
      data.withConfig(
        Map.of(
          "properties",
          List.of(
            property("targetname", "targetvalue"),
            property("wrongname", "wrongvalue")
          )
        )
      ),
      repository,
      notificationSender
    );
    var actual = widget.getValue("targetname");
    assertTrue(actual.isPresent());
    assertEquals("targetvalue", actual.get());
  }

  @Test
  public void testUpdatingPropertyValue(@Given WidgetData data) {
    var notChangedValue = property("wrongname", "wrongvalue");
    var updatedValue = Map.of("updated", "value");
    var widget = new Widget(
      data.withConfig(
        Map.of(
          "properties",
          List.of(property("targetname", "targetvalue"), notChangedValue)
        )
      ),
      repository,
      notificationSender
    );
    Widget updated = widget.updateProperty("targetname", updatedValue);
    assertEquals(updated.getValue("targetname"), Optional.of(updatedValue));
    assertEquals(
      updated.getProperty("wrongname"),
      Optional.of(notChangedValue)
    );
  }

  @Test
  public void testRemoveProperty(@Given WidgetData data) {
    var widget = new Widget(
      data.withConfig(
        Map.of(
          "properties",
          List.of(
            property("targetname", "targetvalue"),
            property("wrongname", "wrongvalue")
          )
        )
      ),
      repository,
      notificationSender
    );
    widget.removeProperty("targetname");
    assertEquals(Optional.of("wrongvalue"), widget.getValue("wrongname"));
  }

  private Map<String, Object> property(String name, Object value) {
    return Map.of("name", name, "value", value);
  }
}

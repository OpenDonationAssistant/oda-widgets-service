package io.github.opendonationassistant.widget.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import io.github.opendonationassistant.widget.repository.Widget;

public class WidgetTest {

  @Test
  public void test() {
    var widget = new Widget();
    var expected = Map.of("properties", List.of());
    var actual = widget.getConfig();
    assertEquals(expected, actual);
  }

  @Test
  public void testReturnProperty() {
    var widget = new Widget();
    var expected = property("targetname", "targetvalue");
    widget.setConfig(
      Map.of(
        "properties",
        List.of(expected, property("wrongname", "wrongvalue"))
      )
    );
    var actual = widget.getProperty("targetname");
    assertTrue(actual.isPresent());
    assertEquals(expected, actual.get());
  }

  @Test
  public void testReturnValue() {
    var widget = new Widget();
    widget.setConfig(
      Map.of(
        "properties",
        List.of(
          property("targetname", "targetvalue"),
          property("wrongname", "wrongvalue")
        )
      )
    );
    var actual = widget.getValue("targetname");
    assertTrue(actual.isPresent());
    assertEquals("targetvalue", actual.get());
  }

  @Test
  public void testUpdatingPropertyValue() {
    var widget = new Widget();
    var notChangedValue = property("wrongname", "wrongvalue");
    var updatedValue = Map.of("updated", "value");
    widget.setConfig(
      Map.of(
        "properties",
        List.of(property("targetname", "targetvalue"), notChangedValue)
      )
    );
    Widget updated = widget.updateProperty("targetname", updatedValue);
    assertEquals(updated.getValue("targetname"), Optional.of(updatedValue));
    assertEquals(
      updated.getProperty("wrongname"),
      Optional.of(notChangedValue)
    );
  }

  @Test
  public void testRemoveProperty() {
    var widget = new Widget();
    widget.setConfig(
      Map.of(
        "properties",
        List.of(
          property("targetname", "targetvalue"),
          property("wrongname", "wrongvalue")
        )
      )
    );
    widget.removeProperty("targetname");
    assertEquals(Optional.of("wrongvalue"), widget.getValue("wrongname"));
  }

  private Map<String, Object> property(String name, Object value) {
    return Map.of("name", name, "value", value);
  }
}

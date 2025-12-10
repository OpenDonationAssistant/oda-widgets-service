package io.github.opendonationassistant.widget.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.model.Update;
import io.github.opendonationassistant.widget.model.Widget;
import io.github.opendonationassistant.widget.model.WidgetProperty;
import io.github.opendonationassistant.widget.model.properties.FontProperty;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.instancio.junit.Given;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(InstancioExtension.class)
public class WidgetTest {

  private final ODALogger log = new ODALogger(this);

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
    var updated = widget.removeProperty("targetname");
    assertEquals(Optional.of("wrongvalue"), updated.getValue("wrongname"));
    assertEquals(Optional.empty(), updated.getProperty("targetname"));
  }

  private Map<String, Object> property(String name, Object value) {
    return Map.of("name", name, "value", value);
  }

  @Test
  public void testApplyingUpdateWithMatchingWidgetClass(
    @Given WidgetData data
  ) {
    var widget = new Widget(
      data.withConfig(
        Map.of(
          "properties",
          List.of(property("targetname", "value"), property("second", "second"))
        )
      ),
      repository,
      notificationSender
    );
    Function<WidgetProperty<?>, WidgetProperty<?>> function = arg -> {
      if (arg.name().equals("targetname")) {
        return new WidgetProperty<>("targetname", "updated");
      }
      return arg;
    };
    var update = new Update(
      new Update.Condition(Widget.class, null, null),
      function
    );
    var updated = widget.runUpdate(update);
    assertEquals(Optional.of("updated"), updated.getValue("targetname"));
  }

  @Test
  public void testApplyingUpdateWithMatchingPropertyClass(
    @Given WidgetData data
  ) throws IOException {
    var config = (Map<String, Object>) ObjectMapper.getDefault()
      .readValue(
        getClass().getClassLoader().getResourceAsStream("fontproperty.json"),
        Argument.ofInstance(new HashMap<String, Object>())
      );
    var widget = new Widget(
      data.withConfig(
        Map.of(
          "properties",
          List.of(property("font", config), property("untouched", "untouched"))
        )
      ),
      repository,
      notificationSender
    );
    Function<WidgetProperty<?>, WidgetProperty<?>> function = property -> {
      log.debug(
        "Running updateFn from test",
        Map.of("propertyName", property.name())
      );
      if (!(property instanceof FontProperty)) {
        return property;
      }
      var font = new HashMap<>(((FontProperty) property).value());
      var width = (Integer) font.getOrDefault("shadowWidth", 0);
      var color = font.getOrDefault("shadowColor", "#000000");
      var x = font.getOrDefault("shadowOffsetX", 0);
      var y = font.getOrDefault("shadowOffsetY", 0);
      font.remove("shadowWidth");
      font.remove("shadowColor");
      font.remove("shadowOffsetX");
      font.remove("shadowOffsetY");
      if (width > 0) {
        font.put(
          "shadows",
          List.of(Map.of("blur", width, "color", color, "x", x, "y", y))
        );
      }
      return WidgetProperty.of(property.name(), font);
    };

    var update = new Update(
      new Update.Condition(null, FontProperty.class, null),
      function
    );
    var updated = widget.runUpdate(update);
    log.debug(
      "Widget has been updated",
      Map.of("updatedConfig", updated.getConfig())
    );
    var font = (Map<String, Object>) updated
      .getProperty("font")
      .get()
      .get("value");
    assertNotNull(font);
    assertEquals(
      List.of(Map.of("blur", 3, "color", "#0C0C0C", "x", 1, "y", 2)),
      font.get("shadows")
    );
    assertEquals(Optional.of("untouched"), updated.getValue("untouched"));
  }

  @Test
  public void testSkippingUpdateBecauseOfWrongWidgetClass(
    @Given WidgetData data
  ) {
    var widget = new Widget(
      data.withConfig(
        Map.of("properties", List.of(property("targetname", "value")))
      ),
      repository,
      notificationSender
    );
    Function<WidgetProperty<?>, WidgetProperty<?>> function = arg -> {
      if (arg.name().equals("targetname")) {
        return new WidgetProperty<>("targetname", "updated");
      }
      return arg;
    };
    var update = new Update(
      new Update.Condition(String.class, null, null),
      function
    );
    var updated = widget.runUpdate(update);
    assertEquals(Optional.of("value"), updated.getValue("targetname"));
  }
}

package io.github.opendonationassistant.widget.model.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FontPropertyTest {

  // @Test
  // public void testRunUpdate() throws IOException {
  //   var config = (Map<String, Object>) ObjectMapper.getDefault()
  //     .readValue(
  //       getClass().getClassLoader().getResourceAsStream("fontproperty.json"),
  //       Argument.ofInstance(new HashMap<String, Object>())
  //     );
  //   var property = new FontProperty("font", config);
  //   property.runUpdate(it -> {
  //     var width = (Integer) it.getOrDefault("shadowWidth", 0);
  //     var color = it.getOrDefault("shadowColor", "#000000");
  //     var x = it.getOrDefault("shadowOffsetX", 0);
  //     var y = it.getOrDefault("shadowOffsetY", 0);
  //     it.remove("shadowWidth");
  //     it.remove("shadowColor");
  //     it.remove("shadowOffsetX");
  //     it.remove("shadowOffsetY");
  //     if (width > 0) {
  //       it.put(
  //         "shadows",
  //         List.of(Map.of("blur", width, "color", color, "x", x, "y", y))
  //       );
  //     }
  //   });
  //   assertEquals(
  //     property.value().get("shadows"),
  //     List.of(Map.of("blur", 3, "color", "#0C0C0C", "x", 1, "y", 2))
  //   );
  // }
}

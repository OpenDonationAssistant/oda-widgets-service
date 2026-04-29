package io.github.opendonationassistant.widget;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.widget.api.UpdateApi;
import io.github.opendonationassistant.widget.model.Update;
import io.github.opendonationassistant.widget.model.WidgetProperty;
import io.github.opendonationassistant.widget.model.properties.AlignmentProperty;
import io.github.opendonationassistant.widget.model.properties.FontProperty;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/update")
public class UpdateController implements UpdateApi {

  private final WidgetRepository widgetRepository;
  private final ODALogger log = new ODALogger(this);

  @Inject
  public UpdateController(WidgetRepository repository) {
    this.widgetRepository = repository;
  }

  public void runUpdate() {
    // widgetRepository.updateWidget(widget -> {
    //   return widget.runUpdate(fontUpdate()).runUpdate(alignmentUpdate());
    // });
    widgetRepository.updateWidget(widget -> {
      if (widget.type().equals("media")) {
        var cost = widget.getValue("songRequestCost").orElse(100);
        widget
          .addProperty("maxLen", Map.<String, Object>of("limitLen", false))
          .addProperty(
            "tarification",
            Map.<String, Object>of("method", "perLink", "cost", cost)
          )
          .save("migration");
      }
      return widget;
    });
  }

  private Update mediaWidgetUpdate() {
    return new Update(
      new Update.Condition(null, WidgetProperty.class, null),
      value -> ((String) value).toLowerCase()
    );
  }

  private Update alignmentUpdate() {
    return new Update(
      new Update.Condition(null, AlignmentProperty.class, null),
      value -> ((String) value).toLowerCase()
    );
  }

  private Update fontUpdate() {
    return new Update(
      new Update.Condition(null, FontProperty.class, null),
      value -> {
        @SuppressWarnings("unchecked")
        var font = new HashMap<>((Map<String, Object>) value);
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
        return font;
      }
    );
  }
}

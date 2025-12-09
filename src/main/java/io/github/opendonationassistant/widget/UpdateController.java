package io.github.opendonationassistant.widget;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.widget.model.Update;
import io.github.opendonationassistant.widget.model.WidgetProperty;
import io.github.opendonationassistant.widget.model.properties.FontProperty;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Controller("/update")
public class UpdateController {

  private final WidgetRepository widgetRepository;
  private final ODALogger log = new ODALogger(this);

  @Inject
  public UpdateController(WidgetRepository repository) {
    this.widgetRepository = repository;
  }

  @Post
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void runUpdate() {
    Function<WidgetProperty<?>, WidgetProperty<?>> function = property -> {
      log.debug("Running updateFn", Map.of("propertyName", property.name()));
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
    widgetRepository.updateWidget(
      WidgetRepository.DONATION_GOAL_TYPE,
      widget -> {
        return widget.runUpdate(update);
      }
    );
  }
}

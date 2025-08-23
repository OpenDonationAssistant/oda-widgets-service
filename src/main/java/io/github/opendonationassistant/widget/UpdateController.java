package io.github.opendonationassistant.widget;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/update")
public class UpdateController {

  private final WidgetRepository widgetRepository;
  private final ODALogger log = new ODALogger(this);

  public static final String DEFAULT_COLOR = "#684aff";
  public static final String DEFAULT_BACKGROUND_COLOR = "rgba(0,0,0,0)";

  @Inject
  public UpdateController(WidgetRepository repository) {
    this.widgetRepository = repository;
  }

  @Post
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void runUpdate() {
    log.info("Running update", Map.of("type", WidgetRepository.PAYMENT_ALERTS_TYPE));
    widgetRepository.updateWidget(
      WidgetRepository.PAYMENT_ALERTS_TYPE,
      widget -> {
        log.info("Updating widget: {}", Map.of("widget", widget);
        var alerts = (List<Map<String, Object>>) widget
          .getConfig()
          .get("alerts");
        if (alerts == null) {
          return widget;
        }
        final List<Map<String, Object>> updatedAlerts = alerts
          .stream()
          .map(alert -> {
            log.debug("Updating alert: {}", alert);
            var amount =
              ((Map<String, Object>) alert.get("trigger")).get("amount");
            var trigger = new HashMap<String, Object>();
            trigger.put("type", "at-least-donation-amount");
            trigger.put("min", amount);
            alert.put("triggers", List.of(trigger));
            var id = alert.get("id");
            if (id == null || id.equals("")) {
              alert.put("id", UUID.randomUUID().toString());
            }
            return alert;
          })
          .toList();
        var property = new HashMap<String, Object>();
        property.put("name", "alerts");
        property.put("value", updatedAlerts);
        log.info("created property:{}", property);
        var properties = (List<Map<String, Object>>) widget
          .getConfig()
          .get("properties");
        var updatedProperties = new ArrayList<Map<String, Object>>();
        if (properties != null) {
          updatedProperties.addAll(properties);
        }
        updatedProperties.add(property);
        log.info("Updated properties", Map.of("updatedProperties", updatedProperties));
        widget.setConfig(Map.of("properties", updatedProperties));
        log.info(
          "Trying to save",
          Map.of("id", widget.getId(),
          "ownerId", widget.getOwnerId())
        );
        return widget;
      }
    );
    log.info("Update finished", Map.of());
  }
}

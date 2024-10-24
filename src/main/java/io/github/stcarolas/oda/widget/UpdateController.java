package io.github.stcarolas.oda.widget;

import static io.github.stcarolas.oda.widget.Utils.*;

import io.github.stcarolas.oda.widget.domain.Widget;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/update")
public class UpdateController {

  private final WidgetRepository widgetRepository;
  private final Logger log = LoggerFactory.getLogger(UpdateController.class);

  public static final String DEFAULT_COLOR = "#684aff";
  public static final String DEFAULT_BACKGROUND_COLOR = "rgba(0,0,0,0)";
  public static final String PAYMENT_ALERTS_TYPE = "payment-alerts";
  public static final String PLAYER_INFO_TYPE = "player-info";
  public static final String DONATION_TIMER_TYPE = "donation-timer";
  public static final String DONATION_GOAL_TYPE = "donationgoal";
  public static final String REEL_TYPE = "reel";
  public static final String DONATERS_TOP_LIST_TYPE = "donaters-top-list";

  @Inject
  public UpdateController(WidgetRepository repository) {
    this.widgetRepository = repository;
  }

  @Post
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void runUpdate() {
    log.info("Running update");
    log.info("Updating {}", PAYMENT_ALERTS_TYPE);
    widgetRepository.updateWidget(
      PAYMENT_ALERTS_TYPE,
      widget -> {
        log.info("Updating widget: {}", widget);
        var alerts = (List<Map<String, Object>>) widget
          .getConfig()
          .get("alerts");
        if (alerts == null){
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
        var updatedProperties = new ArrayList<Map<String,Object>>();
        if (properties != null) {
          updatedProperties.addAll(properties);
        }
        updatedProperties.add(property);
        log.info("Updated properties: {}", updatedProperties);
        widget.setConfig(Map.of("properties", updatedProperties));
        log.info("trying to save widget: {}, owner: {}", widget.getId(), widget.getOwnerId());
        return widget;
      }
    );
    log.info("Update finished");
  }
}

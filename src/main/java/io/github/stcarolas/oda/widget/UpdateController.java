package io.github.stcarolas.oda.widget;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/update")
public class UpdateController {

  private final WidgetRepository widgetRepository;
  private final Logger log = LoggerFactory.getLogger(UpdateController.class);

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

  private Optional<Object> findProperty(List props, String name) {
    return props
      .stream()
      .filter(prop -> name.equals(((Map<String, Object>) prop).get("name")))
      .findFirst();
  }

  private List<?> removeProperty(List<?> props, String name) {
    return props
      .stream()
      .filter(prop -> !name.equals(((Map<String, Object>) prop).get("name")))
      .toList();
  }

  private Map<String, Object> newFontProperty(
    int size,
    String font,
    String color
  ) {
    Map<String, Object> value = new HashMap<>();
    value.put("size", size);
    value.put("color", color);
    value.put("family", font);
    value.put("italic", false);
    value.put("weight", false);
    value.put("underline", false);
    value.put("animation", "none");
    value.put("animationType", "entire");
    value.put("shadowColor", "#000000");
    value.put("shadowWidth", 0);
    value.put("shadowOffsetX", 0);
    value.put("shadowOffsetY", 0);
    return value;
  }

  @Post
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void runUpdate() {
    log.info("Running update");
    widgetRepository
      .findAll()
      .stream()
      .filter(widget -> PAYMENT_ALERTS_TYPE.equals(widget.getType()))
      .forEach(widget -> {
        Map<String, Object> config = widget.getConfig();
        List alerts = (List) config.get("alerts");
        if (alerts == null) {
          return;
        }
        List updatedAlerts = alerts
          .stream()
          .map(alert -> {
            List props = new ArrayList((List) ((Map<String, Object>) alert).get("properties"));
            props = removeProperty(props, "nicknameFont");
            props = removeProperty(props, "nicknameFontSize");
            props = removeProperty(props, "headerColor");
            if (findProperty(props, "audio-volume").isEmpty()) {
              var audioVolumeProperty = new HashMap<String, Object>();
              audioVolumeProperty.put("name", "audio-volume");
              audioVolumeProperty.put("value", 100);
              props.add(audioVolumeProperty);
            }
            ((Map<String, Object>) alert).put("properties", props);
            return alert;
          })
          .toList();
        config.put("alerts", updatedAlerts);
        widget.setConfig(config);
        widgetRepository.update(widget);
      });
    log.info("Update finished");
    // .filter(widget -> PAYMENT_ALERTS_TYPE.equals(widget.getType()))
    // .forEach(widget -> {
    //   Map<String, Object> config = widget.getConfig();
    //   List<Object> alerts = (List<Object>)config.get("alerts");
    //   alerts.forEach(alert -> {
    //   });
    // });
  }
}

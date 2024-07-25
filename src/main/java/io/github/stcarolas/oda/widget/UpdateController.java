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

  private Optional<Map<String, Object>> findProperty(List props, String name) {
    return props
      .stream()
      .filter(prop -> name.equals(((Map<String, Object>) prop).get("name")))
      .findFirst();
  }

  private <T> Optional<T> findValue(List props, String name) {
    return findProperty(props, name)
      .map(prop -> prop.get("value"))
      .map(value -> (T) value);
  }

  private List<?> removeProperty(List<?> props, String name) {
    return props
      .stream()
      .filter(prop -> !name.equals(((Map<String, Object>) prop).get("name")))
      .toList();
  }

  private Map<String,Object> defaultBorderValue(){
    var border = new HashMap<String, Object>();
    border.put("width",0);
    border.put("color","#FFFFFF");
    border.put("type","solid");
    return border;
  }

  @Post
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void runUpdate() {
    log.info("Running update");
    widgetRepository
      .findAll()
      .stream()
      .filter(widget -> REEL_TYPE.equals(widget.getType()))
      .forEach(widget -> {
        Map<String, Object> config = widget.getConfig();
        List props = new ArrayList<Object>();
        props.addAll((List) config.get("properties"));
        if (props == null || props.isEmpty()) {
          return;
        }
        Optional<String> borderColor = findValue(props, "borderColor");
        Optional<Integer> borderWidth = findValue(props, "borderWidth");
        if (borderColor.isEmpty() || borderWidth.isEmpty()) {
          return;
        }
        Map<String, Object> borderValue = defaultBorderValue();
        borderValue.put("color",borderColor.get());
        borderValue.put("width",borderWidth.get());

        var borderProperty = new HashMap<String, Object>();
        borderProperty.put("name", "cardBorder");

        var borderPropertyValue = new HashMap<String, Object>();
        borderPropertyValue.put("isSame", true);
        borderPropertyValue.put("bottom",defaultBorderValue());
        borderPropertyValue.put("left",defaultBorderValue());
        borderPropertyValue.put("right",defaultBorderValue());
        borderPropertyValue.put("top",borderValue);

        borderProperty.put("value", borderPropertyValue);
        props.add(borderProperty);
        config.put("properties", props);
        widget.setConfig(config);
        widgetRepository.update(widget);
      });
    log.info("Update finished");
  }
}

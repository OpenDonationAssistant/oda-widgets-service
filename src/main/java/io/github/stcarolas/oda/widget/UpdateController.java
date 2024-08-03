package io.github.stcarolas.oda.widget;

import static io.github.stcarolas.oda.widget.Utils.*;

import io.github.stcarolas.oda.widget.domain.Widget;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

  final Function<Widget, Widget> timerMigration = widget -> {
    var updatedHeaderFont = updateFontColor(widget, "titleFont");
    var updated = widget.updateProperty("titleFont", updatedHeaderFont);
    return updated;
  };

  final Function<Widget, Widget> goalMigration = widget -> {
    var updatedHeaderFont = updateFontColor(widget, "descriptionFont");
    var updatedAmountFont = updateFontColor(widget, "amountFont");
    var updatedSelectionColor = updateColor(
      widget,
      "backgroundColor",
      DEFAULT_BACKGROUND_COLOR
    );
    var updatedFilledColor = updateColor(
      widget,
      "filledColor",
      DEFAULT_BACKGROUND_COLOR
    );
    var updated = widget
      .removeProperty("background")
      .updateProperty("backgroundColor", updatedSelectionColor)
      .updateProperty("filledColor", updatedFilledColor)
      .updateProperty("descriptionFont", updatedHeaderFont)
      .updateProperty("amountFont", updatedAmountFont);
    return updated;
  };

  final Function<Widget, Widget> reelMigration = widget -> {
    var updatedHeaderFont = updateFontColor(widget, "titleFont");
    var updatedSelectionColor = updateColor(
      widget,
      "selectionColor",
      DEFAULT_BACKGROUND_COLOR
    );
    var updated = widget
      .updateProperty("selectionColor", updatedSelectionColor)
      .updateProperty("titleFont", updatedHeaderFont);
    return updated;
  };

  final Function<Widget, Widget> toplistMigration = widget -> {
    var updatedHeaderFont = updateFontColor(widget, "headerFont");
    var updatedMessageFont = updateFontColor(widget, "messageFont");
    var updatedTitleColor = updateColor(
      widget,
      "titleBackgroundColor",
      DEFAULT_BACKGROUND_COLOR
    );
    var updatedMessageColor = updateColor(
      widget,
      "backgroundColor",
      DEFAULT_BACKGROUND_COLOR
    );
    var updated = widget
      .updateProperty("titleBackgroundColor", updatedTitleColor)
      .updateProperty("backgroundColor", updatedMessageColor)
      .updateProperty("headerFont", updatedHeaderFont)
      .updateProperty("messageFont", updatedMessageFont);
    return updated;
  };

  final Function<Widget, Widget> playerInfoMigration = widget -> {
    var updatedFontSetting = updateFontColor(widget, "titleFont");
    var updated = widget.updateProperty("titleFont", updatedFontSetting);
    return updated;
  };

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
        var alerts = (List<Map<String, Object>>) widget
          .getConfig()
          .get("alerts");
        final List<Map<String, Object>> updatedAlerts = alerts.stream().map(alert -> {
          final List<Map<String, Object>> properties =
            ((List<Object>) alert.get("properties")).stream()
              .map(it -> (Map<String, Object>) it)
              .toList();
          var updatedProperties = properties
            .stream()
            .filter(Objects::nonNull)
            .filter(it ->  Objects.nonNull(it.get("value")))
            .map(it -> {
              if ("headerFont".equals(it.get("name"))) {
                var value = (Map<String,Object>)it.get("value");
                try{
                  var color = (String) value.getOrDefault("color",DEFAULT_COLOR);
                  var  updatedValue  = new HashMap<String,Object>();
                  updatedValue.putAll(value);
                  updatedValue.put("color",color(color));
                  var updated = new HashMap<String, Object>();
                  updated.putAll(it);
                  updated.put("value", updatedValue);
                  return updated;
                } catch(Exception e){
                  e.printStackTrace();
                }
              }
              if ("font".equals(it.get("name"))) {
                var value = (Map<String,Object>)it.get("value");
                try{
                  var color = (String) value.getOrDefault("color",DEFAULT_COLOR);
                  var  updatedValue  = new HashMap<String,Object>();
                  updatedValue.putAll(value);
                  updatedValue.put("color",color(color));
                  var updated = new HashMap<String, Object>();
                  updated.putAll(it);
                  updated.put("value", updatedValue);
                  return updated;
                } catch(Exception e){
                  e.printStackTrace();
                }
              }
              return it;
            })
            .toList();
          var updatedAlert = new HashMap<String, Object>();
          updatedAlert.putAll(alert);
          updatedAlert.put("properties", updatedProperties);
          return (Map<String,Object>)updatedAlert;
        }).toList();
        widget.setConfig(Map.of("alerts", updatedAlerts));
        return widget;
      }
    );
    log.info("Update finished");
  }

  private HashMap<String, Object> updateFontColor(Widget widget, String name) {
    return updateFontColor(widget, name, "#684aff");
  }

  private Map<String, Object> updateColor(
    Widget widget,
    String name,
    String defaultColor
  ) {
    Optional<String> color = widget.getValue(name);
    return color(color.orElse(defaultColor));
  }

  private HashMap<String, Object> updateFontColor(
    Widget widget,
    String name,
    String defaultColor
  ) {
    Optional<Map<String, Object>> headerFont = widget.getValue(name);
    String color = headerFont
      .flatMap(it -> Optional.ofNullable(it.get("color")))
      .map(it -> (String) it)
      .orElse(defaultColor);
    var updatedFontSetting = new HashMap<String, Object>();
    headerFont.ifPresent(updatedFontSetting::putAll);
    updatedFontSetting.put("color", color(color));
    return updatedFontSetting;
  }
}

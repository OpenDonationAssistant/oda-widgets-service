package io.github.stcarolas.oda.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/update")
public class UpdateController {

  private final WidgetRepository widgetRepository;

  public static final String PAYMENT_ALERTS_TYPE = "payment-alerts";
  public static final String PLAYER_INFO_TYPE = "player-info";
  public static final String DONATION_TIMER_TYPE = "donation-timer";
  public static final String DONATION_GOAL_TYPE = "donationgoal";

  @Inject
  public UpdateController(WidgetRepository repository) {
    this.widgetRepository = repository;
  }


  private Optional<Object> findProperty(List props, String name){
      return props.stream().filter(prop -> 
        name.equals(((Map<String, Object>)prop).get("name"))
      ).findFirst();

  }

  private Map<String, Object> newFontProperty(int size, String font, String color){
    Map<String, Object> value = new HashMap<>();
    value.put("size",size);
    value.put("color",color);
    value.put("family", font);
    value.put("italic",false);
    value.put("weight",false);
    value.put("underline",false);
    value.put("animation","none");
    value.put("animationType","entire");
    value.put("shadowColor","#000000");
    value.put("shadowWidth",0);
    value.put("shadowOffsetX",0);
    value.put("shadowOffsetY",0);
    return value;
  }

  @Post
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void runUpdate() {
    widgetRepository
      .findAll()
      .stream()
      .filter(widget -> DONATION_GOAL_TYPE.equals(widget.getType()))
      .forEach(widget -> {
        Map<String, Object> config = widget.getConfig();
        List props = (List) config.get("properties");
        int fontSize = findProperty(props, "titleFontSize")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (Integer)Integer.parseInt(it.toString()))
          .orElse(24);
        String fontName = findProperty(props, "titleFont")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (String)it)
          .orElse("Roboto");
        String color = findProperty(props, "titleColor")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (String)it)
          .orElse("#ffffff");
        Map<String, Object> newFont = new HashMap();
        newFont.put("name","descriptionFont");
        newFont.put("value", newFontProperty(fontSize, fontName, color));
        props.add(newFont);
        int filledFontSize = findProperty(props, "filledFontSize")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (Integer)Integer.parseInt(it.toString()))
          .orElse(24);
        String filledFontName = findProperty(props, "filledFont")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (String)it)
          .orElse("Roboto");
        String filledColor = findProperty(props, "filledTextColor")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (String)it)
          .orElse("#ffffff");
        Map<String, Object> filledFont = new HashMap();
        newFont.put("name","amountFont");
        newFont.put("value", newFontProperty(filledFontSize, filledFontName, filledColor));
        props.add(filledFont);
        config.put("properties", props);
        widget.setConfig(config);
        widgetRepository.update(widget);
      });
      // .filter(widget -> PAYMENT_ALERTS_TYPE.equals(widget.getType()))
      // .forEach(widget -> {
      //   Map<String, Object> config = widget.getConfig();
      //   List<Object> alerts = (List<Object>)config.get("alerts");
      //   alerts.forEach(alert -> {
      //   });
      // });
  }
}

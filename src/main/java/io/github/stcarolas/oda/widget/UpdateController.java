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
  public static final String REEL_TYPE = "reel";
  public static final String DONATERS_TOP_LIST_TYPE = "donaters-top-list";

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
      .filter(widget -> DONATERS_TOP_LIST_TYPE.equals(widget.getType()))
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
        newFont.put("name","headerFont");
        newFont.put("value", newFontProperty(fontSize, fontName, color));
        props.add(newFont);
        int messageFontSize = findProperty(props, "fontSize")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (Integer)Integer.parseInt(it.toString()))
          .orElse(24);
        String messageFontName = findProperty(props, "font")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (String)it)
          .orElse("Roboto");
        String messageColor = findProperty(props, "color")
          .map(it -> ((Map<String, Object>)it).get("value"))
          .map(it -> (String)it)
          .orElse("#ffffff");
        Map<String, Object> messageFont = new HashMap();
        messageFont.put("name","messageFont");
        messageFont.put("value", newFontProperty(messageFontSize, messageFontName, messageColor));
        props.add(messageFont);
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

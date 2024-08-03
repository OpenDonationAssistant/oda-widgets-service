package io.github.stcarolas.oda.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {

  public static Map<String, Object> defaultBorderValue() {
    var border = new HashMap<String, Object>();
    border.put("width", 0);
    border.put("color", "#FFFFFF");
    border.put("type", "solid");
    return border;
  }

  public static Map<String,  Object> color(String value){
    var  colorValue = new HashMap<String, Object>();
    colorValue.put("color", value);

    var  colors = new  ArrayList<Map<String, Object>>();
    colors.add(colorValue);

    var  setting =  new HashMap<String, Object>();
    setting.put("colors", colors);
    setting.put("angle",0);
    setting.put("gradient", false);
    setting.put("repeating", false);
    setting.put("gradientType", 0);

    return setting;
  }
}

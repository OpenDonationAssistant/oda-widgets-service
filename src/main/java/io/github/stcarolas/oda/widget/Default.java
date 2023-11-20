package io.github.stcarolas.oda.widget;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vavr.API;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Default {

  static ObjectMapper mapper = new ObjectMapper();

  static Map<String, Widget> configs = API.<String,String>Map()
  .put("media",
  """
    { 
      "config":{
        "properties": [
          {
            "name": "playlistSongTitleFontSize",
            "displayName": "Размер шрифта в названии песни в плейлисте",
            "value": "16"
          },
          {
            "name": "playlistNicknameFontSize",
            "displayName": "Размер шрифта в имени заказчика в плейлисте",
            "value": "16"
          }
        ]
      }
    }
  """)
  .put("payments",
  """
    { 
      "config":{
        "properties": [
          {
            "name": "nicknameFontSize",
            "displayName": "Размер шрифта в имени донатера",
            "value": "28"
          },
          {
            "name": "messageFontSize",
            "displayName": "Размер шрифта в сообщении",
            "value": "24"
          }
        ]
      }
    }
  """)
  .put("player-info",
  """
    { 
      "config":{
        "properties": [
          {
            "name": "fontSize",
            "displayName": "Размер шрифта",
            "value": "24"
          }
        ]
      }
    }
  """)
  .put("donaters-top-list",
  """
    { 
      "config":{
        "properties": [
          {
            "name": "fontSize",
            "displayName": "Размер шрифта",
            "value": "24"
          },
          {
            "name": "font",
            "displayName": "Шрифт",
            "type":"fontselect",
            "value": "Roboto"
          },
          {
            "name": "type",
            "displayName": "Тип виджета",
            "value": "All"
          },
          {
            "name": "period",
            "displayName": "Период",
            "value": "Month"
          },
          {
            "name": "topsize",
            "displayName": "Кол-во в топе",
            "value": "3"
          }
        ]
      }
    }
  """)
  .put("donation-timer",
  """
    {
      "config":{
        "properties": [
          {
            "name": "fontSize",
            "displayName": "Размер шрифта",
            "value": "24"
          },
          {
            "name": "font",
            "displayName": "Шрифт",
            "type":"fontselect",
            "value": "Roboto"
          },
          {
            "name": "text",
            "displayName": "Текст",
            "type":"string",
            "value": "Без донатов уже <time>"
          }
        ]
      }
    }
  """)
  .mapValues(
    config -> Try.of(
      () -> mapper.readValue(config, Widget.class)
    )
    .onFailure(error -> log.error("some error: " + error.getMessage()))
    .getOrElse(() -> new Widget())
  );

  
}

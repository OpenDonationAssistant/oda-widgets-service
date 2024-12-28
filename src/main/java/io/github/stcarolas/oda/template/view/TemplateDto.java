package io.github.stcarolas.oda.template.view;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import java.util.Map;

@Serdeable
public class TemplateDto {

  public String id;
  public String showcase;
  public List<Map<String, Object>> properties;

  public TemplateDto(
    String id,
    String showcase,
    List<Map<String, Object>> properties
  ) {
    this.id = id;
    this.showcase = showcase;
    this.properties = properties;
  }

  public String getId() {
    return id;
  }

  public String getShowcase() {
    return showcase;
  }

  public List<Map<String, Object>> getProperties() {
    return properties;
  }
}

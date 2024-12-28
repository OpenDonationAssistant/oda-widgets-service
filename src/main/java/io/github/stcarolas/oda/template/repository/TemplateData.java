package io.github.stcarolas.oda.template.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.DataType;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import java.util.Map;

@Serdeable
@MappedEntity("template")
public class TemplateData {

  @Id
  private String id;

  private String ownerId;
  private String widgetType;
  private String showcase;

  @MappedProperty(type = DataType.JSON)
  private List<Map<String, Object>> properties;

  public TemplateData(
    String id,
    String ownerId,
    String widgetType,
    String showcase,
    List<Map<String, Object>> properties
  ) {
    this.id = id;
    this.ownerId = ownerId;
    this.widgetType = widgetType;
    this.showcase = showcase;
    this.properties = properties;
  }

  public String getShowcase() {
    return showcase;
  }

  public List<Map<String, Object>> getProperties() {
    return properties;
  }

  public String getId() {
    return id;
  }

  public String getWidgetType() {
    return widgetType;
  }

  public String getOwnerId() {
    return ownerId;
  }
}

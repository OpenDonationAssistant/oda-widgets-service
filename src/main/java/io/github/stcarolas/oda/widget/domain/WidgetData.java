package io.github.stcarolas.oda.widget.domain;

import java.util.Map;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.DataType;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@MappedEntity("widget")
public class WidgetData {

  @Id
  private String id;

  @MappedProperty(value = "widget_type")
  private String type;

  @MappedProperty(value = "sort_order")
  private Integer sortOrder;

  @MappedProperty(value = "display_name")
  private String name;

  @MappedProperty(value = "owner_id")
  private String ownerId;

  @MappedProperty(type = DataType.JSON)
  private Map<String, Object> config;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public Map<String, Object> getConfig() {
    return config;
  }

  public void setConfig(Map<String, Object> config) {
    this.config = config;
  }

  @Override
  public String toString() {
    return (
      "{\"_type\"=\"Widget\",\"id\"=\"" +
      id +
      "\", type\"=\"" +
      type +
      "\", sortOrder\"=\"" +
      sortOrder +
      "\", name\"=\"" +
      name +
      "\", ownerId\"=\"" +
      ownerId +
      "\", config\"=\"" +
      config +
      "}"
    );
  }

}

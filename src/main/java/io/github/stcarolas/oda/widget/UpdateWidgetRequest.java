package io.github.stcarolas.oda.widget;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Serdeable
public class UpdateWidgetRequest {

  private String name;
  private java.util.Map<String, Object> config;
  private Integer sortOrder;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public java.util.Map<String, Object> getConfig() {
    return config;
  }
  public void setConfig(java.util.Map<String, Object> config) {
    this.config = config;
  }
  public Integer getSortOrder() {
    return sortOrder;
  }
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

}

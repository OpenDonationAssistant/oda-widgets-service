package io.github.stcarolas.oda.widget;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class NewWidgetRequest {

  private String type;
  private Integer sortOrder;

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
}

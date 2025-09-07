package io.github.opendonationassistant.widget;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;

@Serdeable
public class UpdateWidgetRequest {

  @Nullable
  private String name;

  @Nullable
  private java.util.Map<String, Object> config;

  @Nullable
  private Integer sortOrder;

  public @Nullable String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public @Nullable java.util.Map<String, Object> getConfig() {
    return config;
  }

  public void setConfig(java.util.Map<String, Object> config) {
    this.config = config;
  }

  public @Nullable Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
}

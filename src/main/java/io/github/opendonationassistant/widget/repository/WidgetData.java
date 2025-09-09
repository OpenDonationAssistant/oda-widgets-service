package io.github.opendonationassistant.widget.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.DataType;
import io.micronaut.serde.annotation.Serdeable;
import java.util.Map;

@Serdeable
@MappedEntity("widget")
public record WidgetData(
  @Id String id,
  @MappedProperty(value = "widget_type") String type,
  @MappedProperty(value = "sort_order") Integer sortOrder,
  @MappedProperty(value = "display_name") String name,
  @MappedProperty(value = "owner_id") String ownerId,
  @MappedProperty(type = DataType.JSON) Map<String, Object> config,
  @MappedProperty(value = "enabled") boolean enabled,
  @MappedProperty(value = "deleted") boolean deleted
) {
  public WidgetData withName(String value) {
    return new WidgetData(
      id,
      type,
      sortOrder,
      value,
      ownerId,
      config,
      enabled,
      deleted
    );
  }

  public WidgetData withConfig(Map<String, Object> config) {
    return new WidgetData(
      id,
      type,
      sortOrder,
      name,
      ownerId,
      config,
      enabled,
      deleted
    );
  }

  public WidgetData withEnabled(boolean enabled) {
    return new WidgetData(
      id,
      type,
      sortOrder,
      name,
      ownerId,
      config,
      enabled,
      deleted
    );
  }

  public WidgetData withDeleted(boolean deleted) {
    return new WidgetData(
      id,
      type,
      sortOrder,
      name,
      ownerId,
      config,
      enabled,
      deleted
    );
  }
  public WidgetData withSortOrder(Integer sortOrder) {
    return new WidgetData(
      id,
      type,
      sortOrder,
      name,
      ownerId,
      config,
      enabled,
      deleted
    );
  }
}

package io.github.opendonationassistant.template.repository;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.DataType;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import java.util.Map;

@Serdeable
@MappedEntity("template")
public record TemplateData(
  @Id String id,
  String ownerId,
  String widgetType,
  String showcase,
  @MappedProperty(type = DataType.JSON) List<Map<String, Object>> properties
) {}

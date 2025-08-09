package io.github.opendonationassistant.template.view;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import java.util.Map;

@Serdeable
public record TemplateDto(
  String id,
  String showcase,
  List<Map<String, Object>> properties,
  String recipientId
) {}

package io.github.opendonationassistant.template.eventbus;

import io.github.opendonationassistant.events.HasRecipientId;
import io.github.opendonationassistant.template.repository.TemplateData;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record DeletedTemplateEvent(
  TemplateData data,
  String recipientId
) implements HasRecipientId {}

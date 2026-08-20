package io.github.opendonationassistant.template;

import io.github.opendonationassistant.template.eventbus.DeletedTemplateEvent;
import io.github.opendonationassistant.template.repository.TemplateData;
import io.github.opendonationassistant.template.repository.TemplateDataRepository;
import io.github.opendonationassistant.template.view.TemplateDto;
import io.github.opendonationassistant.rabbit.RabbitClient;
import java.io.IOException;

public class Template {

  private TemplateData data;
  private TemplateDataRepository repository;
  private RabbitClient eventSender;

  public Template(
    TemplateData data,
    TemplateDataRepository repository,
    RabbitClient eventSender
  ) {
    this.data = data;
    this.repository = repository;
    this.eventSender = eventSender;
  }

  public void save() {
    repository.update(data);
  }

  public void delete() throws IOException {
    var updatedData = data.withDeleted(true);
    repository.update(updatedData);
    this.data = updatedData;
    eventSender.sendEvent(new DeletedTemplateEvent(data, data.ownerId()));
  }

  public TemplateDto asDto() {
    return new TemplateDto(
      data.id(),
      data.showcase(),
      data.properties(),
      data.ownerId()
    );
  }
}

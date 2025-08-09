package io.github.opendonationassistant.template;

import io.github.opendonationassistant.template.repository.TemplateData;
import io.github.opendonationassistant.template.repository.TemplateDataRepository;
import io.github.opendonationassistant.template.view.TemplateDto;

public class Template {

  private TemplateData data;
  private TemplateDataRepository repository;

  public Template(TemplateData data, TemplateDataRepository repository) {
    this.data = data;
    this.repository = repository;
  }

  public void save() {
    repository.update(data);
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

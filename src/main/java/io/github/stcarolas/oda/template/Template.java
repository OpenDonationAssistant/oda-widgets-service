package io.github.stcarolas.oda.template;

import io.github.stcarolas.oda.template.repository.TemplateData;
import io.github.stcarolas.oda.template.repository.TemplateDataRepository;
import io.github.stcarolas.oda.template.view.TemplateDto;

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
      data.getId(),
      data.getShowcase(),
      data.getProperties()
    );
  }
}

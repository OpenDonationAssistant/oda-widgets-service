package io.github.stcarolas.oda.template.commands;

import io.github.stcarolas.oda.template.repository.TemplateRepository;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;
import java.util.Map;

@Serdeable
public class CreateTemplate {

  private String widgetType;
  private String showcase;
  private List<Map<String, Object>> properties;

  public CreateTemplate(
    String widgetType,
    String showcase,
    List<Map<String, Object>> properties
  ) {
    this.widgetType = widgetType;
    this.showcase = showcase;
    this.properties = properties;
  }

  public void execute(TemplateRepository repository){
    repository.create(widgetType, showcase, properties);
  }
}

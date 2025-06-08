package io.github.opendonationassistant.template.view;

import io.github.opendonationassistant.template.Template;
import io.github.opendonationassistant.template.repository.TemplateRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import java.util.List;

@Controller("/templates")
public class TemplateController {

  private TemplateRepository repository;

  @Inject
  public TemplateController(TemplateRepository repository) {
    this.repository = repository;
  }

  @Get
  @Secured(SecurityRule.IS_ANONYMOUS)
  public List<TemplateDto> listTemplates(
    @QueryValue("widget") String widgetType
  ) {
    return repository
      .listSystem(widgetType)
      .stream()
      .map(Template::asDto)
      .toList();
  }
}

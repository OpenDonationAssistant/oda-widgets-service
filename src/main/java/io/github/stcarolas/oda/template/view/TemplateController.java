package io.github.stcarolas.oda.template.view;

import io.github.stcarolas.oda.template.Template;
import io.github.stcarolas.oda.template.repository.TemplateRepository;
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
  public List<TemplateDto> list(@QueryValue("widget") String widgetType) {
    return repository
      .listSystem(widgetType)
      .stream()
      .map(Template::asDto)
      .toList();
  }
}

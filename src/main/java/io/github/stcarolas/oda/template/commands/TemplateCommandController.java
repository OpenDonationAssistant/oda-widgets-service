package io.github.stcarolas.oda.template.commands;

import io.github.stcarolas.oda.template.repository.TemplateRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/commands/templates")
public class TemplateCommandController {

  private TemplateRepository repository;

  @Inject
  public TemplateCommandController(TemplateRepository repository) {
    this.repository = repository;
  }

  @Post("create")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void create(@Body CreateTemplate command) {
    command.execute(repository);
  }
}

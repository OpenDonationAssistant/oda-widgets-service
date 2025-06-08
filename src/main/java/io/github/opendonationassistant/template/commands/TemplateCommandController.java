package io.github.opendonationassistant.template.commands;

import io.github.opendonationassistant.template.repository.TemplateRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller
public class TemplateCommandController {

  private TemplateRepository repository;

  @Inject
  public TemplateCommandController(TemplateRepository repository) {
    this.repository = repository;
  }

  @Post("/templates/commands/create")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void createTemplate(@Body CreateTemplate command) {
    command.execute(repository);
  }
}

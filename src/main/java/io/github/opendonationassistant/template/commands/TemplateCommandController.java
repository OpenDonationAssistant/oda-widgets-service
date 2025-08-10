package io.github.opendonationassistant.template.commands;

import java.net.http.HttpRequest;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.template.repository.TemplateRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller
public class TemplateCommandController extends BaseController {

  private TemplateRepository repository;

  @Inject
  public TemplateCommandController(TemplateRepository repository) {
    this.repository = repository;
  }

  @Post("/templates/commands/create")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public HttpResponse<Void> createTemplate(
    Authentication auth,
    @Body CreateTemplate command
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()){
      return HttpResponse.unauthorized();
    }
    command.execute(ownerId.get(), repository);
    return HttpResponse.unauthorized();
  }
}

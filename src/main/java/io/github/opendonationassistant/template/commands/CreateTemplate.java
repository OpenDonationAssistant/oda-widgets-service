package io.github.opendonationassistant.template.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.template.api.CreateTemplateApi;
import io.github.opendonationassistant.template.repository.TemplateRepository;
import io.github.opendonationassistant.template.view.TemplateDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller
public class CreateTemplate extends BaseController implements CreateTemplateApi {

  private TemplateRepository repository;

  @Inject
  public CreateTemplate(TemplateRepository repository) {
    this.repository = repository;
  }

  public HttpResponse<TemplateDto> execute(
    Authentication auth,
    @Body CreateTemplateCommand command
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    repository.create(
      ownerId.get(),
      command.widgetType(),
      command.showcase(),
      command.properties()
    );
    return HttpResponse.ok();
  }
}

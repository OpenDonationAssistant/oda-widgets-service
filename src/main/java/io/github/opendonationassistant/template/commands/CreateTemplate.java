package io.github.opendonationassistant.template.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.template.repository.TemplateRepository;
import io.github.opendonationassistant.template.view.TemplateDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

@Controller
public class CreateTemplate extends BaseController {

  private TemplateRepository repository;

  @Inject
  public CreateTemplate(TemplateRepository repository) {
    this.repository = repository;
  }

  @Post("/templates/commands/create")
  @Secured(SecurityRule.IS_AUTHENTICATED)
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

  @Serdeable
  public static record CreateTemplateCommand(
    String widgetType,
    String showcase,
    List<Map<String, Object>> properties
  ) {}
}

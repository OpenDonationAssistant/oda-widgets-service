package io.github.opendonationassistant.template.view;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.template.Template;
import io.github.opendonationassistant.template.api.TemplateApi;
import io.github.opendonationassistant.template.repository.TemplateRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TemplateController extends BaseController implements TemplateApi {

  private TemplateRepository repository;

  @Inject
  public TemplateController(TemplateRepository repository) {
    this.repository = repository;
  }

  public HttpResponse<List<TemplateDto>> listTemplates(
    Authentication auth,
    @QueryValue("widget") String widgetType
  ) {
    var owner = getOwnerId(auth);
    if (owner.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    var merged = new ArrayList<TemplateDto>();
    merged.addAll(
      repository.listSystem(widgetType).stream().map(Template::asDto).toList()
    );
    merged.addAll(
      repository
        .list(owner.get(), widgetType)
        .stream()
        .map(Template::asDto)
        .toList()
    );
    return HttpResponse.ok(merged);
  }
}

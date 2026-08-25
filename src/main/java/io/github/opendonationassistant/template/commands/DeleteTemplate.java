package io.github.opendonationassistant.template.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.template.api.DeleteTemplateApi;
import io.github.opendonationassistant.template.repository.TemplateRepository;
import io.github.opendonationassistant.widget.metrics.WidgetMetrics;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.Optional;

@Controller
public class DeleteTemplate
  extends BaseController
  implements DeleteTemplateApi {

  private TemplateRepository repository;
  private WidgetMetrics metrics;

  @Inject
  public DeleteTemplate(
    TemplateRepository repository,
    WidgetMetrics metrics
  ) {
    this.repository = repository;
    this.metrics = metrics;
  }

  @ExecuteOn(TaskExecutors.BLOCKING)
  public HttpResponse<Void> deleteTemplate(
    Authentication auth,
    DeleteTemplateCommand command
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return repository
      .get(ownerId.get(), command.id())
      .map(it -> {
        try {
          it.delete();
          metrics.templateDeleted(it.widgetType(), ownerId.get());
        } catch (IOException e) {
          return HttpResponse.<Void>serverError();
        }
        return HttpResponse.<Void>ok();
      })
      .orElseGet(() -> HttpResponse.<Void>notFound());
  }
}

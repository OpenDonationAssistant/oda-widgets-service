package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.widget.api.ToggleWidgetApi;
import io.github.opendonationassistant.widget.eventbus.WidgetChangedEventSender;
import io.github.opendonationassistant.widget.metrics.WidgetMetrics;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.github.opendonationassistant.widget.view.WidgetDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Inject;

@Controller
public class ToggleWidgetCommand extends BaseController implements ToggleWidgetApi {

  private final WidgetRepository repository;
  private final WidgetChangedEventSender notificationSender;
  private final WidgetMetrics metrics;

  @Inject
  public ToggleWidgetCommand(
    WidgetRepository repository,
    WidgetChangedEventSender notificationSender,
    WidgetMetrics metrics
  ) {
    this.repository = repository;
    this.notificationSender = notificationSender;
    this.metrics = metrics;
  }

  @ExecuteOn(TaskExecutors.BLOCKING)
  public HttpResponse<WidgetDto> toggleWidget(
    Authentication auth,
    @Body ToogleWidgetRequest request
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return repository
      .findByOwnerIdAndId(ownerId.get(), request.id())
      .map(widget -> {
        var toggled = widget.toggle();
        metrics.widgetToggled(toggled.type(), ownerId.get());
        return toggled;
      })
      .map(WidgetDto::from)
      .map(HttpResponse::ok)
      .orElse(HttpResponse.notFound());
  }
}

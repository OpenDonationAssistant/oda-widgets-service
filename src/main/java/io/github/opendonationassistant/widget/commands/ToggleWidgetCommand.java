package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.repository.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;

@Controller
public class ToggleWidgetCommand extends BaseController {

  private final WidgetRepository repository;
  private final WidgetChangedNotificationSender notificationSender;

  public ToggleWidgetCommand(
    WidgetRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    this.repository = repository;
    this.notificationSender = notificationSender;
  }

  @Post
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @ExecuteOn(TaskExecutors.BLOCKING)
  public HttpResponse<Widget> toggleWidget(
    Authentication auth,
    @Body ToogleWidgetRequest request
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return repository
      .find(ownerId.get(), request.id())
      .map(widget -> {
        widget.setEnabled(!widget.getEnabled());
        repository.update(widget);
        return widget;
      })
      .map(HttpResponse::ok)
      .orElse(HttpResponse.notFound());
  }

  @Serdeable
  public static record ToogleWidgetRequest(String id) {}
}

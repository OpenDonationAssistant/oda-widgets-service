package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.widget.api.AddWidgetApi;
import io.github.opendonationassistant.widget.eventbus.WidgetChangedEventSender;
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
public class AddWidgetCommand extends BaseController implements AddWidgetApi {

  private final WidgetRepository repository;
  private final WidgetChangedEventSender notificationSender;

  @Inject
  public AddWidgetCommand(
    WidgetRepository repository,
    WidgetChangedEventSender notificationSender
  ) {
    this.repository = repository;
    this.notificationSender = notificationSender;
  }

  @ExecuteOn(TaskExecutors.BLOCKING)
  public HttpResponse<WidgetDto> addWidget(
    Authentication auth,
    @Body NewWidgetRequest request
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return HttpResponse.ok(
      WidgetDto.from(repository.create(
        request.type(),
        request.sortOrder(),
        request.name(),
        ownerId.get()
      ))
    );
  }
}

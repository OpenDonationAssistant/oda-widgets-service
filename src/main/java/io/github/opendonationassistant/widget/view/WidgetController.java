package io.github.opendonationassistant.widget.view;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.UpdateWidgetRequest;
import io.github.opendonationassistant.widget.api.WidgetApi;
import io.github.opendonationassistant.widget.commands.ReorderCommand;
import io.github.opendonationassistant.widget.model.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/widgets")
public class WidgetController extends BaseController implements WidgetApi {

  private final WidgetRepository widgetRepository;
  private final WidgetChangedNotificationSender notificationSender;

  @Inject
  public WidgetController(
    WidgetRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    this.widgetRepository = repository;
    this.notificationSender = notificationSender;
  }

  @ExecuteOn(TaskExecutors.BLOCKING)
  public HttpResponse<Void> reorder(
    Authentication auth,
    @Body ReorderCommand command
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    command.execute(ownerId.get(), widgetRepository);
    return HttpResponse.ok();
  }

  @ExecuteOn(TaskExecutors.BLOCKING)
  public HttpResponse<Void> delete(
    @PathVariable("id") String id,
    Authentication auth
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    widgetRepository
      .findByOwnerIdAndId(ownerId.get(), id)
      .ifPresent(Widget::delete);
    return HttpResponse.ok();
  }

  @ExecuteOn(TaskExecutors.BLOCKING)
  public HttpResponse<WidgetDto> update(
    @PathVariable("id") String id,
    @Body UpdateWidgetRequest request,
    Authentication auth
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    @NonNull
    Optional<Widget> widget = widgetRepository.findByOwnerIdAndId(
      ownerId.get(),
      id
    );
    return widget
      .map(it -> request.getName() != null ? it.setName(request.getName()) : it)
      .map(it ->
        request.getSortOrder() != null
          ? it.setSortOrder(request.getSortOrder())
          : it
      )
      .map(it ->
        request.getConfig() != null ? it.withConfig(request.getConfig()) : it
      )
      .map(Widget::save)
      .map(WidgetDto::from)
      .map(updated -> HttpResponse.ok(updated))
      .orElseGet(() -> HttpResponse.notFound());
  }

  public HttpResponse<List<WidgetDto>> list(Authentication auth) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return HttpResponse.ok(
      widgetRepository
        .listByOwnerId(ownerId.get())
        .stream()
        .map(WidgetDto::from)
        .toList()
    );
  }

  public HttpResponse<WidgetDto> get(
    @PathVariable("id") String id,
    Authentication auth
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return widgetRepository
      .findByOwnerIdAndId(ownerId.get(), id)
      .map(WidgetDto::from)
      .map(HttpResponse::ok)
      .orElseGet(() -> HttpResponse.notFound());
  }
}

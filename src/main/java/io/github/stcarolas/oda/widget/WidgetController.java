package io.github.stcarolas.oda.widget;

import io.github.stcarolas.oda.widget.commands.ReorderCommand;
import io.github.stcarolas.oda.widget.domain.Widget;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Controller("/widgets")
public class WidgetController {

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

  @Post("commands/reorder")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Transactional
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

  @Put
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Widget> add(
    @Body NewWidgetRequest request,
    Authentication auth
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    var widget = new Widget();
    widget.setType(request.getType());
    widget.setId(UUID.randomUUID().toString()); // todo change to uuidv7
    widget.setName(request.getType());
    widget.setOwnerId(ownerId.get());
    widget.setSortOrder(
      request.getSortOrder() != null ? request.getSortOrder() : 0
    );
    widget.setConfig(new HashMap<String, Object>());
    widgetRepository.save(widget);
    if (request.getType() != null) {
      notificationSender.send(
        request.getType(),
        new WidgetChangedEvent("created", widget)
      );
    }
    return HttpResponse.ok(widget);
  }

  @Delete("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Void> delete(
    @PathVariable("id") String id,
    Authentication auth
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    Optional<Widget> widget = widgetRepository.find(ownerId.get(), id);
    widgetRepository.deleteById(id);
    widget.ifPresent(it -> {
      notificationSender.send(
        it.getType(),
        new WidgetChangedEvent("deleted", it)
      );
    });
    return HttpResponse.ok();
  }

  @Patch("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Widget> update(
    @PathVariable("id") String id,
    @Body UpdateWidgetRequest request,
    Authentication auth
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()){
      return HttpResponse.unauthorized();
    }
    @NonNull
    Optional<Widget> widget = widgetRepository.find(ownerId.get(), id);
    return widget
      .map(it -> {
        var updated = new Widget();
        updated.setId(it.getId());
        updated.setOwnerId(it.getOwnerId());
        updated.setType(it.getType());
        updated.setName(
          request.getName() != null ? request.getName() : it.getName()
        );
        updated.setSortOrder(
          request.getSortOrder() != null
            ? request.getSortOrder()
            : it.getSortOrder()
        );
        updated.setConfig(
          request.getConfig() != null ? request.getConfig() : it.getConfig()
        );
        widgetRepository.update(updated);
        notificationSender.send(
          it.getType(),
          new WidgetChangedEvent("updated", updated)
        );
        return updated;
      })
      .map(updated -> HttpResponse.ok(updated))
      .orElseGet(() -> HttpResponse.notFound());
  }

  @Secured(SecurityRule.IS_ANONYMOUS)
  @Get
  public HttpResponse<java.util.List<Widget>> list(@Nullable Authentication auth) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()){
      return HttpResponse.unauthorized();
    }
    return HttpResponse.ok(widgetRepository.find(ownerId.get()));
  }

  @Get("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Widget> get(
    @PathVariable("id") String id,
    Authentication auth
  ) {
    final Optional<String> ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    return widgetRepository
      .find(ownerId.get(), id)
      .map(HttpResponse::ok)
      .orElseGet(() -> HttpResponse.notFound());
  }

  private Optional<String> getOwnerId(Authentication auth) {
    return Optional.ofNullable(auth)
      .map(it -> it.getAttributes())
      .map(it -> it.getOrDefault("preferred_username", ""))
      .map(String::valueOf);
  }
}

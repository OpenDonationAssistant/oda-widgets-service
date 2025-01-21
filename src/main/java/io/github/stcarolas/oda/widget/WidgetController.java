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
  public void reorder(Authentication auth, @Body ReorderCommand command){
    command.execute(getOwnerId(auth), widgetRepository);
  }

  @Put
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public Widget add(@Body NewWidgetRequest request, Authentication auth) {
    var widget = new Widget();
    widget.setType(request.getType());
    widget.setId(UUID.randomUUID().toString()); // todo change to uuidv7
    widget.setName(request.getType());
    widget.setOwnerId(getOwnerId(auth));
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
    return widget;
  }

  @Delete("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public void delete(@PathVariable("id") String id, Authentication auth) {
    Optional<Widget> widget = widgetRepository.find(getOwnerId(auth), id);
    widgetRepository.deleteById(id);
    widget.ifPresent(it -> {
      notificationSender.send(
        it.getType(),
        new WidgetChangedEvent("deleted", it)
      );
    });
  }

  @Patch("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Widget> update(
    @PathVariable("id") String id,
    @Body UpdateWidgetRequest request,
    Authentication auth
  ) {
    @NonNull
    Optional<Widget> widget = widgetRepository.find(getOwnerId(auth), id);
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
  public java.util.List<Widget> list(@Nullable Authentication auth) {
    return widgetRepository.find(getOwnerId(auth));
  }

  @Get("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Widget> get(
    @PathVariable("id") String id,
    Authentication auth
  ) {
    return widgetRepository
      .find(getOwnerId(auth), id)
      .map(HttpResponse::ok)
      .orElseGet(() -> HttpResponse.notFound());
  }

  private String getOwnerId(Authentication auth) {
    return String.valueOf(
      auth.getAttributes().getOrDefault("preferred_username", "")
    );
  }
}

package io.github.stcarolas.oda.widget;

import java.util.Optional;
import java.util.UUID;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/widgets")
public class WidgetController {

  private final WidgetRepository widgetRepository;

  @Inject
  public WidgetController(WidgetRepository repository) {
    this.widgetRepository = repository;
  }

  @Put
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public void add(@Body NewWidgetRequest request, Authentication auth){
    var widget = new Widget();
    widget.setType(request.getType());
    widget.setId(UUID.randomUUID().toString());
    widget.setName("New widget");
    widget.setOwnerId(getOwnerId(auth));
    widget.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
    if (request.getType() != null) {
      widget.setConfig(
        Default.configs.get(request.getType())
          .map(Widget::getConfig)
          .getOrElse(() -> new java.util.HashMap<String, Object>())
      );
    }
    widgetRepository.save(widget);
  }

  @Delete("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public void delete(@PathVariable("id") String id){
    widgetRepository.deleteById(id);
  }

  @Patch("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Widget> update(
    @PathVariable("id") String id,
    @Body UpdateWidgetRequest request,
    Authentication auth
  ){
    @NonNull
    Optional<Widget> widget = widgetRepository.find(getOwnerId(auth),id);
    return widget
      .map(it -> {
        var updated = new Widget();
        updated.setId(it.getId());
        updated.setOwnerId(it.getOwnerId());
        updated.setType(it.getType());
        updated.setName(request.getName() != null ? request.getName() : it.getName());
        updated.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : it.getSortOrder());
        updated.setConfig(request.getConfig() != null ? request.getConfig() : it.getConfig());
        widgetRepository.update(updated);
        return updated;
      })
      .map(updated -> HttpResponse.ok(updated))
      .orElseGet(() -> HttpResponse.notFound());
  }

  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get
  public java.util.List<Widget> list(Authentication auth){
    return widgetRepository.find(getOwnerId(auth));
  }

  @Get("{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Widget> get(@PathVariable("id") String id, Authentication auth){
    return widgetRepository.find(getOwnerId(auth), id)
      .map(HttpResponse::ok)
      .orElseGet(() -> HttpResponse.notFound());
  }

  private String getOwnerId(Authentication auth) {
    return String.valueOf(
      auth.getAttributes().getOrDefault("preferred_username", "")
    );
  }

}

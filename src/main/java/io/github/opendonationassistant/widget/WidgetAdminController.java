package io.github.opendonationassistant.widget;

import io.github.opendonationassistant.widget.repository.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller
public class WidgetAdminController {

  private final WidgetRepository widgetRepository;

  @Get("/admin/widgets/{id}")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public HttpResponse<Widget> get(@PathVariable("id") String id) {
    return widgetRepository
      .findById(id)
      .map(HttpResponse::ok)
      .orElseGet(() -> HttpResponse.notFound());
  }

  public WidgetAdminController(WidgetRepository widgetRepository) {
    this.widgetRepository = widgetRepository;
  }
}

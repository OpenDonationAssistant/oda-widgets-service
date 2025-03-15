package io.github.stcarolas.oda.widget;

import io.github.stcarolas.oda.widget.domain.Widget;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller
public class WidgetAdminController {

  private final WidgetRepository widgetRepository;

  @Get("/admin/widgets/{id}")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public HttpResponse<Widget> get(
    @PathVariable("id") String id,
    @QueryValue("recipientId") String recipientId
  ) {
    return widgetRepository
      .find(recipientId, id)
      .map(HttpResponse::ok)
      .orElseGet(() -> HttpResponse.notFound());
  }

  public WidgetAdminController(WidgetRepository widgetRepository) {
    this.widgetRepository = widgetRepository;
  }
}

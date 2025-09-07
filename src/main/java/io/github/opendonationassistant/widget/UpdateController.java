package io.github.opendonationassistant.widget;

import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/update")
public class UpdateController {

  private final WidgetRepository widgetRepository;

  @Inject
  public UpdateController(WidgetRepository repository) {
    this.widgetRepository = repository;
  }

  @Post
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void runUpdate() {
    widgetRepository.updateWidget(
      WidgetRepository.PAYMENT_ALERTS_TYPE,
      widget -> {
        return widget;
      }
    );
  }
}

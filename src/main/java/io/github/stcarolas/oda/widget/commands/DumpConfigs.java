package io.github.stcarolas.oda.widget.commands;

import io.github.stcarolas.oda.widget.WidgetChangedEvent;
import io.github.stcarolas.oda.widget.WidgetChangedNotificationSender;
import io.github.stcarolas.oda.widget.WidgetRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller
public class DumpConfigs {

  private final WidgetRepository repository;
  private final WidgetChangedNotificationSender notificationSender;

  public DumpConfigs(
    WidgetRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    this.repository = repository;
    this.notificationSender = notificationSender;
  }

  @Post("/admin/widgets/dump")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void dumpCongigs() {
    repository
      .findAll()
      .stream()
      .forEach(widget -> {
        notificationSender.send(
          widget.getType(),
          new WidgetChangedEvent("updated", widget)
        );
      });
  }
}

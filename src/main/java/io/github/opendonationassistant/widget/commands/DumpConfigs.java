package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.WidgetChangedEvent;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.repository.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.stream.Stream;

@Controller
public class DumpConfigs {

  private ODALogger log = new ODALogger(this);

  private final WidgetRepository repository;
  private final WidgetChangedNotificationSender notificationSender;

  @Inject
  public DumpConfigs(
    WidgetRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    this.repository = repository;
    this.notificationSender = notificationSender;
  }

  @Post("/admin/widgets/dump")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public void dumpCongigs(@Body DumpConfigsRequest request) {
    Stream<Widget> widgets = repository.all().stream();
    if (StringUtils.isNotEmpty(request.widgetType())) {
      widgets = widgets.filter(widget -> widget.type().equals(request.widgetType()));
    }
    widgets.forEach(widget -> {
      log.info("Dumping widget", Map.of("id", widget.id()));
      notificationSender.send(
        widget.type(),
        new WidgetChangedEvent("updated", widget.asDto())
      );
    });
  }

  @Serdeable
  public static record DumpConfigsRequest(String widgetType) {}
}

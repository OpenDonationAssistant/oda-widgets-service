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
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DumpConfigs {

  private ODALogger log = new ODALogger(this);

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
  public void dumpCongigs(@Body DumpConfigsRequest request) {
    final Stream<Widget> widgets = repository.findAll().stream();
    if (StringUtils.isNotEmpty(request.widgetType())) {
      widgets.filter(widget -> widget.getType().equals(request.widgetType()));
    }
    widgets.forEach(widget -> {
      log.info("Dumping widget", Map.of("id", widget.getId());
      notificationSender.send(
        widget.getType(),
        new WidgetChangedEvent("updated", widget.asDto())
      );
    });
  }

  @Serdeable
  public static record DumpConfigsRequest(String widgetType) {}
}

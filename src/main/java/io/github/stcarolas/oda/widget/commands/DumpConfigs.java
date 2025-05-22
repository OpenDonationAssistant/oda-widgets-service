package io.github.stcarolas.oda.widget.commands;

import io.github.stcarolas.oda.widget.WidgetChangedEvent;
import io.github.stcarolas.oda.widget.WidgetChangedNotificationSender;
import io.github.stcarolas.oda.widget.WidgetRepository;
import io.github.stcarolas.oda.widget.domain.Widget;
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

  private Logger log = LoggerFactory.getLogger(DumpConfigs.class);

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
      log.info("dumping {}", widget.getId());
      notificationSender.send(
        widget.getType(),
        new WidgetChangedEvent("updated", widget)
      );
    });
  }

  @Serdeable
  public static record DumpConfigsRequest(String widgetType) {}
}

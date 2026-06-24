package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.WidgetChangedEvent;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.api.DumpConfigsApi;
import io.github.opendonationassistant.widget.model.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Controller
public class DumpConfigs implements DumpConfigsApi {

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

  public CompletableFuture<HttpResponse<Void>> dumpConfigs(
    Authentication auth,
    @Body DumpConfigsRequest request
  ) {
    Stream<Widget> widgets = repository.all().stream();
    if (StringUtils.isNotEmpty(request.widgetType())) {
      widgets = widgets.filter(widget ->
        widget.type().equals(request.widgetType())
      );
    }
    if (StringUtils.isNotEmpty(request.widgetId())) {
      widgets = widgets.filter(widget -> widget.id().equals(request.widgetId())
      );
    }
    widgets.forEach(widget -> {
      log.info("Dumping widget", Map.of("id", widget.id()));
      notificationSender.send(
        widget.type(),
        new WidgetChangedEvent("updated", widget.asDto(), "dump", null)
      );
    });
    return CompletableFuture.completedFuture(HttpResponse.ok());
  }
}

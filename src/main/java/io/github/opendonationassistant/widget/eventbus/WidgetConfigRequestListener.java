package io.github.opendonationassistant.widget.eventbus;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;

import java.util.List;
import org.jspecify.annotations.Nullable;

@RabbitListener
public class WidgetConfigRequestListener {

  private ODALogger log = new ODALogger(this);
  public static final String QUEUE_NAME = "widget.config-request";
  public static final io.github.opendonationassistant.rabbit.Queue QUEUE =
    new io.github.opendonationassistant.rabbit.Queue(QUEUE_NAME);

  private final WidgetRepository repository;

  @Inject
  public WidgetConfigRequestListener(WidgetRepository repository) {
    this.repository = repository;
  }

  @Queue(QUEUE_NAME)
  public List<Widget> handle(WidgetConfigRequest request) {
    log.info("Widget Config Request received");
    if (request.widgetId() != null) {
      return repository
        .findById(request.widgetId())
        .map(widget -> widget.asDto())
        .map(widget -> List.of(widget))
        .orElse(List.of());
    }
    if (request.widgetType() != null) {
      return repository.findByWidgetType(request.widgetType())
        .map(widget -> widget.asDto())
        .toList();
    }
    return List.of();
  }

  @Serdeable
  public static record WidgetConfigRequest(
    @Nullable String widgetId,
    @Nullable String widgetType
  ) {}
}

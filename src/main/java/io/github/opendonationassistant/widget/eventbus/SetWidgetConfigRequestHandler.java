package io.github.opendonationassistant.widget.eventbus;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.github.opendonationassistant.widget.view.WidgetDto;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * RabbitMQ RPC handler that listens for a {@link SetWidgetConfig} command
 * and updates/overrides the config of an existing widget.
 */
@RabbitListener
public class SetWidgetConfigRequestHandler {

  private final ODALogger log = new ODALogger(this);
  public static final String QUEUE_NAME = "widget.set-config-request";

  private final WidgetRepository repository;

  @Inject
  public SetWidgetConfigRequestHandler(WidgetRepository repository) {
    this.repository = repository;
  }

  @Queue(QUEUE_NAME)
  public List<WidgetDto> handle(SetWidgetConfig request) {
    log.info("Set Widget Config Request received", Map.of("request", request));
    return repository
      .findById(request.widgetId())
      .map(widget -> widget.withConfig(request.config()).save("manual", null))
      .map(WidgetDto::from)
      .map(widget -> List.of(widget))
      .orElse(List.of());
  }

  @Serdeable
  public static record SetWidgetConfig(
    String widgetId,
    Map<String, Object> config
  ) {}
}

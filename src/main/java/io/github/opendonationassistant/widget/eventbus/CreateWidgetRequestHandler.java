package io.github.opendonationassistant.widget.eventbus;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.github.opendonationassistant.widget.view.WidgetDto;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import java.util.Map;

/**
 * RabbitMQ RPC handler that listens for a {@link CreateWidgetRequest} command
 * and creates a widget of the requested type.
 */
@RabbitListener
public class CreateWidgetRequestHandler {

  private final ODALogger log = new ODALogger(this);
  public static final String QUEUE_NAME = "widget.create-request";
  private static final Integer DEFAULT_SORT_ORDER = 0;

  private final WidgetRepository repository;

  @Inject
  public CreateWidgetRequestHandler(WidgetRepository repository) {
    this.repository = repository;
  }

  @Queue(QUEUE_NAME)
  public WidgetDto handle(CreateWidgetRequest request) {
    log.info("Create Widget Request received", Map.of("request", request));
    var widget = repository.create(
      request.type(),
      DEFAULT_SORT_ORDER,
      request.type(),
      request.recipientId()
    );
    return WidgetDto.from(widget);
  }

  @Serdeable
  public static record CreateWidgetRequest(String type, String recipientId) {}
}

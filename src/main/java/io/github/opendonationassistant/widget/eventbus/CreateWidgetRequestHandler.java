package io.github.opendonationassistant.widget.eventbus;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.rabbit.Exchange;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.github.opendonationassistant.widget.view.WidgetDto;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

@RabbitListener
public class CreateWidgetRequestHandler {

  private final ODALogger log = new ODALogger(this);
  private static final Integer DEFAULT_SORT_ORDER = 0;

  public static final String QUEUE_NAME = "widget.create-request";
  public static final io.github.opendonationassistant.rabbit.Queue QUEUE =
    new io.github.opendonationassistant.rabbit.Queue(QUEUE_NAME);
  public static final List<Exchange> BINDINGS = List.of(
    Exchange.Exchange(
      "rpc",
      Map.of(QUEUE_NAME, CreateWidgetRequestHandler.QUEUE)
    )
  );

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

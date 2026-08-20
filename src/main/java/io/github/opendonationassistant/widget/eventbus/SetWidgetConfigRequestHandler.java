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
public class SetWidgetConfigRequestHandler {

  public static final String QUEUE_NAME = "widget.set-config-request";
  public static final io.github.opendonationassistant.rabbit.Queue QUEUE =
    new io.github.opendonationassistant.rabbit.Queue(QUEUE_NAME);
  public static final List<Exchange> BINDINGS = List.of(
    Exchange.Exchange(
      "rpc",
      Map.of(QUEUE_NAME, SetWidgetConfigRequestHandler.QUEUE)
    )
  );

  private final ODALogger log = new ODALogger(this);
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

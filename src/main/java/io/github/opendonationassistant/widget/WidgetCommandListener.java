package io.github.opendonationassistant.widget;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.WidgetCommandSender.WidgetUpdateCommand;
import io.github.opendonationassistant.rabbit.Exchange;
import io.github.opendonationassistant.widget.model.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RabbitListener
public class WidgetCommandListener {

  public static final String QUEUE_NAME = "widgets.commands";
  public static final io.github.opendonationassistant.rabbit.Queue QUEUE =
    new io.github.opendonationassistant.rabbit.Queue(QUEUE_NAME);
  public static final List<Exchange> BINDINGS = List.of(
    Exchange.Exchange("widgets", Map.of("command", WidgetCommandListener.QUEUE))
  );

  private ODALogger log = new ODALogger(this);
  private final WidgetRepository repository;

  public WidgetCommandListener(WidgetRepository repository) {
    this.repository = repository;
  }

  @Queue(QUEUE_NAME)
  public void listen(WidgetUpdateCommand command) {
    log.info("Widget Command received", Map.of("command", command));
    @NonNull
    final Optional<Widget> widget = repository.findById(command.id());
    if (widget.isEmpty()) {
      log.info("Widget not found", Map.of("id", command.id()));
    }
    widget.ifPresent(it -> {
      command
        .patch()
        .properties()
        .forEach(prop -> {
          it
            .updateProperty(prop.name(), prop.value())
            .save("command", command.id());
        });
    });
  }
}

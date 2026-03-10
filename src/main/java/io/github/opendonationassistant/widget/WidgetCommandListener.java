package io.github.opendonationassistant.widget;

import static io.github.opendonationassistant.rabbit.Queue.Commands.WIDGETS;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.ui.UIFacade.UIEventSender;
import io.github.opendonationassistant.events.widget.WidgetUpdateCommand;
import io.github.opendonationassistant.widget.model.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.serde.ObjectMapper;
import java.util.Map;
import java.util.Optional;

@RabbitListener
public class WidgetCommandListener {

  private ODALogger log = new ODALogger(this);
  private final WidgetRepository repository;
  // TODO use facade in automation-service, remove notifying on rest updates
  private final UIEventSender commandSender;
  private final ObjectMapper mapper;

  public WidgetCommandListener(
    WidgetRepository repository,
    UIEventSender commandSender,
    ObjectMapper mapper
  ) {
    this.repository = repository;
    this.commandSender = commandSender;
    this.mapper = mapper;
  }

  @Queue(WIDGETS)
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
          it.updateProperty(prop.name(), prop.value()).save();
        });
      try {
        var event = Map.of("id", command.id(), "type", "ConfigUpdated");
        commandSender.sendEvent(
          it.data().ownerId(),
          mapper.writeValueAsBytes(event)
        );
      } catch (Exception e) {
        log.error("Failed to send event", Map.of("error", e.getMessage()));
      }
    });
  }
}

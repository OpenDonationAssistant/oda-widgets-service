package io.github.opendonationassistant.widget;

import static io.github.opendonationassistant.rabbit.Queue.Commands.WIDGETS;

import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.command.Command;
import io.github.opendonationassistant.events.command.CommandSender;
import io.github.opendonationassistant.events.widget.WidgetChangedEvent;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.events.widget.WidgetUpdateCommand;
import io.github.opendonationassistant.widget.repository.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import java.util.Map;
import java.util.Optional;

@RabbitListener
public class WidgetCommandListener {

  private ODALogger log = new ODALogger(this);
  private final WidgetRepository repository;
  private final CommandSender commandSender;
  private final WidgetChangedNotificationSender changesSender;

  public WidgetCommandListener(
    WidgetRepository repository,
    CommandSender commandSender,
    WidgetChangedNotificationSender changesSender
  ) {
    this.repository = repository;
    this.commandSender = commandSender;
    this.changesSender = changesSender;
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
          log.info(
            "Updating property",
            Map.of("property", prop, "oldValue", it.getProperty(prop.name()))
          );
          var updated = it.updateProperty(prop.name(), prop.value());
          repository.update(updated); // TODO batch and separate
          changesSender.send(
            it.getType(),
            new WidgetChangedEvent("updated", it.asDto())
          );
        });
    });

    var notifyCommand = new Command(command.id(), "reload");
    commandSender.send(notifyCommand);
  }
}

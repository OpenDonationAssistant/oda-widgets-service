package io.github.stcarolas.oda.widget;

import static io.github.opendonationassistant.rabbit.Queue.Commands.WIDGETS;

import io.github.opendonationassistant.events.command.Command;
import io.github.opendonationassistant.events.command.CommandSender;
import io.github.opendonationassistant.events.widget.WidgetUpdateCommand;
import io.github.stcarolas.oda.widget.domain.Widget;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RabbitListener
public class WidgetCommandListener {

  private Logger log = LoggerFactory.getLogger(WidgetCommandListener.class);
  private final WidgetRepository repository;
  private final CommandSender commandSender;

  public WidgetCommandListener(
    WidgetRepository repository,
    CommandSender commandSender
  ) {
    this.repository = repository;
    this.commandSender = commandSender;
  }

  @Queue(WIDGETS)
  public void listen(WidgetUpdateCommand command) {
    log.info("Widget Command received: {}", command);
    @NonNull
    final Optional<Widget> widget = repository.findById(command.getId());
    if (widget.isEmpty()) {
      log.warn("Widget not found: {}", command.getId());
    }
    widget.ifPresent(it -> {
      command
        .getPatch()
        .getProperties()
        .forEach(prop -> {
          log.info("Updating property: {}", prop);
          log.info("Old value: {}", it.getProperty(prop.getName()));
          var updated = it.updateProperty(prop.getName(), prop.getValue());
          repository.update(updated); // TODO batch
        });
    });
    var notifyCommand = new Command(command.getId(), "reload");
    commandSender.send(notifyCommand);
  }
}

package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class ReorderCommand {

  public List<String> ids;

  public ReorderCommand(List<String> ids) {
    this.ids = ids;
  }

  public void execute(String ownerId, WidgetRepository repository) {
    var widgets = repository.listByOwnerId(ownerId);
    widgets
      .stream()
      .forEach(it -> {
        var order = ids.indexOf(it.id());
        it.setSortOrder(order > -1 ? order : widgets.size()).save();
      });
  }
}

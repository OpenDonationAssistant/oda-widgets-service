package io.github.stcarolas.oda.widget.commands;

import java.util.List;

import io.github.stcarolas.oda.widget.WidgetRepository;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ReorderCommand {

  public List<String> ids;

  public ReorderCommand(List<String> ids) {
    this.ids = ids;
  }

  public void execute(String ownerId, WidgetRepository repository){
    var widgets = repository.find(ownerId);
    var updatedWidgets = widgets.stream().map(it -> {
      var order = ids.indexOf(it.getId());
      // TODO: immutable
      it.setSortOrder(order > 0 ? order : widgets.size());
      return it;
    }).toList();
    repository.updateAll(updatedWidgets);
  }

}

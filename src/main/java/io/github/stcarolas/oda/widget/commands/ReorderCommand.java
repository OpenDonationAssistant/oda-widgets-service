package io.github.stcarolas.oda.widget.commands;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.stcarolas.oda.widget.WidgetRepository;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ReorderCommand {

  private Logger log = LoggerFactory.getLogger(ReorderCommand.class);

  public List<String> ids;

  public ReorderCommand(List<String> ids) {
    this.ids = ids;
  }

  public void execute(String ownerId, WidgetRepository repository){
    var widgets = repository.find(ownerId);
    var updatedWidgets = widgets.stream().map(it -> {
      var order = ids.indexOf(it.getId());
      log.debug("id: {}, order: {}", it.getId(), order);
      // TODO: immutable
      it.setSortOrder(order > 0 ? order : widgets.size());
      return it;
    }).toList();
    log.debug("updated widgets: {}", updatedWidgets.stream().map(it -> it.getId()).toList());
    repository.updateAll(updatedWidgets);
  }

}

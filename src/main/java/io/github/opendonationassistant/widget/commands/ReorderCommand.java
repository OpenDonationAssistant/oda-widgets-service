package io.github.opendonationassistant.widget.commands;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.opendonationassistant.widget.repository.WidgetRepository;
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
      // TODO: immutable
      it.setSortOrder(order > -1 ? order : widgets.size());
      repository.update(it);
      log.info("id: {}, order: {}", it.getId(), order);
      return it;
    }).toList();
  }

}

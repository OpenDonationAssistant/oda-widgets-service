package io.github.opendonationassistant.widget.view;

import io.github.opendonationassistant.widget.model.Widget;
import io.micronaut.serde.annotation.Serdeable;
import java.util.Map;

@Serdeable
public record WidgetDto(
  String id,
  String type,
  Integer sortOrder,
  String name,
  String ownerId,
  Map<String, Object> config,
  boolean enabled,
  boolean deleted
) {
  public static WidgetDto from(Widget widget) {
    return new WidgetDto(
      widget.data().id(),
      widget.data().type(),
      widget.data().sortOrder(),
      widget.data().name(),
      widget.data().ownerId(),
      widget.data().config(),
      widget.data().enabled(),
      widget.data().deleted()
    );
  }
}

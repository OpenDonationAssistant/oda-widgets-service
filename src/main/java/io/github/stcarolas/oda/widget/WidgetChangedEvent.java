package io.github.stcarolas.oda.widget;

import io.github.stcarolas.oda.widget.domain.Widget;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class WidgetChangedEvent {

  private String type;

  private Widget widget;

  public WidgetChangedEvent(String type, Widget widget) {
    this.type = type;
    this.widget = widget;
  }

  public String getType() {
    return type;
  }

  public Widget getWidget() {
    return widget;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((widget == null) ? 0 : widget.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    WidgetChangedEvent other = (WidgetChangedEvent) obj;
    if (type == null) {
      if (other.type != null) return false;
    } else if (!type.equals(other.type)) return false;
    if (widget == null) {
      if (other.widget != null) return false;
    } else if (!widget.equals(other.widget)) return false;
    return true;
  }

  @Override
  public String toString() {
    return (
      "{\"_type\"=\"WidgetChangedEvent\",\"type\"=\"" +
      type +
      "\", widget\"=\"" +
      widget +
      "}"
    );
  }
}

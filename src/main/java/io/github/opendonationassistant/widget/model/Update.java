package io.github.opendonationassistant.widget.model;

import io.micronaut.serde.annotation.Serdeable;
import java.util.function.Function;

import org.jspecify.annotations.Nullable;

@Serdeable
public record Update(
  Condition condition,
  Function<WidgetProperty<?>, WidgetProperty<?>> updateFn
) {
  @Serdeable
  public static record Condition(
    @Nullable Class<?> widgetClass,
    @Nullable Class<?> propertyType,
    @Nullable String name
  ) {}
}

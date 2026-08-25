package io.github.opendonationassistant.widget.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

@Singleton
public class WidgetMetrics {

  public static final String ADDED_METRIC_NAME = "widget.added";
  public static final String UPDATED_METRIC_NAME = "widget.updated";
  public static final String DELETED_METRIC_NAME = "widget.deleted";
  public static final String TOGGLED_METRIC_NAME = "widget.toggled";
  public static final String REORDERED_METRIC_NAME = "widget.reordered";
  public static final String TAG_ADDED_METRIC_NAME = "widget.tag.added";
  public static final String CONFIG_REQUESTED_METRIC_NAME =
    "widget.config.requested";
  public static final String CONFIG_SET_METRIC_NAME = "widget.config.set";
  public static final String COMMAND_RECEIVED_METRIC_NAME =
    "widget.command.received";
  public static final String COMMAND_APPLIED_METRIC_NAME =
    "widget.command.applied";
  public static final String TEMPLATE_CREATED_METRIC_NAME = "template.created";
  public static final String TEMPLATE_DELETED_METRIC_NAME = "template.deleted";
  public static final String TYPE_TAG = "type";
  public static final String WIDGET_TYPE_TAG = "widgetType";
  public static final String RECIPIENT_ID_TAG = "recipientId";
  public static final String UNKNOWN = "unknown";

  private final MeterRegistry registry;

  @Inject
  public WidgetMetrics(MeterRegistry registry) {
    this.registry = registry;
  }

  public void widgetAdded(@Nullable String type, @Nullable String recipientId) {
    counter(ADDED_METRIC_NAME, TYPE_TAG, type, recipientId).increment();
  }

  public void widgetUpdated(@Nullable String type, @Nullable String recipientId) {
    counter(UPDATED_METRIC_NAME, TYPE_TAG, type, recipientId).increment();
  }

  public void widgetDeleted(@Nullable String type, @Nullable String recipientId) {
    counter(DELETED_METRIC_NAME, TYPE_TAG, type, recipientId).increment();
  }

  public void widgetToggled(@Nullable String type, @Nullable String recipientId) {
    counter(TOGGLED_METRIC_NAME, TYPE_TAG, type, recipientId).increment();
  }

  public void widgetReordered(@Nullable String recipientId) {
    counter(REORDERED_METRIC_NAME, recipientId).increment();
  }

  public void widgetTagAdded(@Nullable String type, @Nullable String recipientId) {
    counter(TAG_ADDED_METRIC_NAME, TYPE_TAG, type, recipientId).increment();
  }

  public void widgetConfigRequested(@Nullable String recipientId) {
    counter(CONFIG_REQUESTED_METRIC_NAME, recipientId).increment();
  }

  public void widgetConfigSet(@Nullable String type, @Nullable String recipientId) {
    counter(CONFIG_SET_METRIC_NAME, TYPE_TAG, type, recipientId).increment();
  }

  public void widgetCommandReceived(@Nullable String recipientId) {
    counter(COMMAND_RECEIVED_METRIC_NAME, recipientId).increment();
  }

  public void widgetCommandApplied(@Nullable String recipientId) {
    counter(COMMAND_APPLIED_METRIC_NAME, recipientId).increment();
  }

  public void templateCreated(
    @Nullable String widgetType,
    @Nullable String recipientId
  ) {
    counter(TEMPLATE_CREATED_METRIC_NAME, WIDGET_TYPE_TAG, widgetType, recipientId)
      .increment();
  }

  public void templateDeleted(
    @Nullable String widgetType,
    @Nullable String recipientId
  ) {
    counter(TEMPLATE_DELETED_METRIC_NAME, WIDGET_TYPE_TAG, widgetType, recipientId)
      .increment();
  }

  private Counter counter(String name, @Nullable String recipientId) {
    return registry.counter(
      name,
      RECIPIENT_ID_TAG,
      Optional.ofNullable(recipientId).orElse(UNKNOWN)
    );
  }

  private Counter counter(
    String name,
    String tag,
    @Nullable String value,
    @Nullable String recipientId
  ) {
    return registry.counter(
      name,
      tag,
      Optional.ofNullable(value).orElse(UNKNOWN),
      RECIPIENT_ID_TAG,
      Optional.ofNullable(recipientId).orElse(UNKNOWN)
    );
  }
}
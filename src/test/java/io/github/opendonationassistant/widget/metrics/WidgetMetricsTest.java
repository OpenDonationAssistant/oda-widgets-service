package io.github.opendonationassistant.widget.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

public class WidgetMetricsTest {

  private static final String MEDIA = "media";
  private static final String PAYMENT_ALERTS = "payment-alerts";
  private static final String RECIPIENT = "user-1";

  @Test
  public void countsAddedWidgetsPerType() {
    var registry = new SimpleMeterRegistry();
    var metrics = new WidgetMetrics(registry);

    metrics.widgetAdded(MEDIA, RECIPIENT);
    metrics.widgetAdded(MEDIA, RECIPIENT);
    metrics.widgetAdded(PAYMENT_ALERTS, RECIPIENT);

    assertEquals(
      2,
      registry
        .counter(
          WidgetMetrics.ADDED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.ADDED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          PAYMENT_ALERTS,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
  }

  @Test
  public void countsWidgetLifecycleOperations() {
    var registry = new SimpleMeterRegistry();
    var metrics = new WidgetMetrics(registry);

    metrics.widgetUpdated(MEDIA, RECIPIENT);
    metrics.widgetDeleted(MEDIA, RECIPIENT);
    metrics.widgetToggled(MEDIA, RECIPIENT);
    metrics.widgetTagAdded(MEDIA, RECIPIENT);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.UPDATED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.DELETED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.TOGGLED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.TAG_ADDED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
  }

  @Test
  public void countsWidgetOperationsPerRecipient() {
    var registry = new SimpleMeterRegistry();
    var metrics = new WidgetMetrics(registry);

    metrics.widgetUpdated(MEDIA, "user-1");
    metrics.widgetUpdated(MEDIA, "user-2");

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.UPDATED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          "user-1"
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.UPDATED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          "user-2"
        )
        .count()
    );
  }

  @Test
  public void countsRecipientOnlyWidgetOperations() {
    var registry = new SimpleMeterRegistry();
    var metrics = new WidgetMetrics(registry);

    metrics.widgetReordered(RECIPIENT);
    metrics.widgetConfigRequested(RECIPIENT);
    metrics.widgetCommandReceived(RECIPIENT);
    metrics.widgetCommandApplied(RECIPIENT);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.REORDERED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.CONFIG_REQUESTED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.COMMAND_RECEIVED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.COMMAND_APPLIED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
  }

  @Test
  public void countsConfigSetPerType() {
    var registry = new SimpleMeterRegistry();
    var metrics = new WidgetMetrics(registry);

    metrics.widgetConfigSet(MEDIA, RECIPIENT);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.CONFIG_SET_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
  }

  @Test
  public void countsTemplateOperationsPerWidgetType() {
    var registry = new SimpleMeterRegistry();
    var metrics = new WidgetMetrics(registry);

    metrics.templateCreated(MEDIA, RECIPIENT);
    metrics.templateDeleted(MEDIA, RECIPIENT);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.TEMPLATE_CREATED_METRIC_NAME,
          WidgetMetrics.WIDGET_TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.TEMPLATE_DELETED_METRIC_NAME,
          WidgetMetrics.WIDGET_TYPE_TAG,
          MEDIA,
          WidgetMetrics.RECIPIENT_ID_TAG,
          RECIPIENT
        )
        .count()
    );
  }

  @Test
  public void mapsMissingValuesToUnknown() {
    var registry = new SimpleMeterRegistry();
    var metrics = new WidgetMetrics(registry);

    metrics.widgetAdded(null, null);
    metrics.templateCreated(null, null);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.ADDED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          WidgetMetrics.UNKNOWN,
          WidgetMetrics.RECIPIENT_ID_TAG,
          WidgetMetrics.UNKNOWN
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.TEMPLATE_CREATED_METRIC_NAME,
          WidgetMetrics.WIDGET_TYPE_TAG,
          WidgetMetrics.UNKNOWN,
          WidgetMetrics.RECIPIENT_ID_TAG,
          WidgetMetrics.UNKNOWN
        )
        .count()
    );
  }
}
package io.github.opendonationassistant.widget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.opendonationassistant.events.widget.Widget.WidgetConfig;
import io.github.opendonationassistant.events.widget.Widget.WidgetProperty;
import io.github.opendonationassistant.events.widget.WidgetCommandSender.WidgetUpdateCommand;
import io.github.opendonationassistant.widget.eventbus.WidgetChangedEventSender;
import io.github.opendonationassistant.widget.metrics.WidgetMetrics;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class WidgetCommandListenerTest {

  WidgetDataRepository dataRepository = mock(WidgetDataRepository.class);
  WidgetChangedEventSender sender = mock(WidgetChangedEventSender.class);
  SimpleMeterRegistry registry = new SimpleMeterRegistry();
  WidgetMetrics metrics = new WidgetMetrics(registry);

  @Test
  public void countsReceivedAndAppliedCommands() {
    var repository = new WidgetRepository(dataRepository, sender, metrics);
    var data = new WidgetData(
      "id",
      "media",
      0,
      "name",
      "user",
      Map.of(),
      true,
      false,
      List.of()
    );
    when(dataRepository.findById("id")).thenReturn(Optional.of(data));
    var command = new WidgetUpdateCommand(
      "id",
      new WidgetConfig(
        List.of(new WidgetProperty("maxLen", "Max len", "number", 100))
      )
    );

    new WidgetCommandListener(repository, metrics).listen(command);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.COMMAND_RECEIVED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          "user"
        )
        .count()
    );
    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.COMMAND_APPLIED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          "user"
        )
        .count()
    );
  }

  @Test
  public void countsReceivedButNotAppliedWhenWidgetNotFound() {
    var repository = new WidgetRepository(dataRepository, sender, metrics);
    when(dataRepository.findById("id")).thenReturn(Optional.empty());
    var command = new WidgetUpdateCommand(
      "id",
      new WidgetConfig(List.of())
    );

    new WidgetCommandListener(repository, metrics).listen(command);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.COMMAND_RECEIVED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          WidgetMetrics.UNKNOWN
        )
        .count()
    );
    assertEquals(
      0,
      registry
        .counter(
          WidgetMetrics.COMMAND_APPLIED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          WidgetMetrics.UNKNOWN
        )
        .count()
    );
  }
}
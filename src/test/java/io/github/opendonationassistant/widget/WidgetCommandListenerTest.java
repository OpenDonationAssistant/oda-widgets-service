package io.github.opendonationassistant.widget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.opendonationassistant.events.widget.Widget.WidgetConfig;
import io.github.opendonationassistant.events.widget.Widget.WidgetProperty;
import io.github.opendonationassistant.events.widget.WidgetChangedEvent;
import io.github.opendonationassistant.events.widget.WidgetCommandSender.WidgetUpdateCommand;
import io.github.opendonationassistant.widget.eventbus.WidgetChangedEventSender;
import io.github.opendonationassistant.widget.metrics.WidgetMetrics;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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

  @Test
  @SuppressWarnings("unchecked")
  public void appliesAllPropertiesFromSingleCommandAndPersistsOnce() {
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
    when(dataRepository.findById(Mockito.any())).thenReturn(Optional.of(data));
    var command = new WidgetUpdateCommand(
      "id",
      new WidgetConfig(
        List.of(
          new WidgetProperty("first", "First", "text", "a"),
          new WidgetProperty("second", "Second", "text", "b"),
          new WidgetProperty("third", "Third", "number", 3)
        )
      )
    );

    new WidgetCommandListener(repository, metrics).listen(command);

    var captor = ArgumentCaptor.forClass(WidgetData.class);
    verify(dataRepository, times(1)).findById("id");
    verify(dataRepository, times(1)).update(captor.capture());
    var properties = (List<Map<String, Object>>) Objects.requireNonNull(
      captor.getValue().config().get("properties")
    );
    var names = properties.stream().map(prop -> prop.get("name")).toList();
    assertEquals(3, names.size());
    assertTrue(names.containsAll(List.of("first", "second", "third")));

    var eventCaptor = ArgumentCaptor.forClass(WidgetChangedEvent.class);
    verify(sender, times(1)).send(eq("media"), eventCaptor.capture());
    var event = eventCaptor.getValue();
    assertEquals("updated", event.type());
    assertEquals("command", event.source());
    assertEquals("id", event.originId());
    assertEquals("id", event.widget().id());
    var sentConfig = event.widget().config();
    assertEquals("a", sentConfig.getProperty("first").orElseThrow().value());
    assertEquals("b", sentConfig.getProperty("second").orElseThrow().value());
    assertEquals(3, sentConfig.getProperty("third").orElseThrow().value());
  }
}

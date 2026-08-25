package io.github.opendonationassistant.widget.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.opendonationassistant.widget.UpdateWidgetRequest;
import io.github.opendonationassistant.widget.commands.ReorderCommand;
import io.github.opendonationassistant.widget.eventbus.WidgetChangedEventSender;
import io.github.opendonationassistant.widget.metrics.WidgetMetrics;
import io.github.opendonationassistant.widget.repository.WidgetData;
import io.github.opendonationassistant.widget.repository.WidgetDataRepository;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micronaut.security.authentication.Authentication;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WidgetControllerTest {

  WidgetDataRepository dataRepository = mock(WidgetDataRepository.class);
  WidgetChangedEventSender sender = mock(WidgetChangedEventSender.class);
  SimpleMeterRegistry registry = new SimpleMeterRegistry();
  WidgetMetrics metrics = new WidgetMetrics(registry);
  WidgetRepository repository = new WidgetRepository(
    dataRepository,
    sender,
    metrics
  );
  WidgetController controller = new WidgetController(repository, metrics);

  Authentication auth = mock(Authentication.class);

  @BeforeEach
  public void setUp() {
    when(auth.getAttributes())
      .thenReturn(Map.of("preferred_username", "testuser"));
  }

  @Test
  public void countsWidgetUpdate() {
    var data = new WidgetData(
      "id",
      "media",
      0,
      "name",
      "testuser",
      Map.of(),
      true,
      false,
      List.of()
    );
    when(dataRepository.findByOwnerIdAndId("testuser", "id"))
      .thenReturn(Optional.of(data));

    controller.update("id", new UpdateWidgetRequest(), auth);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.UPDATED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          "media",
          WidgetMetrics.RECIPIENT_ID_TAG,
          "testuser"
        )
        .count()
    );
  }

  @Test
  public void countsWidgetDelete() {
    var data = new WidgetData(
      "id",
      "media",
      0,
      "name",
      "testuser",
      Map.of(),
      true,
      false,
      List.of()
    );
    when(dataRepository.findByOwnerIdAndId("testuser", "id"))
      .thenReturn(Optional.of(data));

    controller.delete("id", auth);

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.DELETED_METRIC_NAME,
          WidgetMetrics.TYPE_TAG,
          "media",
          WidgetMetrics.RECIPIENT_ID_TAG,
          "testuser"
        )
        .count()
    );
  }

  @Test
  public void countsWidgetReorder() {
    when(dataRepository.listByOwnerIdAndDeleted("testuser", false))
      .thenReturn(List.of());

    controller.reorder(auth, new ReorderCommand(List.of("id")));

    assertEquals(
      1,
      registry
        .counter(
          WidgetMetrics.REORDERED_METRIC_NAME,
          WidgetMetrics.RECIPIENT_ID_TAG,
          "testuser"
        )
        .count()
    );
  }
}
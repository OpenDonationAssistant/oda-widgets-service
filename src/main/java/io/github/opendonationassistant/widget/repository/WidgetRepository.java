package io.github.opendonationassistant.widget.repository;

import com.fasterxml.uuid.Generators;
import io.github.opendonationassistant.commons.logging.ODALogger;
import io.github.opendonationassistant.events.widget.WidgetChangedEvent;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Singleton
public class WidgetRepository {

  public static final String PAYMENT_ALERTS_TYPE = "payment-alerts";
  public static final String PLAYER_INFO_TYPE = "player-info";
  public static final String DONATION_TIMER_TYPE = "donation-timer";
  public static final String DONATION_GOAL_TYPE = "donationgoal";
  public static final String REEL_TYPE = "reel";
  public static final String DONATERS_TOP_LIST_TYPE = "donaters-top-list";

  private final ODALogger log = new ODALogger(this);
  private final WidgetDataRepository repository;
  private final WidgetChangedNotificationSender notificationSender;

  @Inject
  public WidgetRepository(
    WidgetDataRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    this.repository = repository;
    this.notificationSender = notificationSender;
  }

  private Widget convert(WidgetData data) {
    return new Widget(data, repository, notificationSender);
  }

  public Widget create(
    String type,
    Integer sortOrder,
    String name,
    String recipientId
  ) {
    log.info("Adding widget", Map.of("type", type, "recipientId", recipientId));
    var data = new WidgetData(
      Generators.timeBasedEpochGenerator().generate().toString(),
      type,
      sortOrder,
      name,
      recipientId,
      Map.of(),
      true,
      false
    );
    repository.save(data);
    var widget = new Widget(data, repository, notificationSender);
    notificationSender.send(
      data.type(),
      new WidgetChangedEvent("created", widget.asDto())
    );
    return widget;
  }

  public Optional<Widget> findById(String id) {
    return repository.findById(id).map(this::convert);
  }

  public List<Widget> listByOwnerId(String ownerId) {
    return repository
      .listByOwnerIdAndDeleted(ownerId, false)
      .stream()
      .map(this::convert)
      .toList();
  }

  public List<Widget> all() {
    return repository.findAll().stream().map(this::convert).toList();
  }

  public Optional<Widget> findByOwnerIdAndId(String ownerId, String id) {
    return repository.findByOwnerIdAndId(ownerId, id).map(this::convert);
  }

  public void updateWidget(String type, Function<Widget, Widget> updateFn) {
    log.info("Running update", Map.of("type", type));
    repository
      .findAll()
      .stream()
      .map(this::convert)
      .filter(widget -> type.equals(widget.type()))
      .filter(widget -> !widget.hasNoProperties())
      .map(updateFn)
      .forEach(Widget::save);
    log.info("Update finished", Map.of());
  }
}

package io.github.opendonationassistant.widget.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface WidgetRepository extends CrudRepository<Widget, String> {
  abstract List<Widget> find(String ownerId);

  abstract Optional<Widget> find(String ownerId, String id);

  public static final String PAYMENT_ALERTS_TYPE = "payment-alerts";
  public static final String PLAYER_INFO_TYPE = "player-info";
  public static final String DONATION_TIMER_TYPE = "donation-timer";
  public static final String DONATION_GOAL_TYPE = "donationgoal";
  public static final String REEL_TYPE = "reel";
  public static final String DONATERS_TOP_LIST_TYPE = "donaters-top-list";

  public default void updateWidget(
    String type,
    Function<Widget, Widget> updateFn
  ) {
    findAll()
      .stream()
      .filter(widget -> type.equals(widget.getType()))
      .filter(widget -> !widget.hasNoProperties())
      .map(updateFn)
      .forEach(this::update);
  }
}

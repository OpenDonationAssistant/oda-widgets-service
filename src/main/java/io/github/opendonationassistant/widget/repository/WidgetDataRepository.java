package io.github.opendonationassistant.widget.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface WidgetDataRepository
  extends CrudRepository<WidgetData, String> {
  List<WidgetData> listByOwnerIdAndDeleted(String ownerId, boolean deleted);
  Optional<WidgetData> findByOwnerIdAndId(String ownerId, String id);
}

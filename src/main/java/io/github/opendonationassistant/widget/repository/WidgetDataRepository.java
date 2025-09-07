package io.github.opendonationassistant.widget.repository;

import java.util.List;
import java.util.Optional;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface WidgetDataRepository extends CrudRepository<WidgetData, String> {
  List<WidgetData> listByOwnerId(String ownerId);
  Optional<WidgetData> findByOwnerIdAndId(String ownerId, String id);
}

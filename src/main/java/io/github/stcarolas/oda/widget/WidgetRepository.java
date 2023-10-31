package io.github.stcarolas.oda.widget;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface WidgetRepository extends CrudRepository<Widget, String> {
  abstract java.util.List<Widget> find(String ownerId);
  abstract java.util.Optional<Widget> find(String ownerId, String id);
}

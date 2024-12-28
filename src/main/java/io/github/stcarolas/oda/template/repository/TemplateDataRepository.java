package io.github.stcarolas.oda.template.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TemplateDataRepository
  extends CrudRepository<TemplateData, String> {
  public List<TemplateData> findByOwnerIdAndWidgetType(
    String ownerId,
    String widgetType
  );
}

package io.github.stcarolas.oda.template.repository;

import com.fasterxml.uuid.Generators;
import io.github.stcarolas.oda.template.Template;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

public class TemplateRepository {

  private TemplateDataRepository dataRepository;

  @Inject
  public TemplateRepository(TemplateDataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }

  public void create(
    String widgetType,
    String showcase,
    List<Map<String, Object>> properties
  ) {
    create("ODA", widgetType, showcase, properties);
  }

  public void create(
    String ownerId,
    String widgetType,
    String showcase,
    List<Map<String, Object>> properties
  ) {
    var id = Generators.timeBasedEpochGenerator().generate().toString();
    dataRepository.save(
      new TemplateData(id, ownerId, widgetType, showcase, properties)
    );
  }

  public List<Template> listSystem(String widgetType) {
    return this.list("ODA", widgetType);
  }

  public List<Template> list(String ownerId, String widgetType) {
    return dataRepository
      .findByOwnerIdAndWidgetType(ownerId, widgetType)
      .stream()
      .map(data -> new Template(data, dataRepository))
      .toList();
  }
}

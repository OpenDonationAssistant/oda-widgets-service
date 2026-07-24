package io.github.opendonationassistant.template.repository;

import com.fasterxml.uuid.Generators;
import io.github.opendonationassistant.rabbit.RabbitClient;
import io.github.opendonationassistant.template.Template;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TemplateRepository {

  private TemplateDataRepository dataRepository;
  private RabbitClient eventSender;

  @Inject
  public TemplateRepository(
    TemplateDataRepository dataRepository,
    @Named("events") RabbitClient eventSender
  ) {
    this.dataRepository = dataRepository;
    this.eventSender = eventSender;
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
      new TemplateData(id, ownerId, widgetType, showcase, properties, false)
    );
  }

  public Optional<Template> get(String ownerId, String id) {
    return dataRepository.findByOwnerIdAndId(ownerId, id).map(this::convert);
  }

  public List<Template> listSystem(String widgetType) {
    return this.list("ODA", widgetType);
  }

  public List<Template> list(String ownerId, String widgetType) {
    return dataRepository
      .findByOwnerIdAndWidgetType(ownerId, widgetType)
      .stream()
      .map(this::convert)
      .toList();
  }

  private Template convert(TemplateData data) {
    return new Template(data, dataRepository, eventSender);
  }
}

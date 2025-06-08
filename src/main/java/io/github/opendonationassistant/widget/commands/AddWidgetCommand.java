package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.events.widget.WidgetChangedEvent;
import io.github.opendonationassistant.events.widget.WidgetChangedNotificationSender;
import io.github.opendonationassistant.widget.repository.Widget;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AddWidgetCommand extends BaseController {

  private final WidgetRepository repository;
  private final WidgetChangedNotificationSender notificationSender;

  public AddWidgetCommand(
    WidgetRepository repository,
    WidgetChangedNotificationSender notificationSender
  ) {
    this.repository = repository;
    this.notificationSender = notificationSender;
  }

  @Post("/widgets/commands/add")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  public HttpResponse<Widget> addWidget(
    Authentication auth,
    @Body NewWidgetRequest request
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return HttpResponse.unauthorized();
    }
    var widget = new Widget();
    widget.setType(request.type());
    widget.setId(UUID.randomUUID().toString()); // todo change to uuidv7
    widget.setName(
      Optional.ofNullable(request.name()).orElseGet(() -> request.type())
    );
    widget.setOwnerId(ownerId.get());
    widget.setSortOrder(request.sortOrder() != null ? request.sortOrder() : 0);
    widget.setConfig(new HashMap<String, Object>());
    widget.setEnabled(true);
    repository.save(widget);

    if (request.type() != null) {
      notificationSender.send(
        request.type(),
        new WidgetChangedEvent("created", widget.asDto())
      );
    }

    return HttpResponse.ok();
  }

  @Serdeable
  public static record NewWidgetRequest(
    String type,
    Integer sortOrder,
    String name
  ) {}
}

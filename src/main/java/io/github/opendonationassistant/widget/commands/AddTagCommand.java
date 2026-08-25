package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.widget.api.AddTagApi;
import io.github.opendonationassistant.widget.metrics.WidgetMetrics;
import io.github.opendonationassistant.widget.repository.WidgetRepository;
import io.github.opendonationassistant.widget.view.WidgetDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Controller
public class AddTagCommand extends BaseController implements AddTagApi {

  private final WidgetRepository repository;
  private final WidgetMetrics metrics;

  @Inject
  public AddTagCommand(WidgetRepository repository, WidgetMetrics metrics) {
    this.repository = repository;
    this.metrics = metrics;
  }

  public CompletableFuture<HttpResponse<WidgetDto>> addTag(
    Authentication auth,
    @Body AddTagRequest request
  ) {
    var ownerId = getOwnerId(auth);
    if (ownerId.isEmpty()) {
      return CompletableFuture.completedFuture(HttpResponse.unauthorized());
    }
    return CompletableFuture.supplyAsync(() ->
      repository
        .findByOwnerIdAndId(ownerId.get(), request.id())
        .map(widget -> {
          var saved = widget.addTag(request.tag()).save("manual", null);
          metrics.widgetTagAdded(saved.type(), ownerId.get());
          return saved;
        })
        .map(WidgetDto::from)
        .map(HttpResponse::ok)
        .orElse(HttpResponse.notFound())
    );
  }
}

package io.github.opendonationassistant.widget.commands;

import io.github.opendonationassistant.commons.micronaut.BaseController;
import io.github.opendonationassistant.widget.api.AddTagApi;
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

  @Inject
  public AddTagCommand(WidgetRepository repository) {
    this.repository = repository;
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
        .map(widget -> widget.addTag(request.tag()).save("manual", null))
        .map(WidgetDto::from)
        .map(HttpResponse::ok)
        .orElse(HttpResponse.notFound())
    );
  }
}

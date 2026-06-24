package io.github.opendonationassistant.widget.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import java.util.concurrent.CompletableFuture;
import org.jspecify.annotations.Nullable;

public interface DumpConfigsApi {
  @Post("/widgets/commands/dump-configs")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  CompletableFuture<HttpResponse<Void>> dumpConfigs(
    Authentication auth,
    @Body DumpConfigsRequest request
  );

  @Serdeable
  record DumpConfigsRequest(
    @Nullable String widgetType,
    @Nullable String widgetId
  ) {}
}

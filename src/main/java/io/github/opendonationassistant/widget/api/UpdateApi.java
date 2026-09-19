package io.github.opendonationassistant.widget.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

public interface UpdateApi {
  @Post("/update")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<Void> runUpdate(Authentication auth);
}

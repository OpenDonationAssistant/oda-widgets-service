package io.github.opendonationassistant.widget.api;

import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

public interface UpdateApi {

  @Post("/update")
  @Secured(SecurityRule.IS_ANONYMOUS)
  void runUpdate();
}

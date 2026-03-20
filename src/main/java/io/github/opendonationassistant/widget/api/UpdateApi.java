package io.github.opendonationassistant.widget.api;

import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "update", description = "Widget update operations")
public interface UpdateApi {

  @Hidden
  @Operation(summary = "Run update", description = "Runs widget update")
  @ApiResponse(responseCode = "200", description = "Update completed successfully")
  @Post("/update")
  @Secured(SecurityRule.IS_ANONYMOUS)
  void runUpdate();
}

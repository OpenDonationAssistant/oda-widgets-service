package io.github.opendonationassistant.template.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface DeleteTemplateApi {
  @Operation(
    summary = "Delete template",
    description = "Deletes a template by id"
  )
  @ApiResponse(
    responseCode = "200",
    description = "Template deleted successfully"
  )
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @ApiResponse(responseCode = "404", description = "Template not found")
  @Post("/templates/commands/delete-template")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<Void> deleteTemplate(
    Authentication auth,
    @Body DeleteTemplateCommand command
  );

  @Serdeable
  public static record DeleteTemplateCommand(String id) {}
}

package io.github.opendonationassistant.template.api;

import io.github.opendonationassistant.template.view.TemplateDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

public interface TemplateApi {
  @Operation(
    summary = "List templates",
    description = "Lists all templates for the authenticated user"
  )
  @Parameter(name = "widget", description = "Widget type to filter templates")
  @ApiResponse(
    responseCode = "200",
    description = "Templates retrieved successfully",
    content = @Content(
      array = @ArraySchema(schema = @Schema(implementation = TemplateDto.class))
    )
  )
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @Get("/templates")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<List<TemplateDto>> listTemplates(
    Authentication auth,
    @QueryValue("widget") String widgetType
  );
}

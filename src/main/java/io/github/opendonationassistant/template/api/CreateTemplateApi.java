package io.github.opendonationassistant.template.api;

import io.github.opendonationassistant.template.view.TemplateDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.Map;

public interface CreateTemplateApi {
  @Operation(
    summary = "Create template",
    description = "Creates a new template"
  )
  @RequestBody(
    content = @Content(
      schema = @Schema(implementation = CreateTemplateCommand.class)
    )
  )
  @ApiResponse(
    responseCode = "200",
    description = "Template created successfully"
  )
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @Post("/templates/commands/create")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<TemplateDto> execute(
    Authentication auth,
    @Body CreateTemplateCommand command
  );

  @io.micronaut.serde.annotation.Serdeable
  record CreateTemplateCommand(
    String widgetType,
    String showcase,
    List<Map<String, Object>> properties
  ) {}
}

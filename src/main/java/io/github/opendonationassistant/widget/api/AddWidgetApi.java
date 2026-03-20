package io.github.opendonationassistant.widget.api;

import io.github.opendonationassistant.widget.view.WidgetDto;
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
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "widgets", description = "Widget management operations")
public interface AddWidgetApi {
  @Operation(summary = "Add widget", description = "Creates a new widget")
  @RequestBody(
    content = @Content(
      schema = @Schema(implementation = NewWidgetRequest.class)
    )
  )
  @ApiResponse(
    responseCode = "200",
    description = "Widget created successfully",
    content = @Content(schema = @Schema(implementation = WidgetDto.class))
  )
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @Post("/widgets/commands/add")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<WidgetDto> addWidget(
    Authentication auth,
    @Body NewWidgetRequest request
  );

  @io.micronaut.serde.annotation.Serdeable
  record NewWidgetRequest(String type, Integer sortOrder, String name) {}
}

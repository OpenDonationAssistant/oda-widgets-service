package io.github.opendonationassistant.widget.api;

import io.github.opendonationassistant.widget.view.WidgetDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface ToggleWidgetApi {
  @Operation(summary = "Toggle widget", description = "Toggles a widget by ID")
  @RequestBody(
    content = @Content(
      schema = @Schema(implementation = ToogleWidgetRequest.class)
    )
  )
  @ApiResponse(
    responseCode = "200",
    description = "Widget toggled successfully",
    content = @Content(schema = @Schema(implementation = WidgetDto.class))
  )
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @ApiResponse(responseCode = "404", description = "Widget not found")
  @Post("/widgets/commands/toggle")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<WidgetDto> toggleWidget(
    Authentication auth,
    @Body ToogleWidgetRequest request
  );

  @Serdeable
  record ToogleWidgetRequest(String id) {}
}

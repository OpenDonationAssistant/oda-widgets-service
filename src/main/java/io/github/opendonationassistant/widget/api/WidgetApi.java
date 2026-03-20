package io.github.opendonationassistant.widget.api;

import io.github.opendonationassistant.widget.UpdateWidgetRequest;
import io.github.opendonationassistant.widget.commands.ReorderCommand;
import io.github.opendonationassistant.widget.view.WidgetDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
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
import java.util.List;

@Tag(name = "widgets", description = "Widget management operations")
public interface WidgetApi {

  @Operation(summary = "Reorder widgets", description = "Reorders widgets for the authenticated user")
  @RequestBody(content = @Content(schema = @Schema(implementation = ReorderCommand.class)))
  @ApiResponse(responseCode = "200", description = "Widgets reordered successfully")
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @Post("/widgets/commands/reorder")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<Void> reorder(Authentication auth, @Body ReorderCommand command);

  @Operation(summary = "Delete widget", description = "Deletes a widget by ID")
  @ApiResponse(responseCode = "200", description = "Widget deleted successfully")
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @ApiResponse(responseCode = "404", description = "Widget not found")
  @Delete("/widgets/{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<Void> delete(@PathVariable("id") String id, Authentication auth);

  @Operation(summary = "Update widget", description = "Updates a widget by ID")
  @RequestBody(content = @Content(schema = @Schema(implementation = UpdateWidgetRequest.class)))
  @ApiResponse(responseCode = "200", description = "Widget updated successfully", content = @Content(schema = @Schema(implementation = WidgetDto.class)))
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @ApiResponse(responseCode = "404", description = "Widget not found")
  @Patch("/widgets/{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<WidgetDto> update(
    @PathVariable("id") String id,
    @Body UpdateWidgetRequest request,
    Authentication auth
  );

  @Operation(summary = "List widgets", description = "Lists all widgets for the authenticated user")
  @ApiResponse(responseCode = "200", description = "Widgets retrieved successfully", content = @Content(schema = @Schema(implementation = WidgetDto.class)))
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @Get("/widgets")
  @Secured(SecurityRule.IS_ANONYMOUS)
  HttpResponse<List<WidgetDto>> list(Authentication auth);

  @Operation(summary = "Get widget", description = "Gets a widget by ID")
  @ApiResponse(responseCode = "200", description = "Widget retrieved successfully", content = @Content(schema = @Schema(implementation = WidgetDto.class)))
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @ApiResponse(responseCode = "404", description = "Widget not found")
  @Get("/widgets/{id}")
  @Secured(SecurityRule.IS_AUTHENTICATED)
  HttpResponse<WidgetDto> get(@PathVariable("id") String id, Authentication auth);
}

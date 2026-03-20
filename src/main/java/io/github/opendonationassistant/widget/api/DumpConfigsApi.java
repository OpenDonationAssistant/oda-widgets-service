package io.github.opendonationassistant.widget.api;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "admin", description = "Administrative operations")
public interface DumpConfigsApi {

  @Hidden
  @Operation(summary = "Dump widget configs", description = "Dumps all widget configurations")
  @RequestBody(content = @Content(schema = @Schema(implementation = DumpConfigsRequest.class)))
  @ApiResponse(responseCode = "200", description = "Configs dumped successfully")
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  @Post("/admin/widgets/dump")
  @Secured(SecurityRule.IS_ANONYMOUS)
  void dumpCongigs(@Body DumpConfigsRequest request);

  @io.micronaut.serde.annotation.Serdeable
  record DumpConfigsRequest(String widgetType) {}
}

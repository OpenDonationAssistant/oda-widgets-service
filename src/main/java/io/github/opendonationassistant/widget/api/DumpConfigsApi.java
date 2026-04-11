package io.github.opendonationassistant.widget.api;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.serde.annotation.Serdeable;

public interface DumpConfigsApi {
  @Post("/admin/widgets/dump")
  @Secured(SecurityRule.IS_ANONYMOUS)
  void dumpCongigs(@Body DumpConfigsRequest request);

  @Serdeable
  record DumpConfigsRequest(String widgetType) {}
}

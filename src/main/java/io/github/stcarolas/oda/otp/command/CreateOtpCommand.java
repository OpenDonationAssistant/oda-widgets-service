package io.github.stcarolas.oda.otp.command;

import io.micronaut.serde.annotation.Serdeable;
import java.util.Map;
import java.util.UUID;

@Serdeable
public class CreateOtpCommand {

  private final String widgetId;
  private final String refreshToken;

  public CreateOtpCommand(String widgetId, String refreshToken) {
    this.widgetId = widgetId;
    this.refreshToken = refreshToken;
  }

  public CreateOtpCommandResponse execute(Map<String, String> cache) {
    var otp = UUID.randomUUID().toString();
    cache.put(otp, refreshToken);
    return new CreateOtpCommandResponse(otp);
  }
}

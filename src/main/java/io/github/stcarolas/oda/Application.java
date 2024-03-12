package io.github.stcarolas.oda;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
  info = @Info(
    title = "widget-service",
    version = "0.1",
    description = "ODA Widget Service"
  )
)
public class Application {

  public static void main(String[] args) {
    Micronaut.build(args).banner(false).start();
  }
}

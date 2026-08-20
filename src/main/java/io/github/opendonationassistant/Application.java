package io.github.opendonationassistant;

import io.github.opendonationassistant.rabbit.AMQPConfiguration;
import io.github.opendonationassistant.rabbit.Exchange;
import io.github.opendonationassistant.rabbit.RabbitClient;
import io.github.opendonationassistant.widget.UpdateController;
import io.github.opendonationassistant.widget.WidgetCommandListener;
import io.github.opendonationassistant.widget.commands.DumpConfigs;
import io.github.opendonationassistant.widget.eventbus.*;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.openapi.annotation.OpenAPIExclude;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import io.micronaut.rabbitmq.connect.ChannelPool;
import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.ArrayList;

@OpenAPIDefinition(
  info = @Info(
    title = "ODA Widget Service",
    version = "0.14.0",
    description = "ODA Widget Service",
    license = @License(
      name = "AGPL-3.0",
      url = "https://www.gnu.org/licenses/agpl-3.0.en.html"
    ),
    contact = @Contact(name = "stCarolas", email = "stcarolas@gmail.com")
  )
)
@OpenAPIExclude(classes = { DumpConfigs.class, UpdateController.class })
@Factory
public class Application {

  public static void main(String[] args) {
    Micronaut.build(args).banner(false).start();
  }

  @ContextConfigurer
  public static class Configurer implements ApplicationContextConfigurer {

    @Override
    public void configure(@NonNull ApplicationContextBuilder builder) {
      builder.defaultEnvironments("standalone");
    }
  }

  @Singleton
  public ChannelInitializer rabbitConfiguration() {
    var bindings = new ArrayList<Exchange>();
    bindings.addAll(WidgetCommandListener.BINDINGS);
    bindings.addAll(SetWidgetConfigRequestHandler.BINDINGS);
    bindings.addAll(CreateWidgetRequestHandler.BINDINGS);
    bindings.addAll(WidgetConfigRequestListener.BINDINGS);
    return new AMQPConfiguration(bindings);
  }

  @Named("events")
  @Singleton
  public RabbitClient eventSender(ChannelPool channel, ObjectMapper mapper) {
    return new RabbitClient(channel, mapper, "widgets");
  }
}

package io.github.stcarolas.oda.widget;

import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient(WidgetRabbitConfiguration.CHANGES_WIDGETS)
public interface WidgetChangedNotificationSender {
  void send(@Binding String binding, WidgetChangedEvent event);
}

package io.github.stcarolas.oda.widget;

import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RabbitClient(WidgetRabbitConfiguration.CHANGES_WIDGETS)
public interface WidgetChangedNotificationSender {
  static Logger log = LoggerFactory.getLogger(
    WidgetChangedNotificationSender.class
  );
  void _send(@Binding String binding, WidgetChangedEvent event);

  public default void send(String binding, WidgetChangedEvent event) {
    log.info("Send event: {}", event);
    this._send(binding, event);
  }
}

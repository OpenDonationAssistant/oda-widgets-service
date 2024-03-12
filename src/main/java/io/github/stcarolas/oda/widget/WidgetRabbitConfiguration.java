package io.github.stcarolas.oda.widget;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.HashMap;

@Singleton
public class WidgetRabbitConfiguration extends ChannelInitializer {

    public static final String REEL = "config.reel";
    public static final String CHANGES_WIDGETS = "changes.widgets";

    @Override
    public void initialize(Channel channel, String name) throws IOException {
        channel.exchangeDeclare(CHANGES_WIDGETS, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(REEL, true, false, false, new HashMap<>()); // (4)
        channel.queueBind(REEL, CHANGES_WIDGETS, "reel");
    }

}

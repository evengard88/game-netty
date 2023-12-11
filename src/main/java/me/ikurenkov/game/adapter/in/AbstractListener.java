package me.ikurenkov.game.adapter.in;

import com.google.gson.Gson;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.netty.channel.Channel;
import me.ikurenkov.game.domain.MessageCommand;

import java.util.Map;

public abstract class AbstractListener extends RedisPubSubAdapter<String, String> {
    private final Map<String, Channel> channelGroup;
    private final Gson mapper;

    protected AbstractListener(Map<String, Channel> channelGroup, Gson mapper) {
        this.channelGroup = channelGroup;
        this.mapper = mapper;
    }

    @Override
    public void message(String channel, String message) {
        MessageCommand messageCommand = mapper.fromJson(message, MessageCommand.class);

        Channel foundChannel = channelGroup.get(messageCommand.getChannelId());

        if (foundChannel != null) {
            executeOnChannel(foundChannel, messageCommand.getMessage());
        }
    }

    protected abstract void executeOnChannel(Channel channel, String message);
}

package com.ikurenkov.game.adapter.in;

import com.google.gson.Gson;
import com.ikurenkov.game.domain.MessageCommand;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

public abstract class AbstractListener extends RedisPubSubAdapter<String, String> {
    private final ChannelGroup channelGroup;
    private final Gson mapper;

    protected AbstractListener(ChannelGroup channelGroup, Gson mapper) {
        this.channelGroup = channelGroup;
        this.mapper = mapper;
    }

    @Override
    public void message(String channel, String message) {
        MessageCommand messageCommand = mapper.fromJson(message, MessageCommand.class);

        channelGroup.stream()
                .filter(e -> e.id().asLongText().equals(messageCommand.getChannelId())).findFirst()
                .ifPresent(nettyChanel -> executeOnChannel(nettyChanel, messageCommand.getMessage()));
    }

    protected abstract void executeOnChannel(Channel channel, String message);
}

package com.ikurenkov.game.adapter.in;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.java.Log;

@Log
public class MessageHandler extends AbstractListener {

    @Inject
    public MessageHandler(ChannelGroup channelGroup, Gson mapper) {
        super(channelGroup, mapper);
    }

    @Override
    protected void executeOnChannel(Channel channel, String message) {
        channel.writeAndFlush(message + "\n\r");
    }
}
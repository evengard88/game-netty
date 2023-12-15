package me.ikurenkov.game.adapter.in;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.netty.channel.Channel;
import lombok.extern.java.Log;
import me.ikurenkov.game.application.ChannelMap;

@Log
public class MessageHandler extends AbstractListener {

    @Inject
    public MessageHandler(ChannelMap channelMap, Gson mapper) {
        super(channelMap, mapper);
    }

    @Override
    protected void executeOnChannel(Channel channel, String message) {
        channel.writeAndFlush(message + "\n\r");
    }
}
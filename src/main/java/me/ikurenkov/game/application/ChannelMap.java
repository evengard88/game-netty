package me.ikurenkov.game.application;

import io.netty.channel.Channel;
import io.netty.util.internal.PlatformDependent;
import lombok.experimental.Delegate;

import java.util.Map;

/**
 * Replacement of ChannelGroup, a way to find by text id instead of ChannelId
 */
public class ChannelMap implements Map<String, Channel> {
    @Delegate
    final Map<String, Channel> obj = PlatformDependent.newConcurrentHashMap();
}

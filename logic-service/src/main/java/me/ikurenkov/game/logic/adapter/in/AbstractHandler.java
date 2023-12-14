package me.ikurenkov.game.logic.adapter.in;

import com.google.gson.Gson;
import io.lettuce.core.pubsub.RedisPubSubAdapter;

import java.util.function.Consumer;

public abstract class AbstractHandler<T> extends RedisPubSubAdapter<String, String> {
    private final Gson mapper;

    protected AbstractHandler(Gson mapper) {
        this.mapper = mapper;
    }

    @Override
    public void message(String channel, String message) {

        T t = mapper.fromJson(message, messageToClass());
        execute().accept(t);
    }
    protected abstract Class<T> messageToClass();
    protected abstract Consumer<T> execute();
}

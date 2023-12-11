package me.ikurenkov.game.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class JsonMapperModule extends AbstractModule {
    @Provides
    @Singleton
    public Gson mapper() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

}
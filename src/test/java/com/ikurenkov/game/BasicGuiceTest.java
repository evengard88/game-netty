package com.ikurenkov.game;

import com.google.inject.Guice;
import com.ikurenkov.game.configuration.AppModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BasicGuiceTest {
    @BeforeAll
    public void setUp() {
        Guice.createInjector(
                        new AppModule()
                )
                .injectMembers(this);
    }
}

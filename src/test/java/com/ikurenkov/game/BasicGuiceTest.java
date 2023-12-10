package com.ikurenkov.game;

import com.google.inject.Guice;
import com.ikurenkov.game.configuration.AppDataModule;
import com.ikurenkov.game.configuration.GameConfigurationModule;
import com.ikurenkov.game.configuration.GameRulesModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BasicGuiceTest {
    @BeforeAll
    public void setUp() {
        Guice.createInjector(
                        new GameConfigurationModule(),
                        new AppDataModule(),
                        new GameRulesModule(),
                        new GameConfigurationModule())
                .injectMembers(this);
    }
}

package me.ikurenkov.game;

import com.google.inject.Guice;
import me.ikurenkov.game.configuration.AppDataModule;
import me.ikurenkov.game.configuration.GameConfigurationModule;
import me.ikurenkov.game.configuration.GameRulesModule;
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

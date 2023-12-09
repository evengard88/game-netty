package com.ikurenkov.game;

import com.ikurenkov.game.configuration.GameRulesModule;
import org.junit.jupiter.api.Test;

public class GameServiceTest {

    GameService service;
    @Test
    public void happyPath() {
        new GameRulesModule();
    }
}

package me.ikurenkov.game.logic.application.impl;

import me.ikurenkov.game.logic.domain.PlayerId;

public interface GameDeletePort {
    public void gameDeleteAll(String gameId, PlayerId id, PlayerId id2);
}

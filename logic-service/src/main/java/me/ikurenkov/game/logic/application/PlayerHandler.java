package me.ikurenkov.game.logic.application;

import me.ikurenkov.game.logic.domain.Player;

public interface PlayerHandler {
    void handle(Player p, String message);

    boolean supports(Player p);

}

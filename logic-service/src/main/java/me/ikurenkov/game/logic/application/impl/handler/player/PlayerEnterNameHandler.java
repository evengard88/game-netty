package me.ikurenkov.game.logic.application.impl.handler.player;

import com.google.common.base.Strings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.ikurenkov.game.logic.application.PlayerHandler;
import me.ikurenkov.game.logic.application.port.in.LobbyStorePort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.application.port.out.PlayerStorePort;
import me.ikurenkov.game.logic.domain.Player;

@ApplicationScoped
public class PlayerEnterNameHandler implements PlayerHandler {

    @Inject
    MessagePort messagePort;
    @Inject
    LobbyStorePort lobbyStore;
    @Inject
    PlayerStorePort playerStore;

    @Override
    public void handle(Player p, String message) {
        if (isValid(message)) {
            p.name(message);
            messagePort.sendMessage(p.getPlayerId(), STR."Welcome, \{p.getName()}!");
            messagePort.sendMessage(p.getPlayerId(), "Searching for opponent...");
            playerStore.store(p);
            lobbyStore.store(p);
        } else {
            messagePort.sendMessage(p.getPlayerId(), "Enter your name, at least one symbol");
        }
    }

    @Override
    public boolean supports(Player p) {
        return p.requiresName();
    }

    protected boolean isValid(String message) {
        return !Strings.isNullOrEmpty(message);
    }
}

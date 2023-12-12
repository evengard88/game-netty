package me.ikurenkov.game.logic.application.port.out;

import me.ikurenkov.game.logic.domain.PlayerId;

public interface LobbyPlayerRemovePort {
    PlayerId removeAndGetAny();
}

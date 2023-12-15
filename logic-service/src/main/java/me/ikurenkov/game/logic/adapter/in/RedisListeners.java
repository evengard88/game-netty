package me.ikurenkov.game.logic.adapter.in;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.ikurenkov.game.logic.application.port.in.GameStartUseCase;
import me.ikurenkov.game.logic.application.port.in.MessageReceivedUseCase;
import me.ikurenkov.game.logic.application.port.in.PlayerDisconnectsUseCase;
import me.ikurenkov.game.logic.config.PollQueueTasks;
import me.ikurenkov.game.logic.domain.Player;

import java.util.Iterator;

@ApplicationScoped
public class RedisListeners {

    @Inject
    MessageReceivedUseCase messageReceivedUseCase;
    @Inject
    PlayerDisconnectsUseCase disconnectUseCase;
    @Inject
    GameStartUseCase gameStartUseCase;

    @ConsumeEvent("inputMessageEvent")
    @Blocking
    public void inputMessageEvent(PollQueueTasks.InputMessageEvent messageEvent) {
        messageReceivedUseCase.handleMessage(
                messageEvent.getServerId(),
                messageEvent.getChannelId(),
                messageEvent.getMessage());
    }

    @ConsumeEvent("disconnectionEvent")
    @Blocking
    public void disconnectionEvent(PollQueueTasks.DisconnectionEvent disconnectionEvent) {
        disconnectUseCase.playerDisconnect(
                disconnectionEvent.getServerId(),
                disconnectionEvent.getChannelId());
    }

    @ConsumeEvent("playersFound")
    @Blocking
    public void playersFound(PollQueueTasks.PlayersFound playersFound) {
        Iterator<Player> iterator = playersFound.getPlayers().iterator();
        gameStartUseCase.startGame(iterator.next(), iterator.next());
    }
}

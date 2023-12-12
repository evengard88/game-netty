package me.ikurenkov.game.logic.application.impl.handler;


import com.google.inject.Inject;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.port.in.LobbyPlayerStorePort;
import me.ikurenkov.game.logic.application.port.out.*;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;


public class GameStartHandler implements GameHandler {
    private final LobbyPlayerStorePort lobbyPlayerStorePort;
    private final LobbyPlayerRemovePort lobbyPlayerRemovePort;
    private final GameGetPort gameGetPort;
    private final GameUpdatePort gameUpdatePort;
    private final GameSwapPort gameSwapPort;
    private final MessagePort messagePort;

    @Inject
    public GameStartHandler(LobbyPlayerStorePort lobbyPlayerStorePort, LobbyPlayerRemovePort lobbyPlayerRemovePort, GameGetPort gameGetPort, GameUpdatePort gameUpdatePort, GameSwapPort gameSwapPort, MessagePort messagePort) {
        this.lobbyPlayerStorePort = lobbyPlayerStorePort;
        this.lobbyPlayerRemovePort = lobbyPlayerRemovePort;
        this.gameGetPort = gameGetPort;
        this.gameUpdatePort = gameUpdatePort;
        this.gameSwapPort = gameSwapPort;
        this.messagePort = messagePort;
    }

    @Override
    public void handle(Game game, PlayerId id, String message) {
        if (supports(game, id)) {
            Player actor = game.getActor(id);
            messagePort.sendMessage(id, "Searching for opponent...");
            PlayerId opponentId = lobbyPlayerRemovePort.removeAndGetAny();
            if (opponentId == null) {
                lobbyPlayerStorePort.store(actor.getPlayerId());
                messagePort.sendMessage(actor.getPlayerId(), "No opponent available! Wait for opponent.");
            } else {

                Game opponentsGame = gameGetPort.get(opponentId);
                Player opponent = opponentsGame.getPlayer1();

                gameSwapPort.gameSwap(opponentId, game.getGameId());

                game.setPlayer2(opponent);
                gameUpdatePort.gameUpdate(game);

                messagePort.sendMessage(actor.getPlayerId(), STR."Your opponent is \{opponent.getName()}!");
                messagePort.sendMessage(opponent.getPlayerId(), STR."Your opponent is \{actor.getName()}!");

                messagePort.sendMessage(actor.getPlayerId(), STR."Enter your move: rock(1), paper(2) or scissors(3)");
                messagePort.sendMessage(opponent.getPlayerId(), STR."Enter your move: rock(1), paper(2) or scissors(3)");
            }
        }
    }

    @Override
    public boolean supports(Game game, PlayerId id) {
        return game.getPlayer2() == null;
    }

}

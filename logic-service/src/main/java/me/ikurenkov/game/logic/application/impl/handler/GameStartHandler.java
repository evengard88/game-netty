package me.ikurenkov.game.logic.application.impl.handler;


import com.google.inject.Inject;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.port.in.LobbyPlayerStorePort;
import me.ikurenkov.game.logic.application.port.out.LobbyPlayerGetPort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;


public class GameStartHandler implements GameHandler {
    private final LobbyPlayerStorePort lobbyPlayerStorePort;
    private final LobbyPlayerGetPort lobbyPlayerGetPort;
    private final MessagePort messagePort;

    @Inject
    public GameStartHandler(LobbyPlayerStorePort lobbyPlayerStorePort, LobbyPlayerGetPort lobbyPlayerGetPort, MessagePort messagePort) {
        this.lobbyPlayerStorePort = lobbyPlayerStorePort;
        this.lobbyPlayerGetPort = lobbyPlayerGetPort;
        this.messagePort = messagePort;
    }

    @Override
    public void handle(Game game, PlayerId id, String message) {
        if (supports(game, id)) {
            Player actor = game.getActor(id);
            messagePort.sendMessage(id, "Searching for opponent...1");
            Player opponent = lobbyPlayerGetPort.getAny();
            if (opponent == null) {
                lobbyPlayerStorePort.store(actor);
                messagePort.sendMessage(actor.getPlayerId(), "No opponent available! Wait for opponent.");
            } else {
                game.setPlayer2(opponent);

                context.setGame(game);
                secondGameContext.setGame(game);

                actor.sendMassage("Your opponent is " + secondPlayer.getName() + "!");
                secondPlayer.sendMassage("Your opponent is " + actor.getName() + "!");

                actor.sendMassage("Enter your move: rock(1), paper(2) or scissors(3)");
                secondPlayer.sendMassage("Enter your move: rock(1), paper(2) or scissors(3)");
            }
        }
    }

    @Override
    public boolean supports(Game game, PlayerId id) {
        return game.getPlayer2() == null;
    }

}

package me.ikurenkov.game.logic.application.impl.handler;

import jakarta.inject.Inject;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.impl.GameDeletePort;
import me.ikurenkov.game.logic.application.port.out.DisconnectPort;
import me.ikurenkov.game.logic.application.port.out.GameUpdatePort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.Move;
import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;

public class GameEvaluateHandler implements GameHandler {

    private final MessagePort messagePort;
    private final DisconnectPort disconnectPort;
    private final GameUpdatePort gameUpdatePort;
    private final GameDeletePort gameDeletePort;

    @Inject
    public GameEvaluateHandler(MessagePort messagePort,
                               DisconnectPort disconnectPort,
                               GameUpdatePort gameUpdatePort,
                               GameDeletePort gameDeletePort) {
        this.messagePort = messagePort;
        this.disconnectPort = disconnectPort;
        this.gameUpdatePort = gameUpdatePort;
        this.gameDeletePort = gameDeletePort;
    }

    @Override
    public void handle(Game game, PlayerId id, String message) {
        if (supports(game, id)) {
            Player player1 = game.getPlayer1();
            Player player2 = game.getPlayer2();
            messagePort.sendMessage(player1.getPlayerId().getServerId(), player1.getPlayerId().getChannelId(), "Opponent move: " + player2.getMove());
            messagePort.sendMessage(player2.getPlayerId().getServerId(), player2.getPlayerId().getChannelId(), "Opponent move: " + player1.getMove());
            switch (game(player1.getMove(), player2.getMove())) {
                case PLAYER1_WINS -> finishGameFirstWins(player1, player2, game);
                case PLAYER2_WINS -> finishGameFirstWins(player2, player1, game);
                case TIE -> {
                    restartGame(player1);
                    restartGame(player2);
                }
            }
            gameUpdatePort.gameUpdate(game);
        }
    }

    @Override
    public boolean supports(Game game, PlayerId id) {
        return game.getPlayer1().requiresResult()
                && game.getPlayer2().requiresResult();
    }

    private GameResult game(Move move, Move moveOpponent) {
        if (move == moveOpponent) {
            return GameResult.TIE;
        }
        if (move == Move.ROCK && moveOpponent == Move.SCISSORS) {
            return GameResult.PLAYER1_WINS;
        }
        if (move == Move.PAPER && moveOpponent == Move.ROCK) {
            return GameResult.PLAYER1_WINS;
        }
        if (move == Move.SCISSORS && moveOpponent == Move.PAPER) {
            return GameResult.PLAYER1_WINS;
        }
        return GameResult.PLAYER2_WINS;
    }


    private void restartGame(Player player) {
        player.removeMove();
        messagePort.sendMessage(player.getPlayerId().getServerId(),
                player.getPlayerId().getChannelId(),
                "It is a tie, try again");
    }

    private void finishGameFirstWins(Player player1, Player player2, Game game) {
        disconnectPort.disconnect(player1.getPlayerId(), "You won!");
        disconnectPort.disconnect(player2.getPlayerId(), "You lost!");
    }

    private enum GameResult {
        TIE,
        PLAYER1_WINS,
        PLAYER2_WINS
    }
}

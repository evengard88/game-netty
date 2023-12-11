package me.ikurenkov.game.application.handler;

import me.ikurenkov.game.domain.Move;
import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.GameHandler;
import me.ikurenkov.game.domain.Player;

public class GameEvaluateHandler implements GameHandler {

    @Override
    public void handle(GameContext context, String message) {
        if (supports(context)) {
            Player player1 = context.getGame().player1();
            Player player2 = context.getGame().player2();
            player1.sendMassage("Opponent move: " + player2.getMove());
            player2.sendMassage("Opponent move: " + player1.getMove());
            switch (game(player1.getMove(), player2.getMove())) {
                case PLAYER1_WINS -> finishGameFirstWins(player1, player2, context);
                case PLAYER2_WINS -> finishGameFirstWins(player2, player1, context);
                case TIE -> {
                    restartGame(player1);
                    restartGame(player2);
                }
            }
        }
    }

    @Override
    public boolean supports(GameContext context) {
        return context.getGame().player1().requiresResult()
                && context.getGame().player2().requiresResult();
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
        player.sendMassage("It is a tie, try again");
    }

    private void finishGameFirstWins(Player player1, Player player2, GameContext context) {

        context.setGame(null);
        context.setActor(null);

        player1.disconnect("You won!");
        player2.disconnect("You lost!");

    }

    private enum GameResult {
        TIE,
        PLAYER1_WINS,
        PLAYER2_WINS
    }
}

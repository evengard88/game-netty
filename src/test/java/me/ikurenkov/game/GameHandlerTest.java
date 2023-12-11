package me.ikurenkov.game;

import com.google.inject.Inject;
import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.PlayerMessagePort;
import me.ikurenkov.game.application.handler.GameEvaluateHandler;
import me.ikurenkov.game.domain.Game;
import me.ikurenkov.game.domain.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameHandlerTest extends BasicGuiceTest {

    @Inject
    private GameEvaluateHandler gameEvaluateHandler;

    @Test
    public void gameEvaluateHandlerHappyPath() {
        ArrayList<String> player1Messages = new ArrayList<>();
        ArrayList<String> player2Messages = new ArrayList<>();
        gameEvaluateHandler.handle(new MockGameContext(player1Messages, player2Messages), "123");

        Assertions.assertThat(player1Messages).contains("Opponent move: SCISSORS\n\r",
                "It is a tie, try again\n\r");
        Assertions.assertThat(player2Messages).contains("Opponent move: SCISSORS\n\r",
                "It is a tie, try again\n\r");
    }

    static class MockGameContext implements GameContext {
        private final Player player1;
        private final Player player2;

        public MockGameContext(ArrayList<String> strings, ArrayList<String> strings1) {
            player1 = new Player(new MockPort(strings)).setName("p1").setMove(Move.SCISSORS);
            player2 = new Player(new MockPort(strings1)).setName("p2").setMove(Move.SCISSORS);
        }

        @Override
        public Player getActor() {
            return player1;
        }

        @Override
        public Game getGame() {
            return new Game(player1, player2);
        }

        @Override
        public void setActor(Player actor) {

        }

        @Override
        public void setGame(Game game) {

        }
    }

    public record MockPort(ArrayList<String> strings) implements PlayerMessagePort {
        @Override
        public void say(String message) {
            strings.add(message);
        }

        @Override
        public void disconnect(String message) {
            strings.add(message);
        }
    }
}

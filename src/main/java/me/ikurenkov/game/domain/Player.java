package me.ikurenkov.game.domain;

import lombok.Getter;
import me.ikurenkov.game.application.PlayerMessagePort;
import lombok.ToString;

import java.util.Objects;

@ToString
public class Player {
    private final PlayerMessagePort port;
    private volatile PlayerState state = PlayerState.NAME_AWAIT;
    @Getter
    private volatile String name;
    @Getter
    private volatile Move move;

    public Player(PlayerMessagePort port) {
        this.port = port;
    }

    public Player setMove(Move move) {
        Objects.requireNonNull(move);
        assert (state == PlayerState.MOVE_AWAIT);
        this.move = move;
        this.state = PlayerState.MOVE_DONE;
        return this;
    }


    public Player setName(String name) {
        Objects.requireNonNull(name);
        assert (state == PlayerState.NAME_AWAIT);
        this.name = name;
        this.state = PlayerState.MOVE_AWAIT;
        return this;
    }

    public void sendMassage(String message) {
        port.say(message + "\n\r");
    }

    public void disconnect(String message) {
        port.disconnect(message + "\n\r");
    }

    public void removeMove() {
        this.move = null;
        this.state = PlayerState.MOVE_AWAIT;
    }

    public boolean requiresName() {
        return state == PlayerState.NAME_AWAIT;
    }

    public boolean requiresMove() {
        return state == PlayerState.MOVE_AWAIT;
    }

    public boolean requiresResult() {
        return state == PlayerState.MOVE_DONE;
    }

    public enum PlayerState {
        NAME_AWAIT,
        MOVE_AWAIT,
        MOVE_DONE
    }
}

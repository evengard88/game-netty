package com.ikurenkov.game.domain;

import com.ikurenkov.game.Move;
import com.ikurenkov.game.application.PlayerMessagePort;
import lombok.ToString;

import java.util.Objects;

@ToString
public class Player {
    private final PlayerMessagePort port;
    private volatile PlayerState state = PlayerState.NAME_AWAIT;
    private volatile String name;
    private volatile Move move;

    public Player(PlayerMessagePort port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public Move getMove() {
        return move;
    }

    public PlayerState getState() {
        return state;
    }

    public void setMove(Move move) {
        Objects.requireNonNull(move);
        assert (state == PlayerState.MOVE_AWAIT);
        this.move = move;
        this.state = PlayerState.MOVE_DONE;
    }


    public void setName(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        this.state = PlayerState.MOVE_AWAIT;
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

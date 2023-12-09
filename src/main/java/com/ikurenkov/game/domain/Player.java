package com.ikurenkov.game.domain;

import com.ikurenkov.game.Move;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class Player {
    private volatile PlayerState state = PlayerState.NAME_AWAIT;
    private volatile String name;
    private volatile Move move;

    public PlayerState getState() {
        return state;
    }


    public void setName(String name) {
        this.name = name;
        state = PlayerState.MOVE_AWAIT;
    }

    public void removeMove() {
        this.move = null;
        state = PlayerState.MOVE_AWAIT;
    }

    public void setMove(Move move) {
        this.move = move;
        this.state = PlayerState.MOVE_DONE;
    }

    public enum PlayerState {
        NAME_AWAIT,
        MOVE_AWAIT,
        MOVE_DONE
    }
}

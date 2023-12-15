package me.ikurenkov.game.logic.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Player {
    @Setter
    private PlayerId playerId;
    private PlayerState state = PlayerState.NAME_AWAIT;
    private String name;
    private Move move;
    @Setter
    private String gameId;


    public Player move(Move move) {
        Objects.requireNonNull(move);
        assert (state == PlayerState.MOVE_AWAIT);
        this.move = move;
        this.state = PlayerState.MOVE_DONE;
        return this;
    }


    public Player name(String name) {
        Objects.requireNonNull(name);
        assert (state == PlayerState.NAME_AWAIT);
        this.name = name;
        this.state = PlayerState.MOVE_AWAIT;
        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(playerId, player.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId);
    }

    public enum PlayerState {
        NAME_AWAIT,
        MOVE_AWAIT,
        MOVE_DONE
    }
}

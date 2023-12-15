package me.ikurenkov.game.logic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Game {
    private String gameId;
    private Player player1;
    private Player player2;

    public final Player getOpponent(Player actor) {
        return (Objects.equals(player1, actor)) ? player2 : player1;
    }

    public final Player getActor(PlayerId actor) {
        PlayerId playerId = Optional.ofNullable(player1).map(Player::getPlayerId).orElse(null);
        return (Objects.equals(playerId, actor)) ? player1 : player2;
    }
}

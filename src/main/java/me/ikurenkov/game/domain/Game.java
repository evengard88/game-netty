package me.ikurenkov.game.domain;

public record Game(Player player1, Player player2) {
    public Player getOpponent(Player actor) {
        return (actor == player1) ? player2 : player1;
    }
}

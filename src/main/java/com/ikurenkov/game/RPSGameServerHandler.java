package com.ikurenkov.game;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.Striped;
import com.google.inject.Inject;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.java.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;


@Log
@ChannelHandler.Sharable
public class RPSGameServerHandler extends SimpleChannelInboundHandler<String> {
    private static final AttributeKey<Player> PLAYER_ATTRIBUTE_KEY = AttributeKey.valueOf("player");
    private static final AttributeKey<Player> OPPONENT_ATTRIBUTE_KEY = AttributeKey.valueOf("opponent");
    private final BlockingQueue<Player> lobby;
    private final static Striped<Lock> striped = Striped.lazyWeakLock(2);

    @Inject
    RPSGameServerHandler(BlockingQueue<Player> lobby) {
        this.lobby = lobby;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        ctx.channel().attr(PLAYER_ATTRIBUTE_KEY).set(new Player(ctx.channel()));
        ctx.writeAndFlush("Hello! Enter your name, at least one symbol\n\r");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) {
        log.info("handler, id = " + ctx.channel().id().asLongText()
                + " player " + ctx.channel().attr(PLAYER_ATTRIBUTE_KEY));
        Player player = ctx.channel().attr(PLAYER_ATTRIBUTE_KEY).get();
        if (player.getName() == null) {
            if (!validateNameMessage(message)) {
                ctx.writeAndFlush("Enter your name, at least one symbol\n\r");
                return;
            }
            player.setName(message);
            ctx.writeAndFlush("Welcome, " + player.getName() + "!\n\r");
            ctx.writeAndFlush("Searching for opponent...\n\r");
            Player secondPlayer = lobby.poll();
            if (secondPlayer == null) {
                Lock playerQueue = striped.get("playerQueue");
                try {
                    playerQueue.lock();
                    secondPlayer = lobby.poll();
                    if (secondPlayer == null) {
                        lobby.add(player);
                    }
                } finally {
                    playerQueue.unlock();
                }

                lobby.offer(player);
                player.getChanel().writeAndFlush("No opponent available! Wait for opponent.\n\r");
            }
            if (secondPlayer != null) {
                setOpponents(player, secondPlayer);
            }
            return;
        }

        Player opponent = ctx.channel().attr(OPPONENT_ATTRIBUTE_KEY).get();
        String gameLockId = (opponent.getChanel().compareTo(player.getChanel()) > 0 ? opponent : player).getChanel().id().asLongText();

        Lock gameLock = striped.get(gameLockId);
        try {
            gameLock.lock();
            if (player.getMove() == null) {
                if (!validateMoveMessage(message)) {
                    ctx.writeAndFlush("Enter your move: rock(1), paper(2) or scissors(3)\n\r");
                    return;
                }
                player.setMove(Move.findByNameOrValue(message));
                ctx.writeAndFlush("Your move is: " + player.getMove() + "\n\r");
                if (opponent.getMove() != null) {
                    player.getChanel().write("Opponent move: " + opponent.getMove() + "\n\r");
                    opponent.getChanel().write("Opponent move: " + player.getMove() + "\n\r");
                    switch (game(player.getMove(), opponent.getMove())) {
                        case PLAYER1_WINS -> finishGameContextFirstWins(player, opponent);
                        case PLAYER1_lOSES -> finishGameContextFirstWins(opponent, player);
                        default -> {
                            player.setMove(null);
                            opponent.setMove(null);
                            player.getChanel().writeAndFlush("It is a tie, try again\n\r");
                            opponent.getChanel().writeAndFlush("It is a tie, try again\n\r");
                        }
                    }
                }
            }
        } finally {
            gameLock.unlock();
        }
    }

    private void setOpponents(Player player, Player secondPlayer) {
        player.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(secondPlayer);
        secondPlayer.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(player);

        player.getChanel().writeAndFlush("Your opponent is " + secondPlayer.getName() + "!\n\r");
        secondPlayer.getChanel().writeAndFlush("Your opponent is " + player.getName() + "!\n\r");

        player.getChanel().writeAndFlush("Enter your move: rock(1), paper(2) or scissors(3)\n\r");
        secondPlayer.getChanel().writeAndFlush("Enter your move: rock(1), paper(2) or scissors(3)\n\r");
    }

    private GameResult game(Move move, Move moveOpponent) {
        // rock1 paper2 scissors 3
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
        return GameResult.PLAYER1_lOSES;
    }

    private boolean validateNameMessage(String msg) {
        return !Strings.isNullOrEmpty(msg);
    }

    private boolean validateMoveMessage(String msg) {
        return Move.findByNameOrValue(msg) != null;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("inactive handler, id = " + ctx.channel().id());
        finishOpponentContextWithVictory(ctx);
        lobby.remove(ctx.channel().attr(PLAYER_ATTRIBUTE_KEY).get());
        super.channelInactive(ctx);
    }

    private void finishGameContextFirstWins(Player playerFirst, Player playerSecond) {
        playerSecond.getChanel().attr(PLAYER_ATTRIBUTE_KEY).set(null);
        playerSecond.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(null);

        playerFirst.getChanel().attr(PLAYER_ATTRIBUTE_KEY).set(null);
        playerFirst.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(null);

        playerSecond.getChanel().writeAndFlush("You lost!\n\r").addListener(ChannelFutureListener.CLOSE);
        playerFirst.getChanel().writeAndFlush("You won!\n\r").addListener(ChannelFutureListener.CLOSE);
    }

    private void finishOpponentContextWithVictory(ChannelHandlerContext ctx) {
        Player opponent = ctx.channel().attr(OPPONENT_ATTRIBUTE_KEY).getAndSet(null);
        if (opponent != null) {
            opponent.getChanel()
                    .writeAndFlush("Your opponent left! You won!\n\r")
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}

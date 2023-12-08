package com.ikurenkov.game;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.java.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Log
@ChannelHandler.Sharable
public class RPSGameServerHandler extends SimpleChannelInboundHandler<String> {
    private static final AttributeKey<Player> PLAYER_ATTRIBUTE_KEY = AttributeKey.valueOf("player");
    private static final AttributeKey<Player> OPPONENT_ATTRIBUTE_KEY = AttributeKey.valueOf("opponent");
    private final BlockingQueue<Player> lobby;

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
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws InterruptedException {
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
            Player secondPlayer = lobby.poll(5, TimeUnit.SECONDS);
            if (secondPlayer == null) {
                lobby.add(player);
                player.getChanel().writeAndFlush("No opponent available! Wait for opponent.\n\r");
            } else {
                player.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(secondPlayer);
                secondPlayer.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(player);

                player.getChanel().writeAndFlush("Your opponent is " + secondPlayer.getName() + "!\n\r");
                secondPlayer.getChanel().writeAndFlush("Your opponent is " + player.getName() + "!\n\r");

                player.getChanel().writeAndFlush("Enter your move: rock(1), paper(2) or scissors(3)\n\r");
                secondPlayer.getChanel().writeAndFlush("Enter your move: rock(1), paper(2) or scissors(3)\n\r");
            }
            return;
        }
        Player opponent = ctx.channel().attr(OPPONENT_ATTRIBUTE_KEY).get();
        if (player.getMove() == null && opponent == null) {
            ctx.writeAndFlush("No opponent available! Wait for opponent.\n\r");
            return;
        }
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
                    case PLAYER1_WINS -> {
                        player.getChanel().writeAndFlush("You won!\n\r");
                        opponent.getChanel().writeAndFlush("You lost!\n\r");

                        player.getChanel().attr(PLAYER_ATTRIBUTE_KEY).set(null);
                        player.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(null);

                        opponent.getChanel().attr(PLAYER_ATTRIBUTE_KEY).set(null);
                        opponent.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(null);

                        player.getChanel().close();
                        opponent.getChanel().close();
                    }
                    case PLAYER1_lOSES -> {
                        player.getChanel().writeAndFlush("You lost!\n\r");
                        opponent.getChanel().writeAndFlush("You won!\n\r");

                        player.getChanel().attr(PLAYER_ATTRIBUTE_KEY).set(null);
                        player.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(null);

                        opponent.getChanel().attr(PLAYER_ATTRIBUTE_KEY).set(null);
                        opponent.getChanel().attr(OPPONENT_ATTRIBUTE_KEY).set(null);

                        player.getChanel().close();
                        opponent.getChanel().close();
                    }
                    default -> {
                        player.setMove(null);
                        opponent.setMove(null);
                        player.getChanel().writeAndFlush("It is a tie, try again\n\r");
                        opponent.getChanel().writeAndFlush("It is a tie, try again\n\r");
                    }
                }
            }
        }
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
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("unregistered handler, id = " + ctx.channel().id());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("inactive handler, id = " + ctx.channel().id());
        finishOpponentContext(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.severe("Error on " + ctx.channel().id().asLongText() + " \n\r Error: " + cause.toString());
        cause.printStackTrace();
        finishOpponentContext(ctx);
        ctx.close();
    }

    private void finishOpponentContext(ChannelHandlerContext ctx) {
        Player opponent = ctx.attr(OPPONENT_ATTRIBUTE_KEY).get();
        if (opponent != null) {
            opponent.getChanel().writeAndFlush("Your opponent left! You won!\n\r");
            opponent.getChanel().close();
        }
    }
}

package com.ikurenkov.game;

import com.ikurenkov.game.adapter.in.RPSGameServerHandler;
import io.netty.channel.DefaultChannelId;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    @Test
    public void happyPath() {
        // Create an EmbeddedChannel with the EchoHandler
        RPSGameServerHandler rpsGameServerHandler = new RPSGameServerHandler(new HashSet<>());
        EmbeddedChannel channel = new EmbeddedChannel(DefaultChannelId.newInstance(), rpsGameServerHandler);
        EmbeddedChannel channel2 = new EmbeddedChannel(DefaultChannelId.newInstance(), rpsGameServerHandler);

        assertEquals("Hello! Enter your name, at least one symbol\n\r", channel.readOutbound());
        assertEquals("Hello! Enter your name, at least one symbol\n\r", channel2.readOutbound());

        // Write a message to the channel
        String message = "Player1";
        String message2 = "Player2";
        channel.writeInbound(message);
        channel2.writeInbound(message2);

        assertEquals("Welcome, Player1!\n\r", channel.readOutbound());
        assertEquals("Searching for opponent...\n\r", channel.readOutbound());
        assertEquals("Welcome, Player2!\n\r", channel2.readOutbound());
        assertEquals("Searching for opponent...\n\r", channel2.readOutbound());

        assertEquals("No opponent available! Wait for opponent.\n\r", channel.readOutbound());
        assertEquals("Your opponent is Player1!\n\r", channel2.readOutbound());

        assertEquals("Your opponent is Player2!\n\r", channel.readOutbound());
        assertEquals("Enter your move: rock(1), paper(2) or scissors(3)\n\r", channel.readOutbound());
        assertEquals("Enter your move: rock(1), paper(2) or scissors(3)\n\r", channel2.readOutbound());

        assertNull( channel.readOutbound());
        assertNull( channel2.readOutbound());

        channel.writeInbound("1");
        channel2.writeInbound("1");

        assertEquals("Your move is: ROCK\n\r", channel.readOutbound());
        assertEquals("Your move is: ROCK\n\r", channel2.readOutbound());

        assertEquals("Opponent move: ROCK\n\r", channel.readOutbound());
        assertEquals("Opponent move: ROCK\n\r", channel2.readOutbound());

        assertEquals("It is a tie, try again\n\r", channel.readOutbound());
        assertEquals("It is a tie, try again\n\r", channel2.readOutbound());

        channel.writeInbound("1");
        channel2.writeInbound("2");

        assertEquals("Your move is: ROCK\n\r", channel.readOutbound());
        assertEquals("Your move is: PAPER\n\r", channel2.readOutbound());

        assertEquals("Opponent move: PAPER\n\r", channel.readOutbound());
        assertEquals("Opponent move: ROCK\n\r", channel2.readOutbound());

        assertEquals("You lost!\n\r", channel.readOutbound());
        assertEquals("You won!\n\r", channel2.readOutbound());


        // Ensure there are no more outbound messages
        assertNull(channel.readOutbound());
        assertNull(channel2.readOutbound());

        assertFalse(channel.isActive());
        assertFalse(channel2.isActive());
    }


}

package me.ikurenkov.game.logic.adapter.in;

import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.extern.java.Log;
import me.ikurenkov.game.logic.application.port.in.MessageReceivedUseCase;
import me.ikurenkov.game.logic.configuration.LettuceModule;

import java.util.function.Consumer;

@Log
@LettuceModule.MessageListener
public class MessageHandler extends AbstractHandler<MessageReceivedUseCase.InputMessage> {
    private final MessageReceivedUseCase messageReceivedUseCase;

    @Inject
    public MessageHandler(Gson mapper, MessageReceivedUseCase messageReceivedUseCase) {
        super(mapper);
        this.messageReceivedUseCase = messageReceivedUseCase;
    }

    @Override
    protected Class<MessageReceivedUseCase.InputMessage> messageToClass() {
        return MessageReceivedUseCase.InputMessage.class;
    }

    @Override
    protected Consumer<MessageReceivedUseCase.InputMessage> execute() {
        return (inputMessage) -> messageReceivedUseCase.handleMessage(
                inputMessage.getServerId(),
                inputMessage.getChannelId(),
                inputMessage.getMessage());
    }
}
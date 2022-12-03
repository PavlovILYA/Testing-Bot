package ru.mephi.knowledgechecker.state.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import ru.mephi.knowledgechecker.dto.telegram.income.CallbackQuery;
import ru.mephi.knowledgechecker.dto.telegram.income.Message;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.state.BotState;
import ru.mephi.knowledgechecker.state.ParamsWrapper;
import ru.mephi.knowledgechecker.state.StateContext;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractState implements BotState, ApplicationContextAware {
    // todo перевезти все константы в одно место
    private static final String NOT_IMPLEMENTED_MESSAGE = "Обработка данного действия еще не реализована 🧑🏼‍💻";
    @Autowired
    protected TelegramApiClient telegramApiClient;
    @Lazy
    @Autowired
    protected StateContext stateContext;
    protected ApplicationContext applicationContext;
    protected final Queue<BotState> availableStates = new ConcurrentLinkedQueue<>();
    @Lazy
    @Autowired
    @Qualifier("unknownState")
    protected BotState unknownState;

    @Override
    @Autowired
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected BotState getAvailableState(Class clazz) {
        for (BotState state : availableStates) {
            if (clazz.isInstance(state)) {
                return state;
            }
        }
        return unknownState;
    }

    @Override
    public void processMessage(Message message) {
        sendDefaultMessage(message.getFrom().getId());
    }

    @Override
    public void processCommand(Message message) {
        sendDefaultMessage(message.getFrom().getId());
    }

    @Override
    public void processCallbackQuery(CallbackQuery callbackQuery) {
        sendDefaultMessage(callbackQuery.getFrom().getId());
    }

    private void sendDefaultMessage(Long chatId) {
        MessageParams params  = ParamsWrapper.wrapMessageParams(chatId, NOT_IMPLEMENTED_MESSAGE);
        telegramApiClient.sendMessage(params);
    }
}

package ru.mephi.knowledgechecker.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.CallbackQuery;
import ru.mephi.knowledgechecker.dto.telegram.income.Message;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class StateContext {
    private final BotState initialState;
    private final ConcurrentMap<Long, BotState> states = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, LocalDateTime> timestamps = new ConcurrentHashMap<>();

    @Autowired
    public StateContext(@Qualifier("startMenuState") BotState initialState) {
        this.initialState = initialState;
    }

    public void processCommand(Message message) {
        BotState currentState = getState(message.getFrom().getId());
        currentState.processCommand(message);
    }

    public void processMessage(Message message) {
        BotState currentState = getState(message.getFrom().getId());
        currentState.processMessage(message);
    }

    public void processCallbackReply(CallbackQuery callbackQuery) {
        BotState currentState = getState(callbackQuery.getFrom().getId());
        currentState.processCallbackQuery(callbackQuery);
    }

    // todo поменять мапу на БД (точнее сохранить мапу для кэширования)
    private BotState getState(Long userId) {
        if (!states.containsKey(userId)) {
            // checkDB(); // todo потом тут еще в базу надо лезть проверять..
            putState(userId, initialState);
            return initialState;
        }
        return states.get(userId);
    }

    // todo поменять мапу на БД (точнее сохранить мапу для кэширования)
    public void putState(Long userId, BotState state) {
        states.put(userId, state);
        timestamps.put(userId, LocalDateTime.now());
    }
}

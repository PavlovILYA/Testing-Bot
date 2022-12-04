package ru.mephi.knowledgechecker.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.InvalidUpdateException;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.state.impl.InitialState;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class StateContext {
    private final InitialState initialState;
    private final ConcurrentMap<Long, BotState> states = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, LocalDateTime> timestamps = new ConcurrentHashMap<>();

    public StateContext(InitialState initialState) {
        this.initialState = initialState;
    }

    public void process(Update update) {
        try {
            Long userId = update.getUserId();
            BotState currentState = getState(userId);
            log.info("CURRENT STATE: {}", currentState.getClass().getName());
            currentState.process(update);
        } catch (InvalidUpdateException e) {
            log.warn(e.getMessage());
        }
    }

    // todo поменять мапу на БД (точнее сохранить мапу для кэширования)
    public BotState getState(Long userId) {
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

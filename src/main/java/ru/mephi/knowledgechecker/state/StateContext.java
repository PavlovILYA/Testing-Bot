package ru.mephi.knowledgechecker.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.DataType;
import ru.mephi.knowledgechecker.dto.telegram.income.InvalidUpdateException;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.state.impl.menu.InitialState;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class StateContext {
    private final InitialState initialState;
    private final ConcurrentMap<Long, ExtendedState> states = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, LocalDateTime> timestamps = new ConcurrentHashMap<>();

    public StateContext(InitialState initialState) {
        this.initialState = initialState;
    }

    public void process(Update update) {
        try {
            Long userId = update.getUserId();
            ExtendedState currentState = getState(userId);
            log.info("CURRENT STATE: {}", currentState.getState().getClass().getName());
            log.info("CURRENT DATA: {}", currentState.getData());
            currentState.getState().process(update, currentState.getData());
        } catch (InvalidUpdateException e) {
            log.warn(e.getMessage());
        }
    }

    // todo поменять мапу на БД (точнее сохранить мапу для кэширования)
    public ExtendedState getState(Long userId) {
        if (!states.containsKey(userId)) {
            // checkDB(); // todo потом тут еще в базу надо лезть проверять..
            putState(userId, initialState, new HashMap<>());
            return getState(userId);
        }
        return states.get(userId);
    }

    // todo поменять мапу на БД (точнее сохранить мапу для кэширования)
    public void putState(Long userId, BotState state, Map<DataType, Object> data) {
        if (!states.containsKey(userId)) {
            states.put(userId, new ExtendedState(state, data));
        } else {
            ExtendedState extendedState = states.get(userId);
            extendedState.setState(state);
            extendedState.setData(data);
        }
        timestamps.put(userId, LocalDateTime.now());
    }

    public void putState(Long userId, Map<DataType, Object> data) {
        ExtendedState extendedState = states.get(userId);
        extendedState.setData(data);
        timestamps.put(userId, LocalDateTime.now());
    }
}

package ru.mephi.knowledgechecker.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;

import java.time.LocalDateTime;
import java.util.Objects;
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

    public void process(Update update) {
        try {
            BotState currentState = Objects.requireNonNull(
                    getState(getUserId(update)));
            currentState.process(update);
        } catch (BadUpdateException e) {
            log.warn("{}", e.getMessage());
        }
    }

    private Long getUserId(Update update) throws BadUpdateException {
        if (update.getMessage() != null) {
            return update.getMessage().getFrom().getId();
        } else if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getFrom().getId();
        } else {
            throw new BadUpdateException(update.getId());
        }
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

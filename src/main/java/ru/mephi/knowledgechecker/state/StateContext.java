package ru.mephi.knowledgechecker.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class StateContext {
    private final ConcurrentMap<Long, BotState> states = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, LocalDateTime> timestamps = new ConcurrentHashMap<>();

    public void process(Update update) {
//        for (UpdateStrategy strategy : updateStrategies) {
//            if (strategy.apply(update)) {
//                strategy.process(update);
//                return;
//            }
//        }
    }

    // todo поменять мапу на БД (точнее сохранить мапу для кэширования)
    public BotState getState(Long userId) {
        if (!states.containsKey(userId)) { // Потом тут еще в базу надо лезть проверять..
//            putState(userId, initial);
            return null; // initial
        }
        return states.get(userId);
    }

    // todo поменять мапу на БД (точнее сохранить мапу для кэширования)
    public void putState(Long userId, BotState state) {
        states.put(userId, state);
        timestamps.put(userId, LocalDateTime.now());
    }
}

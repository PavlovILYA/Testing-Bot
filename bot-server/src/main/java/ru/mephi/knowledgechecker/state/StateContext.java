package ru.mephi.knowledgechecker.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.CurrentDataService;
import ru.mephi.knowledgechecker.service.UserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StateContext {

    @Autowired
    private final Map<String, BotState> states = new ConcurrentHashMap<>();
    private final UserService userService;
    private final CurrentDataService dataService;

    public void process(Update update) {
        CurrentData data = getUserData(update);
        states.get(data.getState()).process(data, update);
        log.info("CURRENT USER DATA (after process):\n{}", data);
    }

    private CurrentData getUserData(Update update) {
        Long userId = update.getUserId();
        CurrentData data = dataService.getByUserId(userId);
        if (data == null) {
            data = registerUser(update);
        }
        return data;
    }

    private CurrentData registerUser(Update update) {
        UserDto userDto = update.getCallbackQuery() != null
                ? update.getCallbackQuery().getFrom()
                : update.getMessage().getFrom();
        User user = userService.save(userDto);
        return dataService.createForUser(user);
    }

    public void saveUserData(CurrentData data) {
        dataService.update(data);
    }
}

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

import static ru.mephi.knowledgechecker.model.user.mapper.UserMapper.mapStateToBeanName;

@Slf4j
@Component
@RequiredArgsConstructor
public class StateContext {

//    private final ConcurrentMap<String, BotState> states = new ConcurrentHashMap<>();
    @Autowired
    private final Map<String, BotState> states = new ConcurrentHashMap<>();
    private final UserService userService;
    private final CurrentDataService dataService;

    public void process(Update update) {
        User user = getUser(update);
        log.info("STATES: {}", states);
        states.get(user.getState()).process(user, update);
        log.info("CURRENT USER (STATE, DATA) after process:\n{}", user);
    }

    private User getUser(Update update) {
        Long userId = update.getUserId();
        User user = userService.get(userId);
        if (user == null) {
            user = saveUser(update);
        }
        return user;
    }

    private User saveUser(Update update) {
        UserDto userDto = update.getCallbackQuery() != null
                ? update.getCallbackQuery().getFrom()
                : update.getMessage().getFrom();
        User user = userService.save(userDto);
        CurrentData data = dataService.createForUser(user);
        user.setData(data);
//        userService.save(userDto);
        user = userService.get(user.getId());
        log.info("👤 Пользователь зарегистрирован:\n{}", user);
        return user;
    }

    public void putState(Long userId, BotState state) {
        User user = userService.get(userId);
        user.setState(mapStateToBeanName(state.getClass()));
        userService.save(user);
    }

    public void saveUserData(CurrentData data) {
        dataService.update(data);
    }

    public void putStateAndSaveUserData(BotState state, CurrentData data) {
        data = dataService.update(data);
        User user = userService.get(data.getUser().getId());
        log.info("||||||| USER before: {}", user);
        user.setData(data);
        log.info("||||||| USER after: {}", user);
        user.setState(mapStateToBeanName(state.getClass()));
        userService.save(user);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.test.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.ADD_OPEN_QUESTION;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddOpenQuestionStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;
    private final TestService testService;

    public AddOpenQuestionStrategy(UserService userService, TestService testService) {
        this.userService = userService;
        this.testService = testService;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(ADD_OPEN_QUESTION);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        User user = userService.get(update.getCallbackQuery().getFrom().getId());
        if (data.get("next") == null) {
            askText(data, user);
        }
        Test test = testService.get((String) data.get("testId"));
        switch ((String) data.get("next")) {
            case "text":
                break;
            default:
        }
    }

    private void askText(Map<String, Object> data, User user) {
        MessageParams params =
                wrapMessageParams(user.getId(), "Введите содержание вопроса", null);
        data.put("next", "text");
        putStateToContext(user.getId(), data);
        telegramApiClient.sendMessage(params);
    }

    private void askCorrectAnswer(Update update, Map<String, Object> data, User user, Test test) {
        // todo: question, answer - repository/service!
        // todo: create open answer
//        MessageParams params =
//                wrapMessageParams(user.getId(), "Введите содержание вопроса", null);
//        data.put("next", "text");
//        putStateToContext(user.getId(), data);
//        telegramApiClient.sendMessage(params);
    }
}

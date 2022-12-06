package ru.mephi.knowledgechecker.strategy.impl.test.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.create.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ReadTestInfoStrategy extends AbstractMessageStrategy {
    private final TestService testService;
    private final UserService userService;

    public ReadTestInfoStrategy(TestService testService, UserService userService,
                                QuestionAddingState questionAddingState) {
        this.testService = testService;
        this.userService = userService;
        this.nextState = questionAddingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        User user = userService.get(update.getMessage().getFrom().getId());
        Test test = testService.get((String) data.get("testId"));
        String next = (String) data.get("next");
        switch (next) {
            case "title":
                readTitle(update.getMessage().getText(), data, user, test);
                break;
            case "maxQuestionsNumber":
                readMaxQuestionsNumber(Integer.parseInt(update.getMessage().getText()), data, user, test);
                break;
            default:
        }
    }

    private void readTitle(String title, Map<String, Object> data, User user, Test test) {
        test.setTitle(title);
        testService.save(test);
        MessageParams params =
                wrapMessageParams(user.getId(), "Введите полное количество отображаемых вопросов\n" +
                                "(Вопросов можно будет создать больше, тогда будет браться случайная выборка)", null);
        data.put("next", "maxQuestionsNumber");
        putStateToContext(user.getId(), data);
        telegramApiClient.sendMessage(params);
    }

    private void readMaxQuestionsNumber(Integer maxQuestionsNumber, Map<String, Object> data, User user, Test test) {
        test.setMaxQuestionsNumber(maxQuestionsNumber);
        testService.save(test);
        MessageParams params =
                wrapMessageParams(user.getId(), "Добавить вопрос \n(На данный момент сохранено n вопросов)",
                        getInlineKeyboardMarkup());
        data.put("next", "maxQuestionsNumber");
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }

    private KeyboardMarkup getInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("С вариантами")
                .callbackData(ADD_VARIABLE_QUESTION)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Открытый")
                .callbackData(ADD_OPEN_QUESTION)
                .build());
        markup.add(menu);
        menu.add(InlineKeyboardButton.builder()
                .text("✅️")
                .callbackData(TO_MAIN_MENU)
                .build());
        return wrapInlineKeyboardMarkup(markup);
    }
}

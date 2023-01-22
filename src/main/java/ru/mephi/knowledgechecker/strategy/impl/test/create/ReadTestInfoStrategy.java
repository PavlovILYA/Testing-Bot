package ru.mephi.knowledgechecker.strategy.impl.test.create;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddQuestionInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ReadTestInfoStrategy extends AbstractMessageStrategy {
    private final TestService testService;
    private final UserService userService;

    public ReadTestInfoStrategy(TestService testService, UserService userService,
                                @Lazy QuestionAddingState questionAddingState) {
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
        Test test = testService.getByUniqueTitle((String) data.get("testId"));
        String next = (String) data.get("next");
        switch (next) {
            case "title":
                readTitle(update.getMessage().getText(), data, user, test);
                break;
            case "maxQuestionsNumber":
                readMaxQuestionsNumber(Integer.parseInt(update.getMessage().getText()), data, user, test);
                break;
            default: // todo: add attachment
        }
    }

    private void readTitle(String title, Map<String, Object> data, User user, Test test) {
        test.setTitle(title);
        testService.save(test);
        String boldMessage = "Введите количество отображаемых вопросов";
        String italicMessage =
                "\n\n(Вопросов можно будет создать больше, тогда будет браться случайная выборка)";
        MessageParams params =
                wrapMessageParams(user.getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("underline", 19, 12),
                                new MessageEntity("italic", boldMessage.length(), italicMessage.length())),
                        null);
        data.put("next", "maxQuestionsNumber");
        putStateToContext(user.getId(), data);
        telegramApiClient.sendMessage(params);
    }

    private void readMaxQuestionsNumber(Integer maxQuestionsNumber, Map<String, Object> data, User user, Test test) {
        test.setMaxQuestionsNumber(maxQuestionsNumber);
        testService.save(test);
        int questionCount = test.getVariableQuestions().size();
        questionCount += test.getOpenQuestions().size();
        String boldMessage = "Добавление вопроса";
        String italicMessage =
                "\n\nНа данный момент сохранено " + questionCount + " вопросов\n" +
                "Максимальное количество отображаемых вопросов: " + test.getMaxQuestionsNumber();
        MessageParams params =
                wrapMessageParams(user.getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("italic", boldMessage.length(), italicMessage.length())),
                        getAddQuestionInlineKeyboardMarkup());
        data.remove("next");
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

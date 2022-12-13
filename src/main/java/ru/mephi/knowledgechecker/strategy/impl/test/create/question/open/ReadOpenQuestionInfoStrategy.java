package ru.mephi.knowledgechecker.strategy.impl.test.create.question.open;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.OpenQuestionService;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.Map;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddQuestionInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ReadOpenQuestionInfoStrategy extends AbstractMessageStrategy {
    private final UserService userService;
    private final TestService testService;
    private final OpenQuestionService openQuestionService;

    public ReadOpenQuestionInfoStrategy(UserService userService,
                                        TestService testService,
                                        OpenQuestionService openQuestionService,
                                        @Lazy QuestionAddingState questionAddingState) {
        this.userService = userService;
        this.testService = testService;
        this.openQuestionService = openQuestionService;
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
        switch ((String) data.get("next")) {
            case "text":
                readText(update.getMessage().getText(), data, user, test);
                break;
            case "correctAnswer":
                readCorrectAnswer(update.getMessage().getText(), data, user, test);
                break;
            default: // todo: add attachment
        }
    }

    private void readText(String text, Map<String, Object> data, User user, Test test) {
        OpenQuestion question = OpenQuestion.builder()
                .text(text)
                .test(test)
                .build();
        question = openQuestionService.save(question);
        MessageParams params =
                wrapMessageParams(user.getId(), "Введите правильный ответ", null);
        data.put("next", "correctAnswer");
        data.put("questionId", question.getId());
        putStateToContext(user.getId(), data);
        telegramApiClient.sendMessage(params);
    }

    private void readCorrectAnswer(String correctAnswer, Map<String, Object> data, User user, Test test) {
        OpenQuestion question = openQuestionService.get((Long) data.get("questionId"));
        question.setCorrectAnswer(correctAnswer);
        openQuestionService.save(question);
        int questionCount = test.getVariableQuestions().size();
        questionCount += test.getOpenQuestions().size();
        MessageParams params =
                wrapMessageParams(user.getId(),
                        "Добавить вопрос \n\n" +
                                "На данный момент сохранено " + questionCount + " вопросов\n" +
                                "Максимальное количество отображаемых вопросов: " + test.getMaxQuestionsNumber(),
                        getAddQuestionInlineKeyboardMarkup());
        data.remove("next");
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

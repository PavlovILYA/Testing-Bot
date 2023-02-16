package ru.mephi.knowledgechecker.strategy.impl.test.create.question.open;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.CreationPhaseType;
import ru.mephi.knowledgechecker.common.DataType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.OpenQuestionService;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.addingQuestionParams;
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
    public void process(Update update, Map<DataType, Object> data) throws StrategyProcessException {
        User user = userService.get(update.getMessage().getFrom().getId());
        Test test = testService.getByUniqueTitle((String) data.get(DataType.TEST_ID));
        switch ((CreationPhaseType) data.get(DataType.NEXT_CREATION_PHASE)) {
            case TEXT:
                readText(update.getMessage().getText(), data, user, test);
                break;
            case CORRECT_ANSWER:
                readCorrectAnswer(update.getMessage().getText(), data, user, test);
                break;
            default: // todo: add attachment
        }
    }

    private void readText(String text, Map<DataType, Object> data, User user, Test test) {
        OpenQuestion question = OpenQuestion.builder()
                .text(text)
                .test(test)
                .build();
        question = openQuestionService.save(question);
        String message = "Введите правильный ответ";
        MessageParams params =
                wrapMessageParams(user.getId(), message,
                        List.of(new MessageEntity(TextType.BOLD, 0, message.length()),
                                new MessageEntity(TextType.UNDERLINE, 8, 10)),
                        null);
        data.put(DataType.NEXT_CREATION_PHASE, CreationPhaseType.CORRECT_ANSWER);
        data.put(DataType.QUESTION_ID, question.getId());
        putStateToContext(user.getId(), data);
        telegramApiClient.sendMessage(params);
    }

    private void readCorrectAnswer(String correctAnswer, Map<DataType, Object> data, User user, Test test) {
        OpenQuestion question = openQuestionService.get((Long) data.get(DataType.QUESTION_ID));
        question.setCorrectAnswer(correctAnswer);
        openQuestionService.save(question);

        MessageParams params = addingQuestionParams(test, user.getId());
        data.remove(DataType.NEXT_CREATION_PHASE);
        data.computeIfAbsent(DataType.CHECK_0_QUESTIONS, k -> test.getUniqueTitle());
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

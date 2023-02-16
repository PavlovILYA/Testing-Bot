package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.VariableQuestionService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.Map;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.addingQuestionParams;
import static ru.mephi.knowledgechecker.common.Constants.CHECK_0_QUESTIONS;
import static ru.mephi.knowledgechecker.common.Constants.TO_QUESTION_ADDING;

@Component
public class ToQuestionAddingStrategy extends AbstractCallbackQueryStrategy {
    private final TestService testService;
    private final VariableQuestionService variableQuestionService;

    public ToQuestionAddingStrategy(TestService testService,
                                    VariableQuestionService variableQuestionService,
                                    @Lazy QuestionAddingState questionAddingState) {
        this.testService = testService;
        this.variableQuestionService = variableQuestionService;
        this.nextState = questionAddingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(TO_QUESTION_ADDING);
    }

    @Override
    public void process(Update update, Map<String, Object> data) throws StrategyProcessException {
        if (data.get("questionId") != null) {
            VariableQuestion lastQuestion = variableQuestionService.get((Long) data.get("questionId"));
            if (lastQuestion.getWrongAnswers().size() == 0) {
                throw new StrategyProcessException(update.getCallbackQuery().getFrom().getId(),
                        "Необходимо добавить хотя бы один неправильный вариант ответа");
            }
        }

        Test test = testService.getByUniqueTitle((String) data.get("testId"));

        MessageParams params = addingQuestionParams(test, update.getCallbackQuery().getFrom().getId());
        data.remove("next");
        data.remove("questionId"); // ???
        data.computeIfAbsent(CHECK_0_QUESTIONS, k -> test.getUniqueTitle());
        putStateToContext(update.getCallbackQuery().getFrom().getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.addingQuestionParams;
import static ru.mephi.knowledgechecker.common.Constants.TO_QUESTION_ADDING;

@Component
public class ToQuestionAddingStrategy extends AbstractCallbackQueryStrategy {

    public ToQuestionAddingStrategy(@Lazy QuestionAddingState questionAddingState) {
        this.nextState = questionAddingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(TO_QUESTION_ADDING);
    }

    @Override
    public void process(User user, Update update) throws StrategyProcessException {
        CurrentData data = user.getData();
        if (data.getVariableQuestion() != null) {
            VariableQuestion lastQuestion = data.getVariableQuestion();
            if (lastQuestion.getWrongAnswers().size() == 0) {
                throw new StrategyProcessException(update.getCallbackQuery().getFrom().getId(),
                        "Необходимо добавить хотя бы один неправильный вариант ответа");
            }
        }

        data.setNextPhase(null);
        data.setVariableQuestion(null);
        data.setNeedCheck(true);

        SendMessageParams params = addingQuestionParams(data.getTest(), user.getId());
        sendMessageAndSave(params, nextState, data);
    }
}

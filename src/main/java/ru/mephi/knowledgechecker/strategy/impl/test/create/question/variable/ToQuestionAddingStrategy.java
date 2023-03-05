package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_QUESTION_ADDING;
import static ru.mephi.knowledgechecker.common.CommonMessageParams.addingQuestionParams;

@Component
public class ToQuestionAddingStrategy extends AbstractCallbackQueryStrategy {

    public ToQuestionAddingStrategy(@Lazy QuestionAddingState questionAddingState) {
        this.nextState = questionAddingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(TO_QUESTION_ADDING.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        if (data.getVariableQuestion() != null) {
            VariableQuestion lastQuestion = data.getVariableQuestion();
            if (lastQuestion.getWrongAnswers().size() == 0) {
                throw new StrategyProcessException(data.getUser().getId(),
                        "Необходимо добавить хотя бы один неправильный вариант ответа",
                        update.getCallbackQuery().getId());
            }
        }

        data.setNextPhase(null);
        data.setVariableQuestion(null);
        data.setNeedCheck(true);

        SendMessageParams params = addingQuestionParams(data.getTest(), data.getUser().getId());
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

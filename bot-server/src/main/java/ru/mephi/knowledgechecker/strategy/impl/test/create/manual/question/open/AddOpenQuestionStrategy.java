package ru.mephi.knowledgechecker.strategy.impl.test.create.manual.question.open;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CreationPhaseType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.create.manual.question.open.OpenQuestionInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.ADD_OPEN_QUESTION;

@Component
public class AddOpenQuestionStrategy extends AbstractCallbackQueryStrategy {

    public AddOpenQuestionStrategy(@Lazy OpenQuestionInfoReadingState openQuestionInfoReadingState) {
        this.nextState = openQuestionInfoReadingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(ADD_OPEN_QUESTION.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        data.setNextPhase(CreationPhaseType.TEXT);

        String message = "Введите содержание вопроса";
        data.setState(nextState);
        sendMessageAndSave(message, data);
    }
}

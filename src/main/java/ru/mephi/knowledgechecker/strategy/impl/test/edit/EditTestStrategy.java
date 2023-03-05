package ru.mephi.knowledgechecker.strategy.impl.test.edit;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.EDIT_TEST;
import static ru.mephi.knowledgechecker.common.CommonMessageParams.addingQuestionParams;

@Component
public class EditTestStrategy extends AbstractCallbackQueryStrategy {
    public EditTestStrategy(@Lazy QuestionAddingState questionAddingState) {
        this.nextState = questionAddingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(EDIT_TEST.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        data.setNeedCheck(true);

        SendMessageParams params = addingQuestionParams(data.getTest(), data.getUser().getId());
        data.setState(nextState);
        deleteMenu(data);
        sendMessageAndSave(params, data);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.test.create.question.open;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.CreationPhaseType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.create.question.open.OpenQuestionInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.ADD_OPEN_QUESTION;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddOpenQuestionStrategy extends AbstractCallbackQueryStrategy {

    public AddOpenQuestionStrategy(@Lazy OpenQuestionInfoReadingState openQuestionInfoReadingState) {
        this.nextState = openQuestionInfoReadingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(ADD_OPEN_QUESTION.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        data.setNextPhase(CreationPhaseType.TEXT);

        String message = "Введите содержание вопроса";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                null);
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

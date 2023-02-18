package ru.mephi.knowledgechecker.strategy.impl.test.create.question.open;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.CreationPhaseType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.state.impl.test.create.question.open.OpenQuestionInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.Constants.ADD_OPEN_QUESTION;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddOpenQuestionStrategy extends AbstractCallbackQueryStrategy {

    public AddOpenQuestionStrategy(@Lazy OpenQuestionInfoReadingState openQuestionInfoReadingState) {
        this.nextState = openQuestionInfoReadingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(ADD_OPEN_QUESTION);
    }

    @Override
    public void process(User user, Update update) throws StrategyProcessException {
        CurrentData data = user.getData();
        data.setNextPhase(CreationPhaseType.TEXT);
        saveToContext(nextState, data);

        String message = "Введите содержание вопроса";
        MessageParams params = wrapMessageParams(user.getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                null);
        telegramApiClient.sendMessage(params);
    }
}

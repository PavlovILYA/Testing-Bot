package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.CreationPhaseType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.state.impl.test.create.question.variable.VariableQuestionInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.Constants.ADD_VARIABLE_QUESTION;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddVariableQuestionStrategy extends AbstractCallbackQueryStrategy {

    public AddVariableQuestionStrategy(@Lazy VariableQuestionInfoReadingState variableQuestionInfoReadingState) {
        this.nextState = variableQuestionInfoReadingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(ADD_VARIABLE_QUESTION);
    }

    @Override
    public void process(User user, Update update) throws StrategyProcessException {
        CurrentData data = user.getData();
        data.setNextPhase(CreationPhaseType.TEXT);
        saveToContext(nextState, data);

        String boldMessage = "Введите содержание вопроса";
        String italicMessage =
                "\n\nПредпочтительно добавить в формулировку вопроса варианты ответа:\n" +
                "A: Ответ 1\n" +
                "B: Ответ 2\n" +
                "etc.";
        MessageParams params = wrapMessageParams(user.getId(), boldMessage + italicMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                null);
        telegramApiClient.sendMessage(params);
    }
}

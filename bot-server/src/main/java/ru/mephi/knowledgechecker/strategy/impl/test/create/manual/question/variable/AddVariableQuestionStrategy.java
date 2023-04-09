package ru.mephi.knowledgechecker.strategy.impl.test.create.manual.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CreationPhaseType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.create.manual.question.variable.VariableQuestionInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.ADD_VARIABLE_QUESTION;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddVariableQuestionStrategy extends AbstractCallbackQueryStrategy {

    public AddVariableQuestionStrategy(@Lazy VariableQuestionInfoReadingState variableQuestionInfoReadingState) {
        this.nextState = variableQuestionInfoReadingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(ADD_VARIABLE_QUESTION.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        data.setNextPhase(CreationPhaseType.TEXT);

        String boldMessage = "Введите содержание вопроса";
        String italicMessage =
                "\n\nПредпочтительно добавить в формулировку вопроса варианты ответа:\n" +
                "A: Ответ 1\n" +
                "B: Ответ 2\n" +
                "etc.";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), boldMessage + italicMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                null);
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

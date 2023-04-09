package ru.mephi.knowledgechecker.strategy.impl.test.create.manual.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.create.manual.question.variable.WrongVariableAnswerInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.ADD_WRONG_VARIABLE_ANSWER;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddWrongVariableAnswerStrategy extends AbstractCallbackQueryStrategy {

    public AddWrongVariableAnswerStrategy(@Lazy WrongVariableAnswerInfoReadingState wrongVariableAnswerInfoReadingState) {
        this.nextState = wrongVariableAnswerInfoReadingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(ADD_WRONG_VARIABLE_ANSWER.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String boldMessage = "Введите неправильный ответ (максимум 30 символов)";
        String italicMessage = "\n\nПредпочтительно вводить короткие варианты ответа: A, B, etc.";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), boldMessage + italicMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.UNDERLINE, 8, 12),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                null);
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

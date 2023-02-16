package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.state.impl.test.create.question.variable.WrongVariableAnswerInfoReadingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.ADD_WRONG_VARIABLE_ANSWER;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddWrongVariableAnswerStrategy extends AbstractCallbackQueryStrategy {
    public AddWrongVariableAnswerStrategy(@Lazy WrongVariableAnswerInfoReadingState wrongVariableAnswerInfoReadingState) {
        this.nextState = wrongVariableAnswerInfoReadingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(ADD_WRONG_VARIABLE_ANSWER);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        String boldMessage = "Введите неправильный ответ (максимум 30 символов)";
        String italicMessage = "\n\nПредпочтительно вводить короткие варианты ответа: A, B, etc.";
        MessageParams params =
                wrapMessageParams(update.getCallbackQuery().getFrom().getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                                new MessageEntity(TextType.UNDERLINE, 8, 12),
                                new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                        null);
        putStateToContext(update.getCallbackQuery().getFrom().getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.state.impl.test.create.question.variable.WrongVariableAnswerInfoReadingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

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
        MessageParams params =
                wrapMessageParams(update.getCallbackQuery().getFrom().getId(), "Введите правильный ответ\n\n" +
                        "(Предпочтительно вводить короткие варианты ответа: A, B, etc.)", null);
        putStateToContext(update.getCallbackQuery().getFrom().getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

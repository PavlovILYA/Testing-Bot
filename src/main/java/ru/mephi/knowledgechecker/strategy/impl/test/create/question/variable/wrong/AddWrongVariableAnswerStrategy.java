package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable.wrong;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.state.impl.menu.MainMenuState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.ADD_WRONG_VARIABLE_ANSWER;

@Component
public class AddWrongVariableAnswerStrategy extends AbstractCallbackQueryStrategy {
    public AddWrongVariableAnswerStrategy(@Lazy MainMenuState mainMenuState) {
        this.nextState = mainMenuState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(ADD_WRONG_VARIABLE_ANSWER);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {

    }
}

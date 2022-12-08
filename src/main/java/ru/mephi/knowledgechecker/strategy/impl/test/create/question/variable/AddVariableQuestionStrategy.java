package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.Map;

@Component
public class AddVariableQuestionStrategy extends AbstractCallbackQueryStrategy {
    @Override
    public boolean apply(Update update) {
        return super.apply(update);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {

    }
}

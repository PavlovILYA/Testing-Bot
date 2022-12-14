package ru.mephi.knowledgechecker.strategy.impl.test.solve.reply;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.SHOW_ANSWER;

@Component
public class StartSolvingReplyTestStrategy extends AbstractCallbackQueryStrategy {
    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(SHOW_ANSWER);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {

    }
}

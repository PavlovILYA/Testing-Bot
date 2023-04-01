package ru.mephi.knowledgechecker.strategy.impl.test.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.search.TestSearchAttemptState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.FIND_PUBLIC_TEST;
import static ru.mephi.knowledgechecker.common.CommonMessageParams.askSearchQueryParams;

@Slf4j
@Component
public class AskForTestSearchQueryStrategy extends AbstractCallbackQueryStrategy {
    public AskForTestSearchQueryStrategy(@Lazy TestSearchAttemptState testSearchAttemptState) {
        nextState = testSearchAttemptState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && (update.getCallbackQuery().getData().equals(FIND_PUBLIC_TEST.name()));
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        deleteMenu(data);
        data.setState(nextState);
        sendMessageAndSave(askSearchQueryParams(data.getUser().getId()), data);
    }
}

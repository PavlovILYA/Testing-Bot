package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.SOLVE_OWN_TEST;
import static ru.mephi.knowledgechecker.common.Constants.COLON;
import static ru.mephi.knowledgechecker.common.Constants.OWN_PRIVATE_TEST_PREFIX;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getTestManageKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_TEST;

@Component
public class ChooseSolvingTypeStrategy extends AbstractCallbackQueryStrategy {

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update) &&
                update.getCallbackQuery().getData().equals(SOLVE_OWN_TEST.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Test test = data.getTest();
        String message = MANAGE_TEST.getTitle() + test.getUniqueTitle();
        sendMenuAndSave(data, message, getTestManageKeyboardMarkup(false,
                OWN_PRIVATE_TEST_PREFIX + COLON + test.getUniqueTitle()));
    }
}

package ru.mephi.knowledgechecker.strategy.impl.test;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.solve.ChooseSolvingTypeState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.Constants.COLON;
import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_PREFIX;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getTestManageInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_TEST;

@Component
public class ManageTestStrategy extends AbstractCallbackQueryStrategy {
    private final TestService testService;

    public ManageTestStrategy(TestService testService,
                              @Lazy ChooseSolvingTypeState chooseSolvingTypeState) {
        this.testService = testService;
        this.nextState = chooseSolvingTypeState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update) ||
                !update.getCallbackQuery().getData().split(COLON)[0].equals(PUBLIC_TEST_PREFIX)) {
            return false;
        }

        String uniqueTitle = update.getCallbackQuery().getData().split(COLON)[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);
        return test != null
                && test.getTestType().equals(TestType.PUBLIC);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String uniqueTitle = update.getCallbackQuery().getData().split(COLON)[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);
        data.setTest(test);

        boolean own = test.getCreator().getId().equals(data.getUser().getId());
        String message = MANAGE_TEST.getTitle() + test.getUniqueTitle();
        data.setState(nextState);
        sendMenuAndSave(data, message, getTestManageInlineKeyboardMarkup(own));
    }
}

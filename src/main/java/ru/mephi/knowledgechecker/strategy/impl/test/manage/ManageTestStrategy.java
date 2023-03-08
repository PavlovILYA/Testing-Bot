package ru.mephi.knowledgechecker.strategy.impl.test.manage;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.manage.ManageTestState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getTestManageInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_TEST;

@Component
public class ManageTestStrategy extends AbstractCallbackQueryStrategy {
    private final TestService testService;

    public ManageTestStrategy(TestService testService,
                              @Lazy ManageTestState manageTestState) {
        this.testService = testService;
        this.nextState = manageTestState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }

        String testPrefix = update.getCallbackQuery().getData().split(COLON)[0];
        if (!testPrefix.equals(PUBLIC_TEST_PREFIX) &&
                !testPrefix.equals(PRIVATE_TEST_PREFIX)) {
            return false;
        }

        String uniqueTitle = update.getCallbackQuery().getData().split(COLON)[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);
        switch (testPrefix) {
            case PUBLIC_TEST_PREFIX:
                return test != null && test.getTestType().equals(TestType.PUBLIC);
            case PRIVATE_TEST_PREFIX:
                return test != null
                        && test.getTestType().equals(TestType.PRIVATE)
                        && test.getCreator().getId().equals(data.getUser().getId());
            default:
                return false;
        }
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String uniqueTitle = update.getCallbackQuery().getData().split(COLON)[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);
        data.setTest(test);

        boolean own = test.getCreator().getId().equals(data.getUser().getId());
        String message = MANAGE_TEST.getTitle() + test.getUniqueTitle();
        data.setState(nextState);
        String backCallbackData = data.getCourse() == null
                ? TO_PUBLIC_TEST_LIST.name()
                : OWN_COURSE_PREFIX + COLON + data.getCourse().getId();
        sendMenuAndSave(data, message, getTestManageInlineKeyboardMarkup(own, backCallbackData));
    }
}

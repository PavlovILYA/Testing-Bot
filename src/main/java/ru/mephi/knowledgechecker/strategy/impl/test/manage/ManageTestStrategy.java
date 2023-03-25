package ru.mephi.knowledgechecker.strategy.impl.test.manage;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.manage.ManageTestState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;
import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.*;
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
        if (!testPrefix.equals(PUBLIC_TEST_PREFIX)
                && !testPrefix.equals(OWN_PRIVATE_TEST_PREFIX)
                && !testPrefix.equals(ESTIMATED_PRIVATE_TEST_PREFIX)
                && !testPrefix.equals(TRAIN_PRIVATE_TEST_PREFIX)) {
            return false;
        }

        String uniqueTitle = update.getCallbackQuery().getData().split(COLON)[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);
        switch (testPrefix) {
            case PUBLIC_TEST_PREFIX:
                return test != null && test.getTestType().equals(TestType.PUBLIC);
            case OWN_PRIVATE_TEST_PREFIX:
                return test != null
                        && test.getTestType().equals(TestType.PRIVATE)
                        && test.getCreator().getId().equals(data.getUser().getId());
            case ESTIMATED_PRIVATE_TEST_PREFIX:
                return test != null
                        && test.getTestType().equals(TestType.PRIVATE)
                        && test.getVisibility().equals(VisibilityType.ESTIMATED);
            case TRAIN_PRIVATE_TEST_PREFIX:
                return test != null
                        && test.getTestType().equals(TestType.PRIVATE)
                        && test.getVisibility().equals(VisibilityType.TRAIN);
            default:
                return false;
        }
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String testPrefix = update.getCallbackQuery().getData().split(COLON)[0];
        String uniqueTitle = update.getCallbackQuery().getData().split(COLON)[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);
        data.setTest(test);

        String message = MANAGE_TEST.getTitle() + test.getUniqueTitle();
        data.setState(nextState);
        KeyboardMarkup markup;
        boolean own;
        String backCallbackData;

        switch (testPrefix) {
            case PUBLIC_TEST_PREFIX:
                own = test.getCreator().getId().equals(data.getUser().getId());
                backCallbackData = TO_PUBLIC_TEST_LIST.name();
                markup = getTestManageKeyboardMarkup(own, backCallbackData);
                break;
            case OWN_PRIVATE_TEST_PREFIX:
                own = test.getCreator().getId().equals(data.getUser().getId());
                backCallbackData = TO_PRIVATE_TEST_LIST.name();
                markup = getAdminCourseTestManageKeyboardMarkup(own, backCallbackData);
                break;
            case ESTIMATED_PRIVATE_TEST_PREFIX:
                backCallbackData = TO_ESTIMATED_TESTS.name();
                markup = getCourseTestManageKeyboardMarkup(backCallbackData);
                break;
            case TRAIN_PRIVATE_TEST_PREFIX:
                backCallbackData = TO_TRAIN_TESTS.name();
                markup = getCourseTestManageKeyboardMarkup(backCallbackData);
                break;
            default:
                return;
        }
        sendMenuAndSave(data, message, markup);
    }
}

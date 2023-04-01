package ru.mephi.knowledgechecker.strategy.impl.test.check;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.check.WorksForCheckListState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.SHOW_WORKS_FOR_CHECK;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getStudentsForCheckKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_TEST;

@Component
public class ShowWorksForCheckStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;

    public ShowWorksForCheckStrategy(UserService userService,
                                     @Lazy WorksForCheckListState worksForCheckListState) {
        this.userService = userService;
        this.nextState = worksForCheckListState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(SHOW_WORKS_FOR_CHECK.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Page<User> usersForCheckPage = userService.getStudentsForCheck(data.getTest().getId());
        data.setState(nextState);
        sendMenuAndSave(data, MANAGE_TEST.getTitle() + " - работы для проверки",
                getStudentsForCheckKeyboardMarkup(usersForCheckPage, data.getTest().getUniqueTitle()));
    }
}

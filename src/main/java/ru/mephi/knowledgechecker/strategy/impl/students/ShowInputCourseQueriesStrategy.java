package ru.mephi.knowledgechecker.strategy.impl.students;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.students.InputCourseQueriesState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_INPUT_COURSE_QUERIES;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPotentialStudentsInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.OUTPUT_COURSE_QUERIES;

@Component
public class ShowInputCourseQueriesStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;

    public ShowInputCourseQueriesStrategy(@Lazy InputCourseQueriesState inputCourseQueriesState,
                                          UserService userService) {
        this.nextState = inputCourseQueriesState;
        this.userService = userService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(TO_INPUT_COURSE_QUERIES.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        data.setStudent(null);
        Page<User> queriedPeoplePage = userService.getParticipantsByCourseId(data.getCourse().getId(), false);
        data.setState(nextState);
        sendMenuAndSave(data, OUTPUT_COURSE_QUERIES.getTitle(),
                getPotentialStudentsInlineKeyboardMarkup(queriedPeoplePage, data.getCourse().getId()));
    }
}

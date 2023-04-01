package ru.mephi.knowledgechecker.strategy.impl.students;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.students.StudentsListState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_STUDENTS;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getStudentsListKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.STUDENTS;

@Component
public class ShowStudentsListStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;

    public ShowStudentsListStrategy(@Lazy StudentsListState studentsListState,
                                    UserService userService) {
        this.nextState = studentsListState;
        this.userService = userService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(TO_STUDENTS.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        data.setStudent(null);
        Page<User> studentsPage = userService.getParticipantsByCourseId(data.getCourse().getId(), true);
        data.setState(nextState);
        sendMenuAndSave(data, STUDENTS.getTitle(),
                getStudentsListKeyboardMarkup(studentsPage, data.getCourse().getId()));
    }
}

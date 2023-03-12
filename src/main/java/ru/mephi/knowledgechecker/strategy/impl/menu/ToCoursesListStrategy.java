package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseService;
import ru.mephi.knowledgechecker.state.impl.menu.CoursesListState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_COURSES_LIST;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getStudiedCoursesInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.COURSES_LIST;

@Slf4j
@Component
public class ToCoursesListStrategy extends AbstractCallbackQueryStrategy {
    private final CourseService courseService;

    public ToCoursesListStrategy(@Lazy CoursesListState nextState,
                                 CourseService courseService) {
        this.nextState = nextState;
        this.courseService = courseService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(TO_COURSES_LIST.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        if (data.getCourse() != null) {
            data.setCourse(null);
        }
        Page<Course> studiedCourses = courseService.getCoursesByParticipantId(data.getUser().getId(), true);
        data.setState(nextState);
        sendMenuAndSave(data, COURSES_LIST.getTitle(), getStudiedCoursesInlineKeyboardMarkup(studiedCourses));
    }
}

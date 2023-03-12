package ru.mephi.knowledgechecker.strategy.impl.course.participate;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseService;
import ru.mephi.knowledgechecker.state.impl.course.participate.OutputCourseQueriesState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_OUTPUT_COURSE_QUERIES;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getOutputCourseQueriesInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.OUTPUT_COURSE_QUERIES;

@Component
public class ShowOutputCourseQueriesStrategy extends AbstractCallbackQueryStrategy {
    private final CourseService courseService;

    public ShowOutputCourseQueriesStrategy(@Lazy OutputCourseQueriesState outputCourseQueriesState,
                                           CourseService courseService) {
        this.nextState = outputCourseQueriesState;
        this.courseService = courseService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(TO_OUTPUT_COURSE_QUERIES.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        if (data.getCourse() != null) {
            data.setCourse(null);
        }
        Page<Course> coursesPage = courseService.getCoursesByParticipantId(data.getUser().getId(), false);
        data.setState(nextState);
        sendMenuAndSave(data, OUTPUT_COURSE_QUERIES.getTitle(),
                getOutputCourseQueriesInlineKeyboardMarkup(coursesPage));
    }
}

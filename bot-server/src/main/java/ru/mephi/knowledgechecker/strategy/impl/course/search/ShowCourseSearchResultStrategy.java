package ru.mephi.knowledgechecker.strategy.impl.course.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseService;
import ru.mephi.knowledgechecker.state.impl.course.search.CourseSearchResultState;
import ru.mephi.knowledgechecker.state.impl.menu.CoursesListState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.nothingIsFoundParams;
import static ru.mephi.knowledgechecker.common.Constants.SEMICOLON;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.*;
import static ru.mephi.knowledgechecker.common.MenuTitleType.*;

@Slf4j
@Component
public class ShowCourseSearchResultStrategy extends AbstractMessageStrategy {
    private final CourseService courseService;
    private final CoursesListState coursesListState;

    public ShowCourseSearchResultStrategy(CourseService courseService,
                                          @Lazy CourseSearchResultState courseSearchResultState,
                                          @Lazy CoursesListState coursesListState) {
        this.courseService = courseService;
        this.nextState = courseSearchResultState;
        this.coursesListState = coursesListState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String keyWords = update.getMessage().getText().replaceAll(SEMICOLON, "|");
        Page<Course> coursesPage = courseService.findCourses(keyWords, data.getUser().getId());
        if (coursesPage.getTotalElements() != 0) {
            data.setSearchKeyWords(keyWords);
            sendResults(data, coursesPage);
        } else {
            sendNotFound(data);
        }
    }

    private void sendNotFound(CurrentData data) {
        telegramApiClient.sendMessage(nothingIsFoundParams(data.getUser().getId()));

        data.setState(coursesListState);
        Page<Course> studiedCourses = courseService.getCoursesByParticipantId(data.getUser().getId(), true);
        sendMenuAndSave(data, COURSES_LIST.getTitle(), getStudiedCoursesKeyboardMarkup(studiedCourses));
    }

    private void sendResults(CurrentData data, Page<Course> coursesPage) {
        data.setState(nextState);
        sendMenuAndSave(data, COURSE_SEARCH_RESULT.getTitle(), getCourseSearchResultsKeyboardMarkup(coursesPage));
    }
}

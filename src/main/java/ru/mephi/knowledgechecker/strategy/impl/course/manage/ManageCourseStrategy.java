package ru.mephi.knowledgechecker.strategy.impl.course.manage;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseService;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.course.manage.ManageCourseState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;
import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getManageStudiedCourseMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_COURSE;
import static ru.mephi.knowledgechecker.common.MenuTitleType.QUERY_TO_COURSE;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;

@Component
public class ManageCourseStrategy extends AbstractCallbackQueryStrategy {
    private final CourseService courseService;
    private final TestService testService;

    public ManageCourseStrategy(@Lazy ManageCourseState manageCourseState,
                                CourseService courseService,
                                TestService testService) {
        this.nextState = manageCourseState;
        this.courseService = courseService;
        this.testService = testService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }

        String coursePrefix = update.getCallbackQuery().getData().split(COLON)[0];
        return coursePrefix.equals(OWN_COURSE_PREFIX)
                || coursePrefix.equals(SEARCH_COURSE_PREFIX)
                || coursePrefix.equals(OUTPUT_QUERIES_PREFIX)
                || coursePrefix.equals(STUDIED_COURSE_PREFIX);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        // test to null?
        String coursePrefix = update.getCallbackQuery().getData().split(COLON)[0];
        Long courseId = Long.parseLong(update.getCallbackQuery().getData().split(COLON)[1]);
        Course course = courseService.getById(courseId);
        data.setCourse(course);
        String message;
        KeyboardMarkup markup;

        switch (coursePrefix) {
            case OWN_COURSE_PREFIX:
                message = MANAGE_COURSE.getTitle() + course.getTitle();
                markup = getManageOwnCourseMarkup();
                break;
            case SEARCH_COURSE_PREFIX:
                message = MANAGE_COURSE.getTitle() + course.getTitle();
                markup = getManageSearchCourseMarkup();
                break;
            case OUTPUT_QUERIES_PREFIX:
                message = QUERY_TO_COURSE.getTitle() + course.getTitle();
                markup = getManageOutputQueryMarkup();
                break;
            case STUDIED_COURSE_PREFIX:
                message = MANAGE_COURSE.getTitle() + course.getTitle();
                Page<String> testsOfCoursePage = testService.getTestsByCourse(course);
                markup = getManageStudiedCourseMarkup(testsOfCoursePage);
                break;
            default:
                return;
        }

        data.setState(nextState);
        sendMenuAndSave(data, message, markup);
    }

    private KeyboardMarkup getManageOwnCourseMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_ADMIN_MENU.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_STUDENTS.getDescription())
                .callbackData(TO_STUDENTS.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_PRIVATE_TEST_LIST.getDescription())
                .callbackData(TO_PRIVATE_TEST_LIST.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(ACADEMIC_PERFORMANCE.getDescription())
                .callbackData(ACADEMIC_PERFORMANCE.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_INPUT_COURSE_QUERIES.getDescription())
                .callbackData(TO_INPUT_COURSE_QUERIES.name())
                .build()));
        return wrapInlineKeyboardMarkup(markup);
    }

    private KeyboardMarkup getManageSearchCourseMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(
                InlineKeyboardButton.builder()
                        .text("⬅️")
                        .callbackData(TO_COURSES_LIST.name())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(PARTICIPATE_IN_COURSE.getDescription())
                        .callbackData(PARTICIPATE_IN_COURSE.name())
                        .build()));
        return wrapInlineKeyboardMarkup(markup);
    }

    private KeyboardMarkup getManageOutputQueryMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(
                InlineKeyboardButton.builder()
                        .text("⬅️")
                        .callbackData(TO_OUTPUT_COURSE_QUERIES.name())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(CANCEL_OUTPUT_QUERY.getDescription())
                        .callbackData(CANCEL_OUTPUT_QUERY.name())
                        .build()));
        return wrapInlineKeyboardMarkup(markup);
    }
}

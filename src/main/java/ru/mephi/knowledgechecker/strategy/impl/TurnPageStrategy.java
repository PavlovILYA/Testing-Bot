package ru.mephi.knowledgechecker.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.CourseService;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;

import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.*;
import static ru.mephi.knowledgechecker.common.MenuTitleType.*;

@Slf4j
@Component
public class TurnPageStrategy extends AbstractCallbackQueryStrategy {
    private final TestService testService;
    private final CourseService courseService;
    private final UserService userService;

    public TurnPageStrategy(TestService testService,
                            CourseService courseService,
                            UserService userService) {
        this.testService = testService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }

        String prefix = update.getCallbackQuery().getData().split(COLON)[0];
        return prefix.equals(CREATED_TESTS_PAGE_PREFIX)
                || prefix.equals(SEARCH_TESTS_PAGE_PREFIX)
                || prefix.equals(OWN_COURSE_PAGE_PREFIX)
                || prefix.equals(OWN_PRIVATE_TESTS_PAGE_PREFIX)
                || prefix.equals(SEARCH_COURSES_PAGE_PREFIX)
                || prefix.equals(OUTPUT_QUERIES_PAGE_PREFIX)
                || prefix.equals(INPUT_QUERIES_PAGE_PREFIX)
                || prefix.equals(STUDENT_PAGE_PREFIX)
                || prefix.equals(STUDIED_COURSE_PAGE_PREFIX)
                || prefix.equals(ESTIMATED_PRIVATE_TESTS_PAGE_PREFIX)
                || prefix.equals(TRAIN_PRIVATE_TESTS_PAGE_PREFIX);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String prefix = update.getCallbackQuery().getData().split(COLON)[0];
        int pageNumber = Integer.parseInt(update.getCallbackQuery().getData().split(COLON)[1]);
        KeyboardMarkup markup;
        String message;

        switch (prefix) {
            case CREATED_TESTS_PAGE_PREFIX:
                message = PUBLIC_TEST_LIST.getTitle();
                Page<String> publicTests = testService.getCreatedTests(data.getUser().getId(), pageNumber);
                markup = getPublicTestMenuKeyboardMarkup(publicTests);
                break;
            case SEARCH_TESTS_PAGE_PREFIX:
                message = TEST_SEARCH_RESULT.getTitle();
                Page<String> testTitlesPage = testService.findTests(data.getSearchKeyWords(),
                        data.getUser().getId(), pageNumber);
                markup = getTestSearchResultsKeyboardMarkup(testTitlesPage);
                break;
            case OWN_PRIVATE_TESTS_PAGE_PREFIX:
                message = MANAGE_COURSE.getTitle() + data.getCourse().getTitle();
                Page<String> privateTestsPage = testService.getTestsByCourse(data.getCourse(), pageNumber);
                markup = getOwnPrivateTestListKeyboardMarkup(privateTestsPage, data.getCourse().getId());
                break;
            case ESTIMATED_PRIVATE_TESTS_PAGE_PREFIX:
                message = MANAGE_COURSE.getTitle() + data.getCourse().getTitle() + " – ТЕСТЫ НА ОЦЕНКУ";
                Page<String> estimatedTestsPage =
                        testService.getTestsByCourseAndVisibility(
                                data.getCourse(), VisibilityType.ESTIMATED, pageNumber);
                markup = getStudiedPrivateTestListKeyboardMarkup(
                        estimatedTestsPage, data.getCourse().getId(), VisibilityType.ESTIMATED);
                break;
            case TRAIN_PRIVATE_TESTS_PAGE_PREFIX:
                message = MANAGE_COURSE.getTitle() + data.getCourse().getTitle() + " – ТРЕНИРОВОЧНЫЕ ТЕСТЫ";
                Page<String> trainTestsPage = testService.getTestsByCourseAndVisibility(
                        data.getCourse(), VisibilityType.TRAIN, pageNumber);
                markup = getStudiedPrivateTestListKeyboardMarkup(
                        trainTestsPage, data.getCourse().getId(), VisibilityType.TRAIN);
                break;
            case OWN_COURSE_PAGE_PREFIX:
                message = ADMIN_MENU.getTitle();
                Page<Course> ownCoursesPage = courseService.getCoursesByCreatorId(data.getUser().getId(), pageNumber);
                markup = getOwnCoursesKeyboardMarkup(ownCoursesPage);
                break;
            case SEARCH_COURSES_PAGE_PREFIX:
                message = COURSE_SEARCH_RESULT.getTitle();
                Page<Course> coursesPage = courseService.findCourses(data.getSearchKeyWords(),
                        data.getUser().getId(), pageNumber);
                markup = getCourseSearchResultsKeyboardMarkup(coursesPage);
                break;
            case STUDIED_COURSE_PAGE_PREFIX:
                message = COURSES_LIST.getTitle();
                Page<Course> studiedCoursesPage = courseService.getCoursesByParticipantId(data.getUser().getId(),
                        true, pageNumber);
                markup = getStudiedCoursesKeyboardMarkup(studiedCoursesPage);
                break;
            case OUTPUT_QUERIES_PAGE_PREFIX:
                message = OUTPUT_COURSE_QUERIES.getTitle();
                Page<Course> outputQueriesPage = courseService.getCoursesByParticipantId(data.getUser().getId(),
                        false, pageNumber);
                markup = getOutputCourseQueriesKeyboardMarkup(outputQueriesPage);
                break;
            case INPUT_QUERIES_PAGE_PREFIX:
                message = OUTPUT_COURSE_QUERIES.getTitle();
                Page<User> queriedPeoplePage = userService.getParticipantsByCourseId(
                        data.getCourse().getId(), false, pageNumber);
                markup = getStudentsListKeyboardMarkup(queriedPeoplePage, data.getCourse().getId());
                break;
            case STUDENT_PAGE_PREFIX:
                message = STUDENTS.getTitle();
                Page<User> studentsPage = userService.getParticipantsByCourseId(
                        data.getCourse().getId(), true, pageNumber);
                markup = getPotentialStudentsKeyboardMarkup(studentsPage, data.getCourse().getId());
                break;
            default:
                return;
        }

        sendMenuAndSave(data, message, markup);
    }
}

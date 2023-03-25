package ru.mephi.knowledgechecker.common;

import org.springframework.data.domain.Page;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.KeyboardButton;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.solving.SolvingType;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;
import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapReplyKeyboardMarkup;

public class KeyboardMarkups {
    public static KeyboardMarkup getStartKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_PUBLIC_TEST_LIST.getDescription())
                .callbackData(TO_PUBLIC_TEST_LIST.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_COURSES_LIST.getDescription())
                .callbackData(TO_COURSES_LIST.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_ADMIN_MENU.getDescription())
                .callbackData(TO_ADMIN_MENU.name())
                .build()));
        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getAddQuestionKeyboardMarkup(String doneCallbackData) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text(ADD_VARIABLE_QUESTION.getDescription())
                .callbackData(ADD_VARIABLE_QUESTION.name())
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text(ADD_OPEN_QUESTION.getDescription())
                .callbackData(ADD_OPEN_QUESTION.name())
                .build());
        markup.add(menu);
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("✅️")
                .callbackData(doneCallbackData)
                .build()));
        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getAddWrongVariableAnswerKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text(ADD_WRONG_VARIABLE_ANSWER.getDescription())
                .callbackData(ADD_WRONG_VARIABLE_ANSWER.name())
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("✅️")
                .callbackData(TO_QUESTION_ADDING.name())
                .build());
        markup.add(menu);
        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getAdminCourseTestManageKeyboardMarkup(boolean own, String backCallbackData) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(backCallbackData)
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(MANAGE_VISIBILITY.getDescription())
                .callbackData(MANAGE_VISIBILITY.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(SOLVE_OWN_TEST.getDescription())
                .callbackData(SOLVE_OWN_TEST.name())
                .build()));
        if (own) {
            markup.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(EDIT_TEST.getDescription())
                            .callbackData(EDIT_TEST.name())
                            .build(),
                    InlineKeyboardButton.builder()
                            .text(DELETE_TEST.getDescription())
                            .callbackData(DELETE_TEST.name())
                            .build()
            ));
        }
        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getTestManageKeyboardMarkup(boolean own, String backCallbackData) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(backCallbackData)
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(SolvingType.INSTANT_DEMONSTRATION_ANSWER.getDescription())
                .callbackData(SolvingType.INSTANT_DEMONSTRATION_ANSWER.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(SolvingType.REPORT_GENERATING_AT_THE_END.getDescription())
                .callbackData(SolvingType.REPORT_GENERATING_AT_THE_END.name())
                .build()));
        if (own) {
            markup.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(EDIT_TEST.getDescription())
                            .callbackData(EDIT_TEST.name())
                            .build(),
                    InlineKeyboardButton.builder()
                            .text(DELETE_TEST.getDescription())
                            .callbackData(DELETE_TEST.name())
                            .build()
            ));
        }
        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getCourseTestManageKeyboardMarkup(String backCallback) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(backCallback)
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("Пройти")
                .callbackData(SolvingType.REPORT_GENERATING_AT_THE_END.name())
                .build()));
//        markup.add(List.of(InlineKeyboardButton.builder() // todo
//                .text("Результаты")
//                .callbackData()
//                .build()));
        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getVariableAnswerKeyboardMarkup(VariableQuestion question) {
        List<VariableAnswer> answers = question.getWrongAnswers();
        Collections.shuffle(answers);
        answers = answers.stream()
                .limit(question.getMaxAnswerNumber() - 1)
                .collect(Collectors.toList());
        answers.add(question.getCorrectAnswer());
        Collections.shuffle(answers);

        boolean odd = answers.size() % 2 != 0;

        VariableAnswer oddAnswer = null;
        if (odd) {
            oddAnswer = answers.get(answers.size() - 1);
            answers.remove(answers.size() - 1);
        }

        List<List<KeyboardButton>> markup = new ArrayList<>();
        for (int i = 0; i < answers.size(); i += 2) {
            markup.add(List.of(
                    KeyboardButton.builder()
                            .text(answers.get(i).getText())
                            .build(),
                    KeyboardButton.builder()
                            .text(answers.get(i + 1).getText())
                            .build()));
        }

        if (odd) {
            markup.add(List.of(KeyboardButton.builder()
                    .text(oddAnswer.getText())
                    .build()));
        }

        return wrapReplyKeyboardMarkup(markup, "Выберите правильный ответ");
    }

    public static KeyboardMarkup getPublicTestMenuKeyboardMarkup(Page<String> publicTestsPage) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_MAIN_MENU.getDescription())
                .callbackData(TO_MAIN_MENU.name())
                .build()));
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text(CREATE_TEST.getDescription())
                .callbackData(CREATE_TEST.name())
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text(FIND_PUBLIC_TEST.getDescription())
                .callbackData(FIND_PUBLIC_TEST.name())
                .build());
        markup.add(menu);

        return getTestListKeyboardMarkup(markup, publicTestsPage,
                PUBLIC_TEST_PREFIX, CREATED_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getTestSearchResultsKeyboardMarkup(Page<String> publicTestsPage) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> back = List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_PUBLIC_TEST_LIST.name())
                .build());
        markup.add(back);

        return getTestListKeyboardMarkup(markup, publicTestsPage,
                PUBLIC_TEST_PREFIX, SEARCH_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getManageStudiedCourseMarkup(long trainCount, long estimatedCount) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(
                InlineKeyboardButton.builder()
                        .text("⬅️")
                        .callbackData(TO_COURSES_LIST.name())
                        .build()));
        markup.add(List.of(
                InlineKeyboardButton.builder()
                        .text(TO_TRAIN_TESTS.getDescription() + " (" + trainCount + ")")
                        .callbackData(TO_TRAIN_TESTS.name())
                        .build()));
        markup.add(List.of(
                InlineKeyboardButton.builder()
                        .text(TO_ESTIMATED_TESTS.getDescription() + " (" + estimatedCount + ")")
                        .callbackData(TO_ESTIMATED_TESTS.name())
                        .build()));

        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getStudiedPrivateTestListKeyboardMarkup(Page<String> testsOfCoursePage, Long courseId,
                                                                         VisibilityType visibilityType) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(STUDIED_COURSE_PREFIX + COLON + courseId)
                .build());
        markup.add(menu);

        if (visibilityType.equals(VisibilityType.ESTIMATED)) {
            return getTestListKeyboardMarkup(markup, testsOfCoursePage,
                    ESTIMATED_PRIVATE_TEST_PREFIX, ESTIMATED_PRIVATE_TESTS_PAGE_PREFIX);
        } else {
            return getTestListKeyboardMarkup(markup, testsOfCoursePage,
                    TRAIN_PRIVATE_TEST_PREFIX, TRAIN_PRIVATE_TESTS_PAGE_PREFIX);
        }
    }

    public static KeyboardMarkup getOwnPrivateTestListKeyboardMarkup(Page<String> privateTestsPage, Long courseId) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(OWN_COURSE_PREFIX + COLON + courseId)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text(CREATE_TEST.getDescription())
                .callbackData(CREATE_TEST.name())
                .build());
        markup.add(menu);

        return getTestListKeyboardMarkup(markup, privateTestsPage,
                OWN_PRIVATE_TEST_PREFIX, OWN_PRIVATE_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getTestListKeyboardMarkup(List<List<InlineKeyboardButton>> markup,
                                                           Page<String> testsPage,
                                                           String testPublicityPrefix,
                                                           String pagePrefix) {
        for (String test: testsPage.getContent()) {
            List<InlineKeyboardButton> testList = new ArrayList<>();
            testList.add(InlineKeyboardButton.builder()
                    .text("📌 " + test)
                    .callbackData(testPublicityPrefix + COLON + test)
                    .build());
            markup.add(testList);
        }

        if (testsPage.getTotalElements() > PAGE_SIZE) {
            markup.add(getNavigationButtons(testsPage, pagePrefix));
        }

        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getOwnCoursesKeyboardMarkup(Page<Course> coursesPage) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text(TO_MAIN_MENU.getDescription())
                .callbackData(TO_MAIN_MENU.name())
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text(CREATE_COURSE.getDescription())
                .callbackData(CREATE_COURSE.name())
                .build());
        markup.add(menu);

        return getCourseListKeyboardMarkup(markup, coursesPage,
                OWN_COURSE_PREFIX, OWN_COURSE_PAGE_PREFIX);
    }

    public static KeyboardMarkup getCourseSearchResultsKeyboardMarkup(Page<Course> courseTitlesPage) {
        return getForeignCoursesKeyboardMarkup(courseTitlesPage,
                SEARCH_COURSE_PREFIX, SEARCH_COURSES_PAGE_PREFIX);
    }

    public static KeyboardMarkup getOutputCourseQueriesKeyboardMarkup(Page<Course> courseTitlesPage) {
        return getForeignCoursesKeyboardMarkup(courseTitlesPage,
                OUTPUT_QUERIES_PREFIX, OUTPUT_QUERIES_PAGE_PREFIX);
    }

    public static KeyboardMarkup getForeignCoursesKeyboardMarkup(Page<Course> courseTitlesPage,
                                                                 String coursePrefix, String pagePrefix) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> back = List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_COURSES_LIST.name())
                .build());
        markup.add(back);

        return getCourseListKeyboardMarkup(markup, courseTitlesPage,
                coursePrefix, pagePrefix);
    }

    public static KeyboardMarkup getStudiedCoursesKeyboardMarkup(Page<Course> studiedCoursesPage) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_MAIN_MENU.getDescription())
                .callbackData(TO_MAIN_MENU.name())
                .build()));
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text(FIND_COURSE.getDescription())
                .callbackData(FIND_COURSE.name())
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text(TO_OUTPUT_COURSE_QUERIES.getDescription())
                .callbackData(TO_OUTPUT_COURSE_QUERIES.name())
                .build());
        markup.add(menu);

        return getCourseListKeyboardMarkup(markup, studiedCoursesPage,
                STUDIED_COURSE_PREFIX, STUDIED_COURSE_PAGE_PREFIX);
    }

    private static KeyboardMarkup getCourseListKeyboardMarkup(List<List<InlineKeyboardButton>> markup,
                                                              Page<Course> coursesPage,
                                                              String coursePrefix,
                                                              String pagePrefix) {
        for (Course course: coursesPage.getContent()) {
            List<InlineKeyboardButton> courseList = new ArrayList<>();
            courseList.add(InlineKeyboardButton.builder()
                    .text("📌 " + course.getTitle())
                    .callbackData(coursePrefix + COLON + course.getId())
                    .build());
            markup.add(courseList);
        }

        if (coursesPage.getTotalElements() > PAGE_SIZE) {
            markup.add(getNavigationButtons(coursesPage, pagePrefix));
        }

        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getStudentsListKeyboardMarkup(Page<User> studentsPage, Long courseId) {
        return getUsersKeyboardMarkup(studentsPage, courseId,
                STUDENT_PREFIX, STUDENT_PAGE_PREFIX);
    }

    public static KeyboardMarkup getPotentialStudentsKeyboardMarkup(Page<User> queriedPeoplePage, Long courseId) {
        return getUsersKeyboardMarkup(queriedPeoplePage, courseId,
                INPUT_QUERIES_PREFIX, INPUT_QUERIES_PAGE_PREFIX);
    }

    public static KeyboardMarkup getUsersKeyboardMarkup(Page<User> usersPage, Long courseId,
                                                        String userPrefix, String pagePrefix) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(OWN_COURSE_PREFIX + COLON + courseId)
                .build()));

        return getUserListKeyboardMarkup(markup, usersPage,
                userPrefix, pagePrefix);
    }

    public static KeyboardMarkup getUserListKeyboardMarkup(List<List<InlineKeyboardButton>> markup,
                                                           Page<User> queriedPeople,
                                                           String userPrefix,
                                                           String pagePrefix) {
        for (User user : queriedPeople.getContent()) {
            List<InlineKeyboardButton> testList = new ArrayList<>();
            testList.add(InlineKeyboardButton.builder()
                    .text("👤 " + user.getUsername())
                    .callbackData(userPrefix + COLON + user.getId())
                    .build());
            markup.add(testList);
        }

        if (queriedPeople.getTotalElements() > PAGE_SIZE) {
            markup.add(getNavigationButtons(queriedPeople, pagePrefix));
        }

        return wrapInlineKeyboardMarkup(markup);
    }

    public static List<InlineKeyboardButton> getNavigationButtons(Page<?> currentPage, String callbackPrefix) {
        int pageNumber = currentPage.getNumber();
        List<InlineKeyboardButton> navigationList = new ArrayList<>();
        if (pageNumber != 0) {
            navigationList.add(getPreviousPageButton(pageNumber, callbackPrefix));
        }
        if (pageNumber != currentPage.getTotalPages() - 1) {
            navigationList.add(getNextPageButton(pageNumber, callbackPrefix));
        }
        return navigationList;
    }

    private static InlineKeyboardButton getNextPageButton(int currentPage, String callbackPrefix) {
        return InlineKeyboardButton.builder()
                .text(NEXT.getDescription())
                .callbackData(callbackPrefix + COLON + (currentPage + 1))
                .build();
    }

    private static InlineKeyboardButton getPreviousPageButton(int currentPage, String callbackPrefix) {
        return InlineKeyboardButton.builder()
                .text(PREVIOUS.getDescription())
                .callbackData(callbackPrefix + COLON + (currentPage - 1))
                .build();
    }
}

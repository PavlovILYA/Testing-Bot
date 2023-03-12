package ru.mephi.knowledgechecker.common;

import org.springframework.data.domain.Page;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.KeyboardButton;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.solving.SolvingType;
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

    public static KeyboardMarkup getAddQuestionInlineKeyboardMarkup(String doneCallbackData) {
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

    public static KeyboardMarkup getAddWrongVariableAnswerInlineKeyboardMarkup() {
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

    public static KeyboardMarkup getTestManageInlineKeyboardMarkup(boolean own, String backCallbackData) {
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

    public static KeyboardMarkup getCourseTestManageInlineKeyboardMarkup(Course course) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(STUDIED_COURSE_PREFIX + COLON + course.getId())
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

    public static KeyboardMarkup getPublicTestMenuInlineKeyboardMarkup(Page<String> publicTestsPage) {
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

        return getTestListInlineKeyboardMarkup(markup, publicTestsPage,
                PUBLIC_TEST_PREFIX, CREATED_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getTestSearchResultsInlineKeyboardMarkup(Page<String> publicTestsPage) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> back = List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_PUBLIC_TEST_LIST.name())
                .build());
        markup.add(back);

        return getTestListInlineKeyboardMarkup(markup, publicTestsPage,
                PUBLIC_TEST_PREFIX, SEARCH_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getManageStudiedCourseMarkup(Page<String> testsOfCoursePage) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(
                InlineKeyboardButton.builder()
                        .text("⬅️")
                        .callbackData(TO_COURSES_LIST.name())
                        .build()));
        return getTestListInlineKeyboardMarkup(markup, testsOfCoursePage,
                COURSE_PRIVATE_TEST_PREFIX, COURSE_PRIVATE_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getPrivateTestListInlineKeyboardMarkup(Page<String> privateTestsPage, Long courseId) {
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

        return getTestListInlineKeyboardMarkup(markup, privateTestsPage,
                OWN_PRIVATE_TEST_PREFIX, OWN_PRIVATE_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getTestListInlineKeyboardMarkup(List<List<InlineKeyboardButton>> markup,
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

    public static KeyboardMarkup getOwnCoursesInlineKeyboardMarkup(Page<Course> coursesPage) {
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

        return getCourseListInlineKeyboardMarkup(markup, coursesPage,
                OWN_COURSE_PREFIX, OWN_COURSE_PAGE_PREFIX);
    }

    public static KeyboardMarkup getCourseSearchResultsInlineKeyboardMarkup(Page<Course> courseTitlesPage) {
        return getForeignCoursesInlineKeyboardMarkup(courseTitlesPage,
                SEARCH_COURSE_PREFIX, SEARCH_COURSES_PAGE_PREFIX);
    }

    public static KeyboardMarkup getOutputCourseQueriesInlineKeyboardMarkup(Page<Course> courseTitlesPage) {
        return getForeignCoursesInlineKeyboardMarkup(courseTitlesPage,
                OUTPUT_QUERIES_PREFIX, OUTPUT_QUERIES_PAGE_PREFIX);
    }

    public static KeyboardMarkup getForeignCoursesInlineKeyboardMarkup(Page<Course> courseTitlesPage,
                                                                       String coursePrefix, String pagePrefix) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> back = List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_COURSES_LIST.name())
                .build());
        markup.add(back);

        return getCourseListInlineKeyboardMarkup(markup, courseTitlesPage,
                coursePrefix, pagePrefix);
    }

    public static KeyboardMarkup getStudiedCoursesInlineKeyboardMarkup(Page<Course> studiedCoursesPage) {
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

        return getCourseListInlineKeyboardMarkup(markup, studiedCoursesPage,
                STUDIED_COURSE_PREFIX, STUDIED_COURSE_PAGE_PREFIX);
    }

    private static KeyboardMarkup getCourseListInlineKeyboardMarkup(List<List<InlineKeyboardButton>> markup,
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

    public static KeyboardMarkup getStudentsListInlineKeyboardMarkup(Page<User> studentsPage, Long courseId) {
        return getUsersInlineKeyboardMarkup(studentsPage, courseId,
                STUDENT_PREFIX, STUDENT_PAGE_PREFIX);
    }

    public static KeyboardMarkup getPotentialStudentsInlineKeyboardMarkup(Page<User> queriedPeoplePage, Long courseId) {
        return getUsersInlineKeyboardMarkup(queriedPeoplePage, courseId,
                INPUT_QUERIES_PREFIX, INPUT_QUERIES_PAGE_PREFIX);
    }

    public static KeyboardMarkup getUsersInlineKeyboardMarkup(Page<User> usersPage, Long courseId,
                                                              String userPrefix, String pagePrefix) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(OWN_COURSE_PREFIX + COLON + courseId)
                .build()));

        return getUserListInlineKeyboardMarkup(markup, usersPage,
                userPrefix, pagePrefix);
    }

    public static KeyboardMarkup getUserListInlineKeyboardMarkup(List<List<InlineKeyboardButton>> markup,
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

package ru.mephi.knowledgechecker.common;

import org.springframework.data.domain.Page;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.KeyboardButton;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.solving.SolvingType;

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

    public static KeyboardMarkup getAddQuestionInlineKeyboardMarkup() {
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
                .callbackData(TO_PUBLIC_TEST_LIST.name())
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

    public static KeyboardMarkup getTestManageInlineKeyboardMarkup(boolean own) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_PUBLIC_TEST_LIST.name())
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
                .text(CREATE_PUBLIC_TEST.getDescription())
                .callbackData(CREATE_PUBLIC_TEST.name())
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text(FIND_PUBLIC_TEST.getDescription())
                .callbackData(FIND_PUBLIC_TEST.name())
                .build());
        markup.add(menu);

        return getPublicTestListInlineKeyboardMarkup(markup, publicTestsPage, CREATED_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getSearchResultsInlineKeyboardMarkup(Page<String> publicTestsPage) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> back = List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_PUBLIC_TEST_LIST.name())
                .build());
        markup.add(back);

        return getPublicTestListInlineKeyboardMarkup(markup, publicTestsPage, SEARCH_TESTS_PAGE_PREFIX);
    }

    public static KeyboardMarkup getPublicTestListInlineKeyboardMarkup(List<List<InlineKeyboardButton>> markup,
                                                                       Page<String> publicTestsPage,
                                                                       String callbackPrefix) {
        for (String test: publicTestsPage.getContent()) {
            List<InlineKeyboardButton> testList = new ArrayList<>();
            testList.add(InlineKeyboardButton.builder()
                    .text("📌 " + test)
                    .callbackData(PUBLIC_TEST_PREFIX + COLON + test)
                    .build());
            markup.add(testList);
        }

        if (publicTestsPage.getTotalElements() > PAGE_SIZE) {
            markup.add(getNavigationButtons(publicTestsPage, callbackPrefix));
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
        return getCourseListInlineKeyboardMarkup(markup, coursesPage);
    }

    private static KeyboardMarkup getCourseListInlineKeyboardMarkup(List<List<InlineKeyboardButton>> markup,
                                                                    Page<Course> coursesPage) {
        for (Course course: coursesPage.getContent()) {
            List<InlineKeyboardButton> courseList = new ArrayList<>();
            courseList.add(InlineKeyboardButton.builder()
                    .text("📌 " + course.getTitle())
                    .callbackData(OWN_COURSE_PREFIX + COLON + course.getId())
                    .build());
            markup.add(courseList);
        }

        if (coursesPage.getTotalElements() > PAGE_SIZE) {
            markup.add(getNavigationButtons(coursesPage, OWN_COURSE_PAGE_PREFIX));
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

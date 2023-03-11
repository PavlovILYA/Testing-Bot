package ru.mephi.knowledgechecker.strategy.impl.course.manage;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseService;
import ru.mephi.knowledgechecker.state.impl.course.manage.ManageCourseState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;
import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_COURSE;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;

@Component
public class ManageCourseStrategy extends AbstractCallbackQueryStrategy {
    private final CourseService courseService;

    public ManageCourseStrategy(@Lazy ManageCourseState manageCourseState,
                                CourseService courseService) {
        this.nextState = manageCourseState;
        this.courseService = courseService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        String coursePrefix = update.getCallbackQuery().getData().split(COLON)[0];
        return super.apply(data, update)
                && (
                        coursePrefix.equals(OWN_COURSE_PREFIX)
                        || coursePrefix.equals(SEARCH_COURSE_PREFIX)
        );
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String coursePrefix = update.getCallbackQuery().getData().split(COLON)[0];
        Long id = Long.parseLong(update.getCallbackQuery().getData().split(COLON)[1]);
        Course course = courseService.getById(id);
        data.setCourse(course);
        String message = MANAGE_COURSE.getTitle() + course.getTitle();
        KeyboardMarkup markup;

        switch (coursePrefix) {
            case OWN_COURSE_PREFIX:
                markup = getManageOwnCourseMarkup();
                break;
            case SEARCH_COURSE_PREFIX:
                markup = getManageSearchCourseMarkup();
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
                .text(GENERATE_INVITE_CODE.getDescription())
                .callbackData(GENERATE_INVITE_CODE.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(TO_PRIVATE_TEST_LIST.getDescription())
                .callbackData(TO_PRIVATE_TEST_LIST.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(ACADEMIC_PERFORMANCE.getDescription())
                .callbackData(ACADEMIC_PERFORMANCE.name())
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
}

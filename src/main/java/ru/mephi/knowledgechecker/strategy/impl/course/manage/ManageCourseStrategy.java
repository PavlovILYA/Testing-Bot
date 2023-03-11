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
import static ru.mephi.knowledgechecker.common.Constants.COLON;
import static ru.mephi.knowledgechecker.common.Constants.OWN_COURSE_PREFIX;
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
        return super.apply(data, update)
                && (
                        update.getCallbackQuery().getData().split(COLON)[0].equals(OWN_COURSE_PREFIX)
//                        || // todo delete course (с подтверждением)
//                        update.getCallbackQuery().getData().equals(DELETE_TEST.name())
        );
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Long id = Long.parseLong(update.getCallbackQuery().getData().split(COLON)[1]);
        Course course = courseService.getById(id);
        data.setCourse(course);
        data.setState(nextState);
        String message = MANAGE_COURSE.getTitle() + course.getTitle();
        sendMenuAndSave(data, message, getManageCourseMarkup());
    }

    private KeyboardMarkup getManageCourseMarkup() {
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
}

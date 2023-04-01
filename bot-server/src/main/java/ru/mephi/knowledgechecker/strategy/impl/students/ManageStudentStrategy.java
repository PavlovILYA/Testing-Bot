package ru.mephi.knowledgechecker.strategy.impl.students;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.students.ManageStudentState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;
import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.MenuTitleType.QUERY_TO_COURSE;
import static ru.mephi.knowledgechecker.common.MenuTitleType.STUDENT;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;

@Component
public class ManageStudentStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;

    public ManageStudentStrategy(@Lazy ManageStudentState manageStudentState,
                                 UserService userService) {
        this.nextState = manageStudentState;
        this.userService = userService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }

        String studentPrefix = update.getCallbackQuery().getData().split(COLON)[0];
        return studentPrefix.equals(INPUT_QUERIES_PREFIX)
                || studentPrefix.equals(STUDENT_PREFIX);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String studentPrefix = update.getCallbackQuery().getData().split(COLON)[0];
        Long studentId = Long.parseLong(update.getCallbackQuery().getData().split(COLON)[1]);
        User student = userService.get(studentId);
        data.setStudent(student);
        String message;
        KeyboardMarkup markup;

        switch (studentPrefix) {
            case INPUT_QUERIES_PREFIX:
                message = QUERY_TO_COURSE.getTitle() + student.getUsername();
                markup = getManageInputQueryMarkup();
                break;
            case STUDENT_PREFIX:
                message = STUDENT.getTitle() + student.getUsername();
                markup = getManageStudentMarkup();
                break;
            default:
                return;
        }

        data.setState(nextState);
        sendMenuAndSave(data, message, markup);
    }

    private KeyboardMarkup getManageInputQueryMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(
                InlineKeyboardButton.builder()
                        .text("⬅️")
                        .callbackData(TO_INPUT_COURSE_QUERIES.name())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(ACCEPT_INPUT_QUERY.getDescription())
                        .callbackData(ACCEPT_INPUT_QUERY.name())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(REJECT_INPUT_QUERY.getDescription())
                        .callbackData(REJECT_INPUT_QUERY.name())
                        .build()));
        return wrapInlineKeyboardMarkup(markup);
    }

    private KeyboardMarkup getManageStudentMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(
                InlineKeyboardButton.builder()
                        .text("⬅️")
                        .callbackData(TO_INPUT_COURSE_QUERIES.name())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(BLOCK_STUDENT.getDescription())
                        .callbackData(BLOCK_STUDENT.name())
                        .build()));
        return wrapInlineKeyboardMarkup(markup);
    }
}

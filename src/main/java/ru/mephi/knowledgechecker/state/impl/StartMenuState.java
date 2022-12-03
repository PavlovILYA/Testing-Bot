package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Message;
import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineSendMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.ParamsWrapper;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.state.Constants.PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.state.Constants.START_COMMAND;
import static ru.mephi.knowledgechecker.strategy.Constants.*;
import static ru.mephi.knowledgechecker.strategy.KeyboardMarkups.getStartReplyKeyboardMarkup;

@Slf4j
@Component
public class StartMenuState extends AbstractState {
    private final UserService userService;

    @Autowired
    public StartMenuState(UserService userService,
                          PublicTestListState publicTestListState,
                          CoursesListState coursesListState,
                          AdminMenuState adminMenuState) {
        this.userService = userService;
        availableStates.add(publicTestListState);
        availableStates.add(coursesListState);
        availableStates.add(adminMenuState);
    }

    @Override
    public void processCommand(Message message) {
        if (message.getText().equals(START_COMMAND)) {
            String answer = registerUser(message.getFrom());
            answer += "▶️ ГЛАВНАЯ";
            ReplySendMessageParams params = ParamsWrapper.wrapReplySendMessageParams(
                    message.getFrom().getId(), answer, getStartReplyKeyboardMarkup(), "Основное меню");
            telegramApiClient.sendMessage(params);
        } else {
            super.processCommand(message);
        }
    }

    @Override
    public void processMessage(Message message) {
        if (message.getText().equals(PUBLIC_TEST_LIST)) {
            stateContext.putState(message.getFrom().getId(), getAvailableState(PublicTestListState.class));
            InlineSendMessageParams params = ParamsWrapper.wrapInlineSendMessageParams(
                    message.getFrom().getId(), "▶️ ГЛАВНАЯ ➡️ ПУБЛИЧНЫЕ ТЕСТЫ", getTestsMarkup());
            telegramApiClient.sendMessage(params);
        } else if (message.getText().equals(COURSES_LIST)) {
            stateContext.putState(message.getFrom().getId(), getAvailableState(CoursesListState.class));
            InlineSendMessageParams params = ParamsWrapper.wrapInlineSendMessageParams(
                    message.getFrom().getId(), "▶️ ГЛАВНАЯ ➡️ СПИСОК КУРСОВ", getCoursesdMarkup());
            telegramApiClient.sendMessage(params);
        } else if (message.getText().equals(ADMIN_MENU)) {
            stateContext.putState(message.getFrom().getId(), getAvailableState(AdminMenuState.class));
            InlineSendMessageParams params = ParamsWrapper.wrapInlineSendMessageParams(
                    message.getFrom().getId(), "▶️ ГЛАВНАЯ ➡️ АДМИНИСТРАТОРСКОЕ МЕНЮ", getAdminMarkup());
            telegramApiClient.sendMessage(params);
        } else {
            super.processMessage(message);
        }
    }

    private String registerUser(UserDto userDto) {
        if (userService.getUser(userDto.getId()).isEmpty()) {
            userService.saveUser(userDto);
            return String.format("👤 Пользователь %1$s (%2$s) зарегистрирован!\n\n",
                    userDto.getFirstName(),
                    userDto.getUsername());
        }
        return "";
    }

    private List<List<InlineKeyboardButton>> getTestsMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_MAIN_MENU)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Создать тест")
                .callbackData(CREATE_PUBLIC_TEST)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Найти тест")
                .callbackData(FIND_PUBLIC_TEST)
                .build());
        markup.add(menu);

        // todo
//        List<InlineKeyboardButton> publicTests = new ArrayList<>();
//        for (test : tests) {
//            publicTests.add(InlineKeyboardButton.builder()
//                    .text(test.getName())
//                    .callbackData("public-test:" + test.getId())
//                    .build());
//        }
//        markup.add(two);
        return markup;
    }

    private List<List<InlineKeyboardButton>> getCoursesdMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_MAIN_MENU)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Поступить на курс")
                .callbackData(ATTEND_COURSE)
                .build());
        markup.add(menu);

        // todo
//        List<InlineKeyboardButton> publicTests = new ArrayList<>();
//        for (test : tests) {
//            publicTests.add(InlineKeyboardButton.builder()
//                    .text(test.getName())
//                    .callbackData("public-test:" + test.getId())
//                    .build());
//        }
//        markup.add(two);
        return markup;
    }

    private List<List<InlineKeyboardButton>> getAdminMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_MAIN_MENU)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Создать курс")
                .callbackData(CREATE_COURSE)
                .build());
        markup.add(menu);

        // todo
//        List<InlineKeyboardButton> publicTests = new ArrayList<>();
//        for (test : tests) {
//            publicTests.add(InlineKeyboardButton.builder()
//                    .text(test.getName())
//                    .callbackData("public-test:" + test.getId())
//                    .build());
//        }
//        markup.add(two);
        return markup;
    }
}

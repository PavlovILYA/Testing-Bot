package ru.mephi.knowledgechecker.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.strategy.UpdateStrategy;

import static ru.mephi.knowledgechecker.strategy.Constants.START_COMMAND;
import static ru.mephi.knowledgechecker.strategy.KeyboardMarkups.getStartReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class StartCommandStrategy extends AbstractBotCommandStrategy implements UpdateStrategy {
    private final TelegramApiClient telegramApiClient;
    private final UserService userService;

    @Override
    public boolean apply(Update update) {
        return super.apply(update) && update.getMessage().getText().equals(START_COMMAND);
    }

    @Override
    public void process(Update update) {
        UserDto userDto = update.getMessage().getFrom();
        String answer = registerUser(userDto);
        answer += "▶️ ГЛАВНАЯ";
        sendStartMenu(update.getMessage().getChat().getId(), answer);
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

    private void sendStartMenu(Long chatId, String text) {
        ReplySendMessageParams params =
                wrapReplySendMessageParams(chatId, text, getStartReplyKeyboardMarkup(), "Основное меню");
        telegramApiClient.sendMessage(params);
    }
}

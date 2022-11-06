package ru.mephi.knowledgechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.knowledgechecker.dto.telegram.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.InlineKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.SendMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.Update;
import ru.mephi.knowledgechecker.service.TelegramApiService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UpdateController {
    private final TelegramApiService telegramApiService;

    @PostMapping
    public void giveUpdate(@RequestBody Update update) {
        log.info("Updates: {}", update);
        SendMessageParams params = SendMessageParams.builder()
                .chatId(update.getMessage().getChat().getId())
                .text("Привет, придурок! 🐸")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .inlineKeyboard(getInlineKeyboardMarkup())
                        .build())
                .build();
        telegramApiService.sendMessage(params);
    }

    private List<List<InlineKeyboardButton>> getInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> one = new ArrayList<>();
        one.add(InlineKeyboardButton.builder()
                .text("Текст1.1")
                .callbackData("callbackData1.1")
                .build());
        one.add(InlineKeyboardButton.builder()
                .text("Текст1.2")
                .callbackData("callbackData1.2")
                .build());
        List<InlineKeyboardButton> two = new ArrayList<>();
        two.add(InlineKeyboardButton.builder()
                .text("Текст2.1")
                .callbackData("callbackData2.1")
                .build());
        two.add(InlineKeyboardButton.builder()
                .text("Текст2.2")
                .callbackData("callbackData2.2")
                .build());
        markup.add(one);
        markup.add(two);
        return markup;
    }
}

package ru.mephi.knowledgechecker.strategy.impl.test.create;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.state.impl.test.create.TestCreatingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.CREATE_PUBLIC_TEST;
import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ToTestCreatingStrategy extends AbstractCallbackQueryStrategy {
    public ToTestCreatingStrategy(@Lazy TestCreatingState nextState) {
        this.nextState = nextState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(CREATE_PUBLIC_TEST);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        Long userId = update.getCallbackQuery().getFrom().getId();
        String message = "Введите уникальное название теста";
        MessageParams params =
                wrapMessageParams(userId, message,
                        List.of(new MessageEntity("bold", 0, message.length()),
                                new MessageEntity("underline", 8, 10)),
                        getInlineKeyboardMarkup());
        putStateToContext(userId, nextState, data);
        telegramApiClient.sendMessage(params);
    }

    private KeyboardMarkup getInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(PUBLIC_TEST_LIST)
                .build());
        markup.add(menu);
        return wrapInlineKeyboardMarkup(markup);
    }
}

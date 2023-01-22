package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.Command;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.state.impl.menu.MainMenuState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractActionStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;
import static ru.mephi.knowledgechecker.common.Constants.TO_MAIN_MENU;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getStartReplyKeyboardMarkup;
import static ru.mephi.knowledgechecker.strategy.impl.AbstractBotCommandStrategy.BOT_COMMAND;

@Slf4j
@Component
public class ToMainMenuStrategy extends AbstractActionStrategy {
    public ToMainMenuStrategy(@Lazy MainMenuState nextState) {
        this.nextState = nextState;
    }

    @Override
    public boolean apply(Update update) {
        return update.getCallbackQuery() != null
                && update.getCallbackQuery().getData().equals(TO_MAIN_MENU)
                ||
                update.getMessage() != null
                && update.getMessage().getEntities() != null
                && update.getMessage().getEntities().stream()
                .anyMatch(e -> e.getType().equals(BOT_COMMAND))
                && update.getMessage().getText().equals(Command.START.getName());
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        Long userId = update.getCallbackQuery() != null
                ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
        putStateToContext(userId, nextState, data);
        sendStartMenu(userId, "🔽️\nГЛАВНОЕ МЕНЮ ⤵️");
    }

    private void sendStartMenu(Long chatId, String text) {
        MessageParams params =
                wrapMessageParams(chatId, text,
                        List.of(new MessageEntity("bold", 0, text.length())),
                        getStartReplyKeyboardMarkup());
        telegramApiClient.sendMessage(params);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.Command;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.menu.MainMenuState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractActionStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.CHECK_0_QUESTIONS;
import static ru.mephi.knowledgechecker.common.Constants.TO_MAIN_MENU;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getStartReplyKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;
import static ru.mephi.knowledgechecker.strategy.impl.AbstractBotCommandStrategy.BOT_COMMAND;

@Slf4j
@Component
public class ToMainMenuStrategy extends AbstractActionStrategy {
    private final TestService testService;

    public ToMainMenuStrategy(@Lazy MainMenuState nextState,
                              TestService testService) {
        this.nextState = nextState;
        this.testService = testService;
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
    public void process(Update update, Map<String, Object> data) throws StrategyProcessException {
        if (data.get(CHECK_0_QUESTIONS) != null) {
            String uniqueTitle = (String) data.get(CHECK_0_QUESTIONS);
            Test test = testService.getByUniqueTitle(uniqueTitle);
            if (test.getOpenQuestions().size() + test.getVariableQuestions().size() == 0) {
                throw new StrategyProcessException(update.getCallbackQuery().getFrom().getId(),
                        "Необходимо добавить как минимум один вопрос");
            }
            data.remove(CHECK_0_QUESTIONS);
        }
        Long userId = update.getCallbackQuery() != null
                ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
        putStateToContext(userId, nextState, data);
        sendStartMenu(userId);
    }

    private void sendStartMenu(Long chatId) {
        String text = "🔽️\nГЛАВНОЕ МЕНЮ ⤵️";
        MessageParams params =
                wrapMessageParams(chatId, text,
                        List.of(new MessageEntity(TextType.BOLD, 0, text.length())),
                        getStartReplyKeyboardMarkup());
        telegramApiClient.sendMessage(params);
    }
}

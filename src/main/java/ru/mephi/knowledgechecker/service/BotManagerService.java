package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.state.StateContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotManagerService {
    private static final String BOT_COMMAND = "bot_command"; // todo
    private final StateContext stateContext;

    public void process(Update update) {
        if (update.getMessage() != null) {
            if (update.getMessage().getEntities() != null && update.getMessage().getEntities().stream()
                    .anyMatch(e -> e.getType().equals(BOT_COMMAND))) {
                stateContext.processCommand(update.getMessage());
            } else {
                stateContext.processMessage(update.getMessage());
            }
        } else if (update.getCallbackQuery() != null) {
            stateContext.processCallbackReply(update.getCallbackQuery());
        } else {
            log.warn("Update {} doesn't have neither message nor callback query", update.getId());
        }
    }
}

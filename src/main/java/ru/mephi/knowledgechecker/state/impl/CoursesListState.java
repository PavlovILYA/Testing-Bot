package ru.mephi.knowledgechecker.state.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.CallbackQuery;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;
import ru.mephi.knowledgechecker.state.ParamsWrapper;

import static ru.mephi.knowledgechecker.state.Constants.TO_MAIN_MENU;
import static ru.mephi.knowledgechecker.strategy.KeyboardMarkups.getStartReplyKeyboardMarkup;

@Component
public class CoursesListState extends AbstractState {
    @Autowired
    public CoursesListState(@Lazy StartMenuState startMenuState) {
        availableStates.add(startMenuState);
    }

    @Override
    public void processCallbackQuery(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().equals(TO_MAIN_MENU)) {
            stateContext.putState(callbackQuery.getFrom().getId(), getAvailableState(StartMenuState.class));
            ReplySendMessageParams params = ParamsWrapper.wrapReplySendMessageParams(
                    callbackQuery.getFrom().getId(), "▶️ ГЛАВНАЯ", getStartReplyKeyboardMarkup(), "Основное меню");
            telegramApiClient.sendMessage(params);
        } else {
            super.processCallbackQuery(callbackQuery);
        }
    }
}

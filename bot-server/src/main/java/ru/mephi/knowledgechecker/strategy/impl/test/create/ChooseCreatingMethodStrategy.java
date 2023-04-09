package ru.mephi.knowledgechecker.strategy.impl.test.create;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.create.ChoiceOfCreatingMethodState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;
import static ru.mephi.knowledgechecker.common.MenuTitleType.TEST_CREATING_TYPE;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;

@Component
public class ChooseCreatingMethodStrategy extends AbstractCallbackQueryStrategy {
    public ChooseCreatingMethodStrategy(@Lazy ChoiceOfCreatingMethodState choiceOfCreatingMethodState) {
        nextState = choiceOfCreatingMethodState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(CREATE_TEST.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String backCallbackData = data.getCourse() == null
                ? TO_PUBLIC_TEST_LIST.name()
                : TO_PRIVATE_TEST_LIST.name();
        data.setState(nextState);
        sendMenuAndSave(data, TEST_CREATING_TYPE.getTitle(), getInlineKeyboardMarkup(backCallbackData));
    }

    private KeyboardMarkup getInlineKeyboardMarkup(String backCallbackData) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(backCallbackData)
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(IMPORT.getDescription())
                .callbackData(IMPORT.name())
                .build()));
        markup.add(List.of(InlineKeyboardButton.builder()
                .text(CREATE_HERE.getDescription())
                .callbackData(CREATE_HERE.name())
                .build()));
        return wrapInlineKeyboardMarkup(markup);
    }
}

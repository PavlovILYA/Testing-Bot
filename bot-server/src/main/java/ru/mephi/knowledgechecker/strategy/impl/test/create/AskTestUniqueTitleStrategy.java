package ru.mephi.knowledgechecker.strategy.impl.test.create;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.BotState;
import ru.mephi.knowledgechecker.state.impl.test.create.file.FileTestCreatingState;
import ru.mephi.knowledgechecker.state.impl.test.create.manual.ManualTestCreatingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AskTestUniqueTitleStrategy extends AbstractCallbackQueryStrategy {
    private final BotState manualTestCreatingState;
    private final BotState fileTestCreatingState;

    public AskTestUniqueTitleStrategy(@Lazy ManualTestCreatingState manualTestCreatingState,
                                      @Lazy FileTestCreatingState fileTestCreatingState) {
        this.manualTestCreatingState = manualTestCreatingState;
        this.fileTestCreatingState = fileTestCreatingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                &&
                (update.getCallbackQuery().getData().equals(IMPORT.name())
                || update.getCallbackQuery().getData().equals(CREATE_HERE.name()));
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String message = "Введите уникальное название теста (максимум 30 символов)";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length()),
                        new MessageEntity(TextType.UNDERLINE, 8, 10)),
                getInlineKeyboardMarkup());
        deleteMenu(data);
        if (update.getCallbackQuery().getData().equals(CREATE_HERE.name())) {
            data.setState(manualTestCreatingState);
        } else {
            data.setState(fileTestCreatingState);
        }
        sendMessageAndSave(params, data);
    }

    private KeyboardMarkup getInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(CREATE_TEST.name())
                .build());
        markup.add(menu);
        return wrapInlineKeyboardMarkup(markup);
    }
}

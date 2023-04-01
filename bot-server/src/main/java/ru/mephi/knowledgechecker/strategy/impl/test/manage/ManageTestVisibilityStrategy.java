package ru.mephi.knowledgechecker.strategy.impl.test.manage;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.manage.ManageTestVisibilityState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.MANAGE_VISIBILITY;
import static ru.mephi.knowledgechecker.common.Constants.COLON;
import static ru.mephi.knowledgechecker.common.Constants.OWN_PRIVATE_TEST_PREFIX;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_TEST;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.model.test.VisibilityType.*;

@Component
public class ManageTestVisibilityStrategy extends AbstractCallbackQueryStrategy {
    private final TestService testService;

    public ManageTestVisibilityStrategy(@Lazy ManageTestVisibilityState manageTestVisibilityState,
                                        TestService testService) {
        this.nextState = manageTestVisibilityState;
        this.testService = testService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update) &&
                (update.getCallbackQuery().getData().equals(MANAGE_VISIBILITY.name()) ||
                    update.getCallbackQuery().getData().equals(INVISIBLE.name()) ||
                    update.getCallbackQuery().getData().equals(TRAIN.name()) ||
                    update.getCallbackQuery().getData().equals(ESTIMATED.name()));
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Test test = data.getTest();
        if (!update.getCallbackQuery().getData().equals(MANAGE_VISIBILITY.name())) {
            test.setVisibility(VisibilityType.valueOf(update.getCallbackQuery().getData()));
            test = testService.save(test);
            data.setTest(test);
        }
        String message = MANAGE_TEST.getTitle() + test.getUniqueTitle() + " (" + test.getVisibility().name() + ")";
        data.setState(nextState);
        sendMenuAndSave(data, message, getManageVisibilityKeyboardMarkup(test));
    }

    private KeyboardMarkup getManageVisibilityKeyboardMarkup(Test test) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(OWN_PRIVATE_TEST_PREFIX + COLON + test.getUniqueTitle())
                .build()));
        switch (test.getVisibility()) {
            case INVISIBLE:
                markup.add(List.of(InlineKeyboardButton.builder()
                        .text(TRAIN.getDescription())
                        .callbackData(TRAIN.name())
                        .build()));
                markup.add(List.of(InlineKeyboardButton.builder()
                        .text(ESTIMATED.getDescription())
                        .callbackData(ESTIMATED.name())
                        .build()));
                break;
            case TRAIN:
                markup.add(List.of(InlineKeyboardButton.builder()
                        .text(INVISIBLE.getDescription())
                        .callbackData(INVISIBLE.name())
                        .build()));
                markup.add(List.of(InlineKeyboardButton.builder()
                        .text(ESTIMATED.getDescription())
                        .callbackData(ESTIMATED.name())
                        .build()));
                break;
            case ESTIMATED:
                markup.add(List.of(InlineKeyboardButton.builder()
                        .text(INVISIBLE.getDescription())
                        .callbackData(INVISIBLE.name())
                        .build()));
                markup.add(List.of(InlineKeyboardButton.builder()
                        .text(TRAIN.getDescription())
                        .callbackData(TRAIN.name())
                        .build()));
                break;
            default:
        }
        return wrapInlineKeyboardMarkup(markup);
    }
}

package ru.mephi.knowledgechecker.strategy.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineSendMessageParams;
import ru.mephi.knowledgechecker.state.impl.CoursesListState;
import ru.mephi.knowledgechecker.strategy.Constants;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.state.ParamsWrapper.wrapInlineSendMessageParams;

@Component
public class ToCoursesListStrategy extends AbstractMessageStrategy {
    public ToCoursesListStrategy(@Lazy CoursesListState nextState) {
        this.nextState = nextState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getMessage().getText().equals(Constants.COURSES_LIST);
    }

    @Override
    public void process(Update update) {
        Long userId = update.getMessage().getChat().getId();
        InlineSendMessageParams params =
                wrapInlineSendMessageParams(userId, "▶️ ГЛАВНАЯ ➡️ КУРСЫ", getInlineKeyboardMarkup());
        putStateToContext(userId, nextState);
        telegramApiClient.sendMessage(params);
    }

    private List<List<InlineKeyboardButton>> getInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(Constants.TO_MAIN_MENU)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Поступить на курс")
                .callbackData(Constants.ATTEND_COURSE)
                .build());
        markup.add(menu);

        // todo
//        List<InlineKeyboardButton> publicTests = new ArrayList<>();
//        for (test : tests) {
//            publicTests.add(InlineKeyboardButton.builder()
//                    .text(test.getName())
//                    .callbackData("public-test:" + test.getId())
//                    .build());
//        }
//        markup.add(two);
        return markup;
    }
}

package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.Constants;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.state.impl.menu.CoursesListState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
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
    public void process(Update update, Map<String, Object> data) {
        Long userId = update.getMessage().getChat().getId();
        String text = "🔽\nГЛАВНОЕ МЕНЮ\n⬇️\n️КУРСЫ";
        MessageParams params =
                wrapMessageParams(userId, text, List.of(new MessageEntity(TextType.BOLD, 0, text.length())),
                        getInlineKeyboardMarkup());
        putStateToContext(userId, nextState, data);
        telegramApiClient.sendMessage(params);
    }

    private KeyboardMarkup getInlineKeyboardMarkup() {
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
//                    .callbackData(PUBLIC_TEST_PREFIX + ":" + test.getId())
//                    .build());
//        }
//        markup.add(two);
        return wrapInlineKeyboardMarkup(markup);
    }
}

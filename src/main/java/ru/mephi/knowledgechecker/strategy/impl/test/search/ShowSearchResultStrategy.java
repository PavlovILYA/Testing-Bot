package ru.mephi.knowledgechecker.strategy.impl.test.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.DataType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.state.impl.test.search.TestSearchResultState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_PREFIX;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestListInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class ShowSearchResultStrategy extends AbstractMessageStrategy {
    private final TestService testService;
    private final PublicTestListState publicTestListState;
    private final UserService userService;

    public ShowSearchResultStrategy(TestService testService,
                                    @Lazy TestSearchResultState testSearchResultState,
                                    @Lazy PublicTestListState publicTestListState,
                                    UserService userService) {
        this.testService = testService;
        this.nextState = testSearchResultState;
        this.publicTestListState = publicTestListState;
        this.userService = userService;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update);
    }

    @Override
    public void process(Update update, Map<DataType, Object> data) throws StrategyProcessException {
        String keyWords = update.getMessage().getText().replaceAll(";", "|");
        List<String> testTitles = testService.findTests(keyWords, update.getMessage().getFrom().getId());
        if (testTitles.size() != 0) {
            sendResults(testTitles, update.getMessage().getFrom().getId(), data);
        } else {
            sendNotFound(update.getMessage().getFrom().getId(), data);
        }
    }

    private void sendNotFound(Long userId, Map<DataType, Object> data) {
        putStateToContext(userId, publicTestListState, data);
        String message = "По данному запросу ничего не найдено 🤷🏼‍";
        MessageParams params =
                wrapMessageParams(userId, message,
                        List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                        null);
        telegramApiClient.sendMessage(params);

        User user = userService.get(userId);
        String text = "🔽\nГЛАВНОЕ МЕНЮ\n⬇️️\nПУБЛИЧНЫЕ ТЕСТЫ";
        params =
                wrapMessageParams(userId, text, List.of(new MessageEntity(TextType.BOLD, 0, text.length())),
                        getPublicTestListInlineKeyboardMarkup(user));
        telegramApiClient.sendMessage(params);
    }

    private void sendResults(List<String> testTitles, Long userId, Map<DataType, Object> data) {
        String message = "🕵🏻‍ Результаты поиска:";
        MessageParams params =
                wrapMessageParams(userId, message,
                        List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                        getInlineKeyboardMarkup(testTitles));
        putStateToContext(userId, nextState, data);
        telegramApiClient.sendMessage(params);
    }

    private KeyboardMarkup getInlineKeyboardMarkup(List<String> testTitles) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> back = List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(PUBLIC_TEST_LIST)
                .build());
        markup.add(back);

        for (String title: testTitles) {
            List<InlineKeyboardButton> testList = new ArrayList<>();
            testList.add(InlineKeyboardButton.builder()
                    .text("📌 " + title)
                    .callbackData(PUBLIC_TEST_PREFIX + ":" + title)
                    .build());
            markup.add(testList);
        }
        return wrapInlineKeyboardMarkup(markup);
    }
}

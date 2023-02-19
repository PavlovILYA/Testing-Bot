package ru.mephi.knowledgechecker.strategy.impl.test.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.state.impl.test.search.TestSearchResultState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.ArrayList;
import java.util.List;

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

    public ShowSearchResultStrategy(TestService testService,
                                    @Lazy TestSearchResultState testSearchResultState,
                                    @Lazy PublicTestListState publicTestListState) {
        this.testService = testService;
        this.nextState = testSearchResultState;
        this.publicTestListState = publicTestListState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update);
    }

    @Override
    public void process(User user, Update update) throws StrategyProcessException {
        String keyWords = update.getMessage().getText().replaceAll(";", "|");
        List<String> testTitles = testService.findTests(keyWords, user.getId());
        if (testTitles.size() != 0) {
            sendResults(user, testTitles);
        } else {
            sendNotFound(user);
        }
    }

    private void sendNotFound(User user) {
        String message = "По данному запросу ничего не найдено 🤷🏼‍";
        SendMessageParams params = wrapMessageParams(user.getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                null);
        telegramApiClient.sendMessage(params);

        message = "🔽\nГЛАВНОЕ МЕНЮ\n⬇️️\nПУБЛИЧНЫЕ ТЕСТЫ";
        params = wrapMessageParams(user.getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                getPublicTestListInlineKeyboardMarkup(user));
        sendMenuAndSave(params, publicTestListState, user.getData());
    }

    private void sendResults(User user, List<String> testTitles) {
        String message = "🕵🏻‍ Результаты поиска:";
        SendMessageParams params = wrapMessageParams(user.getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                getInlineKeyboardMarkup(testTitles));
        sendMessageAndSave(params, nextState, user.getData());
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

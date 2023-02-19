package ru.mephi.knowledgechecker.strategy.impl.test.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.state.impl.test.search.TestSearchResultState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestListInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.PUBLIC_TEST_LIST;
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
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String keyWords = update.getMessage().getText().replaceAll(SEMICOLON, "|");
        List<String> testTitles = testService.findTests(keyWords, data.getUser().getId());
        if (testTitles.size() != 0) {
            sendResults(data, testTitles);
        } else {
            sendNotFound(data);
        }
    }

    private void sendNotFound(CurrentData data) {
        String message = "По данному запросу ничего не найдено 🤷🏼‍";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                null);
        telegramApiClient.sendMessage(params);

        params = wrapMessageParams(data.getUser().getId(), PUBLIC_TEST_LIST.getTitle(),
                List.of(new MessageEntity(TextType.BOLD, 0, PUBLIC_TEST_LIST.getTitle().length())),
                getPublicTestListInlineKeyboardMarkup(data.getUser()));
        data.setState(publicTestListState);
        sendMenuAndSave(params, data);
    }

    private void sendResults(CurrentData data, List<String> testTitles) {
        String message = "🕵🏻‍ Результаты поиска:";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                getInlineKeyboardMarkup(testTitles));
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }

    private KeyboardMarkup getInlineKeyboardMarkup(List<String> testTitles) {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> back = List.of(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_PUBLIC_TEST_LIST.name())
                .build());
        markup.add(back);

        for (String title: testTitles) {
            List<InlineKeyboardButton> testList = new ArrayList<>();
            testList.add(InlineKeyboardButton.builder()
                    .text("📌 " + title)
                    .callbackData(PUBLIC_TEST_PREFIX + COLON + title)
                    .build());
            markup.add(testList);
        }
        return wrapInlineKeyboardMarkup(markup);
    }
}

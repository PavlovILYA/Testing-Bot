package ru.mephi.knowledgechecker.strategy.impl.test.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.state.impl.test.search.TestSearchResultState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.Constants.SEMICOLON;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestMenuInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getSearchResultsInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.MenuTitleType.SEARCH_RESULT;
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
        Page<String> testTitlesPage = testService.findTests(keyWords, data.getUser().getId());
        if (testTitlesPage.getTotalElements() != 0) {
            data.setSearchKeyWords(keyWords);
            sendResults(data, testTitlesPage);
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

        Page<String> publicTests = testService.getCreatedTests(data.getUser().getId());
        params = wrapMessageParams(data.getUser().getId(), PUBLIC_TEST_LIST.getTitle(),
                List.of(new MessageEntity(TextType.BOLD, 0, PUBLIC_TEST_LIST.getTitle().length())),
                getPublicTestMenuInlineKeyboardMarkup(publicTests));
        data.setState(publicTestListState);
        sendMenuAndSave(params, data);
    }

    private void sendResults(CurrentData data, Page<String> testTitlesPage) {
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), SEARCH_RESULT.getTitle(),
                List.of(new MessageEntity(TextType.BOLD, 0, SEARCH_RESULT.getTitle().length())),
                getSearchResultsInlineKeyboardMarkup(testTitlesPage));
        data.setState(nextState);
        sendMenuAndSave(params, data);
    }
}

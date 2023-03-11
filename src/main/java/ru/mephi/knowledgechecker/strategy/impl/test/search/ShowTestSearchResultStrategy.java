package ru.mephi.knowledgechecker.strategy.impl.test.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.menu.TestListState;
import ru.mephi.knowledgechecker.state.impl.test.search.TestSearchResultState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.nothingIsFoundParams;
import static ru.mephi.knowledgechecker.common.Constants.SEMICOLON;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestMenuInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getTestSearchResultsInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.MenuTitleType.TEST_SEARCH_RESULT;

@Slf4j
@Component
public class ShowTestSearchResultStrategy extends AbstractMessageStrategy {
    private final TestService testService;
    private final TestListState testListState;

    public ShowTestSearchResultStrategy(TestService testService,
                                        @Lazy TestSearchResultState testSearchResultState,
                                        @Lazy TestListState testListState) {
        this.testService = testService;
        this.nextState = testSearchResultState;
        this.testListState = testListState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update);
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
        telegramApiClient.sendMessage(nothingIsFoundParams(data.getUser().getId()));

        Page<String> publicTests = testService.getCreatedTests(data.getUser().getId());
        data.setState(testListState);
        sendMenuAndSave(data, PUBLIC_TEST_LIST.getTitle(), getPublicTestMenuInlineKeyboardMarkup(publicTests));
    }

    private void sendResults(CurrentData data, Page<String> testTitlesPage) {
        data.setState(nextState);
        sendMenuAndSave(data, TEST_SEARCH_RESULT.getTitle(), getTestSearchResultsInlineKeyboardMarkup(testTitlesPage));
    }
}

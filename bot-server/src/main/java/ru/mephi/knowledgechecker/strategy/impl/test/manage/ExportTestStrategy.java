package ru.mephi.knowledgechecker.strategy.impl.test.manage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.CallbackDataType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.httpclient.ParserApiClient;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.io.File;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;
import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAdminCourseTestManageKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getTestManageKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_TEST;

@Component
@RequiredArgsConstructor
public class ExportTestStrategy extends AbstractCallbackQueryStrategy {
    private final SolvingService solvingService;
    private final ParserApiClient parserApiClient;
    private final TelegramApiClient telegramApiClient;

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }
        String testPrefix = update.getCallbackQuery().getData().split(COLON)[0];
        if (!testPrefix.equals(TO_PUBLIC_TEST_LIST.name())
                && !testPrefix.equals(TO_PRIVATE_TEST_LIST.name())) {
            return false;
        }
        return update.getCallbackQuery().getData().split(COLON)[1].equals(EXPORT.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        File file = parserApiClient.exportTest(data.getUser().getId(), data.getTest());
        deleteMenu(data);
        telegramApiClient.sendDocument(data.getUser().getId(), file);
        file.delete();
        String backCallbackData = update.getCallbackQuery().getData().split(COLON)[0];
        KeyboardMarkup markup;
        switch (CallbackDataType.valueOf(backCallbackData)) {
            case TO_PUBLIC_TEST_LIST:
                markup = getTestManageKeyboardMarkup(true, backCallbackData);
                break;
            case TO_PRIVATE_TEST_LIST:
                long uncheckedWorkCount = solvingService.getUncheckedWorksCount(data.getTest().getId());
                markup = getAdminCourseTestManageKeyboardMarkup(true, backCallbackData, uncheckedWorkCount);
                break;
            default:
                return;
        }
        sendMenuAndSave(data, MANAGE_TEST.getTitle() + data.getTest().getUniqueTitle(), markup);
    }
}

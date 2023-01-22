package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.solve.ChooseSolvingTypeState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getTestSolvingTypesInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ToChooseSolvingTypeStrategy extends AbstractCallbackQueryStrategy {
    private final TestService testService;

    public ToChooseSolvingTypeStrategy(TestService testService,
                                       @Lazy ChooseSolvingTypeState chooseSolvingTypeState) {
        this.testService = testService;
        this.nextState = chooseSolvingTypeState;
    }

    @Override
    public boolean apply(Update update) {
        if (!super.apply(update) || !update.getCallbackQuery().getData().split(":")[0].equals("public-test")) {
            return false;
        }

        String uniqueTitle = update.getCallbackQuery().getData().split(":")[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);
        return test != null
                && test.getTestType().equals(TestType.PUBLIC);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        String message = "Выберите вариант прохождения теста";
        MessageParams params =
                wrapMessageParams(update.getCallbackQuery().getFrom().getId(), message,
                        List.of(new MessageEntity("bold", 0, message.length())),
                        getTestSolvingTypesInlineKeyboardMarkup());
        data.put("testUniqueTitle", update.getCallbackQuery().getData().split(":")[1]);
        putStateToContext(update.getCallbackQuery().getFrom().getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

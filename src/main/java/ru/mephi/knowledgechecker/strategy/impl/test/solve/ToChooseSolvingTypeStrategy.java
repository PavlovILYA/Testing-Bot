package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.solve.ChooseSolvingTypeState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_PREFIX;
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
        if (!super.apply(update) || !update.getCallbackQuery().getData().split(":")[0].equals(PUBLIC_TEST_PREFIX)) {
            return false;
        }

        String uniqueTitle = update.getCallbackQuery().getData().split(":")[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);
        return test != null
                && test.getTestType().equals(TestType.PUBLIC);
    }

    @Override
    public void process(User user, Update update) throws StrategyProcessException {
        String uniqueTitle = update.getCallbackQuery().getData().split(":")[1];
        Test test = testService.getByUniqueTitle(uniqueTitle);

        CurrentData data = user.getData();
        data.setTest(test);
        saveToContext(nextState, data);

        String message = "Выберите вариант прохождения теста";
        MessageParams params = wrapMessageParams(user.getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                getTestSolvingTypesInlineKeyboardMarkup());
        telegramApiClient.sendMessage(params);
    }
}

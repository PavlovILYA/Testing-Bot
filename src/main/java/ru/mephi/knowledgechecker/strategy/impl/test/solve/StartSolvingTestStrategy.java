package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.solving.SolvingType;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.state.impl.test.solve.SolvingTestState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestListInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class StartSolvingTestStrategy extends AbstractCallbackQueryStrategy {

    private final SolvingService solvingService;
    private final ShowQuestionStrategy showQuestionStrategy;
    private final PublicTestListState publicTestListState;

    public StartSolvingTestStrategy(SolvingService solvingService, ShowQuestionStrategy showQuestionStrategy,
                                    @Lazy SolvingTestState solvingTestState,
                                    @Lazy PublicTestListState publicTestListState) {
        this.solvingService = solvingService;
        this.showQuestionStrategy = showQuestionStrategy;
        this.nextState = solvingTestState;
        this.publicTestListState = publicTestListState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                &&
                (update.getCallbackQuery().getData().equals(SolvingType.INSTANT_DEMONSTRATION_ANSWER.name())
                || update.getCallbackQuery().getData().equals(SolvingType.REPORT_GENERATING_AT_THE_END.name()));
    }

    @Override
    public void process(User user, Update update) throws StrategyProcessException {
        CurrentData data = user.getData();
        Test test = data.getTest();
        if (test.getMaxQuestionsNumber() == null ||
                test.getVariableQuestions().size() + test.getOpenQuestions().size() == 0) {
            data.setTest(null);
            saveToContext(publicTestListState, data);

            String message = "Тест составлен автором некорректно 🆘";
            MessageParams params = wrapMessageParams(user.getId(), message,
                    List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                    null);
            telegramApiClient.sendMessage(params);

            message = "🔽\nГЛАВНОЕ МЕНЮ\n⬇️️\nПУБЛИЧНЫЕ ТЕСТЫ";
            params = wrapMessageParams(user.getId(), message,
                    List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                    getPublicTestListInlineKeyboardMarkup(user));
            telegramApiClient.sendMessage(params);
            return;
        }

        Solving solving = solvingService.generateQuestions(user, test,
                SolvingType.valueOf(update.getCallbackQuery().getData()));
        data.setTest(null);
        saveToContext(nextState, data);
        showQuestionStrategy.sendQuestion(solving, data, update);
    }
}

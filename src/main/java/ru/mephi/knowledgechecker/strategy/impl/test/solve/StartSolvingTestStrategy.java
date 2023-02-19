package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendPopupParams;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.solving.SolvingType;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.CurrentData;
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
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Test test = data.getTest();
        if (test.getMaxQuestionsNumber() == null ||
                test.getVariableQuestions().size() + test.getOpenQuestions().size() == 0) {
            data.setTest(null);

            SendPopupParams popup = SendPopupParams.builder()
                    .callbackQueryId(update.getCallbackQuery().getId())
                    .text("Тест составлен автором некорректно 🆘")
                    .showAlert(true)
                    .build();
            telegramApiClient.answerCallbackQuery(popup);

            String message = "🔽\nГЛАВНОЕ МЕНЮ\n⬇️️\nПУБЛИЧНЫЕ ТЕСТЫ";
            SendMessageParams params = wrapMessageParams(data.getUser().getId(), message,
                    List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                    getPublicTestListInlineKeyboardMarkup(data.getUser()));
            data.setState(publicTestListState);
            sendMenuAndSave(params, data);
            return;
        }

        Solving solving = solvingService.generateQuestions(data.getUser(), test,
                SolvingType.valueOf(update.getCallbackQuery().getData()));
        data.setTest(null);
        clearInlineKeyboard(data);
        data.setState(nextState);
        saveToContext(data);
        showQuestionStrategy.sendQuestion(solving, data, update);
    }
}

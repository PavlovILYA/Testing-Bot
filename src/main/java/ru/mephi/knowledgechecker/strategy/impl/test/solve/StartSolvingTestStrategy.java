package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.state.impl.test.solve.SolvingTestState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToPublicTestListStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.DEMONSTRATION_ANSWER;
import static ru.mephi.knowledgechecker.common.Constants.REPORT_GENERATING;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestListInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class StartSolvingTestStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;
    private final TestService testService;
    private final SolvingService solvingService;
    private final ShowQuestionStrategy showQuestionStrategy;
    private final PublicTestListState publicTestListState;

    public StartSolvingTestStrategy(UserService userService,
                                    TestService testService,
                                    SolvingService solvingService, ShowQuestionStrategy showQuestionStrategy,
                                    @Lazy SolvingTestState solvingTestState,
                                    @Lazy ToPublicTestListStrategy toPublicTestListStrategy,
                                    @Lazy PublicTestListState publicTestListState) {
        this.userService = userService;
        this.testService = testService;
        this.solvingService = solvingService;
        this.showQuestionStrategy = showQuestionStrategy;
        this.nextState = solvingTestState;
        this.publicTestListState = publicTestListState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                &&
                (update.getCallbackQuery().getData().equals(DEMONSTRATION_ANSWER)
                || update.getCallbackQuery().getData().equals(REPORT_GENERATING));
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        User user = userService.get(update.getCallbackQuery().getFrom().getId());
        Test test = testService.getByUniqueTitle((String) data.get("testUniqueTitle"));
        if (test.getMaxQuestionsNumber() == null ||
                test.getVariableQuestions().size() + test.getOpenQuestions().size() == 0) {
            putStateToContext(user.getId(), publicTestListState, data);
            String message = "Тест составлен автором некорректно 🆘";
            MessageParams params =
                    wrapMessageParams(user.getId(), message,
                            List.of(new MessageEntity("bold", 0, message.length())),
                            null);
            telegramApiClient.sendMessage(params);

            String text = "🔽\nГЛАВНОЕ МЕНЮ\n⬇️️\nПУБЛИЧНЫЕ ТЕСТЫ";
            params =
                    wrapMessageParams(user.getId(), text, List.of(new MessageEntity("bold", 0, text.length())),
                            getPublicTestListInlineKeyboardMarkup(user));
            telegramApiClient.sendMessage(params);
            return;
        }
        Solving solving = solvingService.generateQuestions(user, test);
        data.remove("testUniqueTitle");
        data.put("solvingType", update.getCallbackQuery().getData());
        putStateToContext(update.getCallbackQuery().getFrom().getId(), nextState, data);
        showQuestionStrategy.sendQuestion(solving, data, update);
    }
}

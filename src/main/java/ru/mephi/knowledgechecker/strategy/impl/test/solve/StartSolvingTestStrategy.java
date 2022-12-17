package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.solve.SolvingTestState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.DEMONSTRATION_ANSWER;
import static ru.mephi.knowledgechecker.common.Constants.REPORT_GENERATING;

@Component
public class StartSolvingTestStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;
    private final TestService testService;
    private final SolvingService solvingService;
    private final ShowQuestionStrategy showQuestionStrategy;

    public StartSolvingTestStrategy(UserService userService,
                                    TestService testService,
                                    SolvingService solvingService, ShowQuestionStrategy showQuestionStrategy,
                                    @Lazy SolvingTestState solvingTestState) {
        this.userService = userService;
        this.testService = testService;
        this.solvingService = solvingService;
        this.showQuestionStrategy = showQuestionStrategy;
        this.nextState = solvingTestState;
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
        Solving solving = solvingService.generateQuestions(user, test);
        data.remove("testUniqueTitle");
        data.put("solvingType", update.getCallbackQuery().getData());
        putStateToContext(update.getCallbackQuery().getFrom().getId(), nextState, data);
        showQuestionStrategy.sendQuestion(solving, data, update);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.test.check;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.check.WorkCheckState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.Constants.COLON;
import static ru.mephi.knowledgechecker.common.Constants.FOR_CHECK_PREFIX;

@Component
public class StartCheckWorkStrategy extends AbstractCallbackQueryStrategy {
    private final SolvingService solvingService;
    private final UserService userService;
    private final MarkOpenAnswerStrategy markOpenAnswerStrategy;

    public StartCheckWorkStrategy(SolvingService solvingService,
                                  UserService userService,
                                  @Lazy WorkCheckState workCheckState,
                                  @Lazy MarkOpenAnswerStrategy markOpenAnswerStrategy) {
        this.solvingService = solvingService;
        this.userService = userService;
        this.nextState = workCheckState;
        this.markOpenAnswerStrategy = markOpenAnswerStrategy;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }

        String studentPrefix = update.getCallbackQuery().getData().split(COLON)[0];
        return studentPrefix.equals(FOR_CHECK_PREFIX);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Long studentId = Long.parseLong(update.getCallbackQuery().getData().split(COLON)[1]);
        Solving solving = solvingService.get(studentId, data.getTest().getId(), VisibilityType.ESTIMATED);
        User student = userService.get(studentId);
        data.setStudent(student);
        data.setState(nextState);
        deleteMenu(data);
        saveToContext(data);

        markOpenAnswerStrategy.sendNextAnswer(data, solving, update);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.course.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.course.search.CourseSearchAttemptState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.FIND_COURSE;
import static ru.mephi.knowledgechecker.common.CommonMessageParams.askSearchQueryParams;

@Slf4j
@Component
public class AskForCourseSearchQueryStrategy extends AbstractCallbackQueryStrategy {
    public AskForCourseSearchQueryStrategy(@Lazy CourseSearchAttemptState courseSearchAttemptState) {
        nextState = courseSearchAttemptState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && (update.getCallbackQuery().getData().equals(FIND_COURSE.name()));
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        deleteMenu(data);
        data.setState(nextState);
        sendMessageAndSave(askSearchQueryParams(data.getUser().getId()), data);
    }
}

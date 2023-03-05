package ru.mephi.knowledgechecker.strategy.impl.course.create;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.course.create.CreatingCourseState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.CREATE_COURSE;

@Component
public class CreatingCourseStrategy extends AbstractCallbackQueryStrategy {

    public CreatingCourseStrategy(@Lazy CreatingCourseState creatingCourseState) {
        nextState = creatingCourseState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(CREATE_COURSE.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String message = "Введите название курса";
        data.setState(nextState);
        deleteMenu(data);
        sendMessageAndSave(message, data);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.course.participate.students;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_STUDENTS;

@Component
public class ShowStudentsListStrategy extends AbstractCallbackQueryStrategy {
    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(TO_STUDENTS.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {

    }
}

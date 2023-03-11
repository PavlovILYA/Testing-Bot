package ru.mephi.knowledgechecker.strategy.impl.course.participate.output;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseParticipationService;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.CANCEL_QUERY;

@Component
public class CancelOutputCourseStrategy extends AbstractCallbackQueryStrategy {
    private final ShowOutputCourseQueriesStrategy showOutputCourseQueriesStrategy;
    private final CourseParticipationService participationService;

    public CancelOutputCourseStrategy(@Lazy ShowOutputCourseQueriesStrategy showOutputCourseQueriesStrategy,
                                      CourseParticipationService participationService) {
        this.showOutputCourseQueriesStrategy = showOutputCourseQueriesStrategy;
        this.participationService = participationService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(CANCEL_QUERY.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        participationService.remove(data.getUser().getId(), data.getCourse().getId());
        String message = "Заявка на курс '" + data.getCourse().getTitle() + "' отменена";
        deleteMenu(data);
        sendBoldMessage(message, data.getUser().getId());
        data.setCourse(null);
        showOutputCourseQueriesStrategy.process(data, update);
    }
}

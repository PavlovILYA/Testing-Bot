package ru.mephi.knowledgechecker.strategy.impl.course.participate.input;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseParticipationService;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.*;

@Component
public class AnswerToInputQueryStrategy extends AbstractCallbackQueryStrategy {
    private final ShowInputCourseQueriesStrategy showInputCourseQueriesStrategy;
    private final CourseParticipationService participationService;

    public AnswerToInputQueryStrategy(@Lazy ShowInputCourseQueriesStrategy showInputCourseQueriesStrategy,
                                      CourseParticipationService participationService) {
        this.showInputCourseQueriesStrategy = showInputCourseQueriesStrategy;
        this.participationService = participationService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }

        String callbackData = update.getCallbackQuery().getData();
        return callbackData.equals(ACCEPT_INPUT_QUERY.name()) || callbackData.equals(REJECT_INPUT_QUERY.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String callbackData = update.getCallbackQuery().getData();
        String message;
        if (callbackData.equals(ACCEPT_INPUT_QUERY.name())) {
            participationService.acceptParticipation(data.getStudent().getId(), data.getCourse().getId());
            message = "Заявка на курс от '" + data.getStudent().getUsername() + "' принята";
        } else {
            participationService.remove(data.getStudent().getId(), data.getCourse().getId());
            message = "Заявка на курс от '" + data.getStudent().getUsername() + "' отклонена";
        }
        deleteMenu(data);
        sendBoldMessage(message, data.getUser().getId());
        data.setStudent(null);
        showInputCourseQueriesStrategy.process(data, update);
    }
}

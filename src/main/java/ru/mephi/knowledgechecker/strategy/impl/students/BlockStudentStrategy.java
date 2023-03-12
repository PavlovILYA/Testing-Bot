package ru.mephi.knowledgechecker.strategy.impl.students;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseParticipationService;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.BLOCK_STUDENT;

@Component
public class BlockStudentStrategy extends AbstractCallbackQueryStrategy {
    private final ShowStudentsListStrategy showStudentsListStrategy;
    private final CourseParticipationService participationService;

    public BlockStudentStrategy(@Lazy ShowStudentsListStrategy showStudentsListStrategy,
                                CourseParticipationService participationService) {
        this.showStudentsListStrategy = showStudentsListStrategy;
        this.participationService = participationService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }

        String callbackData = update.getCallbackQuery().getData();
        return callbackData.equals(BLOCK_STUDENT.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        participationService.remove(data.getStudent().getId(), data.getCourse().getId());
        String message = "Пользователь '" + data.getStudent().getUsername() + "' был отчислен с курса";
        deleteMenu(data);
        sendBoldMessage(message, data.getUser().getId()); // todo написать пользователю новость!
        data.setStudent(null);
        showStudentsListStrategy.process(data, update);
    }
}

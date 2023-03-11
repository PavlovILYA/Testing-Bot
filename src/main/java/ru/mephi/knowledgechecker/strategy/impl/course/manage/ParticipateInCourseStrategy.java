package ru.mephi.knowledgechecker.strategy.impl.course.manage;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseParticipationService;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToCoursesListStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.PARTICIPATE_IN_COURSE;

@Component
public class ParticipateInCourseStrategy extends AbstractCallbackQueryStrategy {
    private final CourseParticipationService participationService;
    private final ToCoursesListStrategy toCoursesListStrategy;

    public ParticipateInCourseStrategy(@Lazy ToCoursesListStrategy toCoursesListStrategy,
                                       CourseParticipationService participationService) {
        this.toCoursesListStrategy = toCoursesListStrategy;
        this.participationService = participationService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getCallbackQuery().getData().equals(PARTICIPATE_IN_COURSE.name());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Course course = data.getCourse();
        participationService.save(data.getUser().getId(), course.getId());
        String message = "Заявка на курс '" + course.getTitle() +
                "' отправлена\nОжидайте, когда администратор ее одобрит";
        SendMessageParams params = SendMessageParams.builder()
                .chatId(data.getUser().getId())
                .text(message)
                .entities(List.of(new MessageEntity(TextType.BOLD, 0, message.length())))
                .build();
        deleteMenu(data);
        telegramApiClient.sendMessage(params);
        toCoursesListStrategy.process(data, update);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseService;
import ru.mephi.knowledgechecker.state.impl.menu.AdminMenuState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractActionStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_ADMIN_MENU;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getOwnCoursesInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.ADMIN_MENU;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class ToAdminMenuStrategy extends AbstractActionStrategy {
    private final CourseService courseService;

    public ToAdminMenuStrategy(@Lazy AdminMenuState nextState,
                               CourseService courseService) {
        this.nextState = nextState;
        this.courseService = courseService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return update.getCallbackQuery() != null
                && update.getCallbackQuery().getData().equals(TO_ADMIN_MENU.name())
                ||
                update.getMessage() != null && data.getState().equals("creatingCourseState"); // todo
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        if (update.getMessage() != null) {
            Course course = Course.builder()
                    .title(update.getMessage().getText())
                    .creator(data.getUser())
                    .build();
            courseService.save(course);
        }

        Page<Course> courses = courseService.getCoursesByCreatorId(data.getUser().getId());
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), ADMIN_MENU.getTitle(),
                List.of(new MessageEntity(TextType.BOLD, 0, ADMIN_MENU.getTitle().length())),
                getOwnCoursesInlineKeyboardMarkup(courses));
        data.setState(nextState);
        sendMenuAndSave(params, data);
    }
}

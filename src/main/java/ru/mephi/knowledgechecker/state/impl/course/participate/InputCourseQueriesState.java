package ru.mephi.knowledgechecker.state.impl.course.participate;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.course.manage.ManageCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.input.ManageStudentStrategy;

@Component
public class InputCourseQueriesState extends AbstractBotState {
    public InputCourseQueriesState(ManageCourseStrategy manageCourseStrategy,
                                   ManageStudentStrategy manageStudentStrategy) {
        availableStrategies.add(manageCourseStrategy);
        availableStrategies.add(manageStudentStrategy);
    }
}

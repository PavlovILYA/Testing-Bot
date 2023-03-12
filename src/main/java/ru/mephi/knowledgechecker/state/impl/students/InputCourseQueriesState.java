package ru.mephi.knowledgechecker.state.impl.students;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.manage.ManageCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.students.ManageStudentStrategy;

@Component
public class InputCourseQueriesState extends AbstractBotState {
    public InputCourseQueriesState(ManageCourseStrategy manageCourseStrategy,
                                   ManageStudentStrategy manageStudentStrategy,
                                   TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(manageCourseStrategy);
        availableStrategies.add(manageStudentStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}

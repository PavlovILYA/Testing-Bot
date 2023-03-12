package ru.mephi.knowledgechecker.state.impl.course.manage;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.CancelOutputCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.ParticipateInCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.ShowOutputCourseQueriesStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToCoursesListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.students.ShowInputCourseQueriesStrategy;
import ru.mephi.knowledgechecker.strategy.impl.students.ShowStudentsListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestStrategy;

@Component
public class ManageCourseState extends AbstractBotState {
    public ManageCourseState(ToAdminMenuStrategy toAdminMenuStrategy,
                             ToTestListStrategy toTestListStrategy,
                             ToCoursesListStrategy toCoursesListStrategy,
                             ParticipateInCourseStrategy participateInCourseStrategy,
                             ShowOutputCourseQueriesStrategy showOutputCourseQueriesStrategy,
                             CancelOutputCourseStrategy cancelOutputCourseStrategy,
                             ShowInputCourseQueriesStrategy showInputCourseQueriesStrategy,
                             ShowStudentsListStrategy showStudentsListStrategy,
                             ManageTestStrategy manageTestStrategy,
                             TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(toAdminMenuStrategy);
        availableStrategies.add(toTestListStrategy);
        availableStrategies.add(toCoursesListStrategy);
        availableStrategies.add(participateInCourseStrategy);
        availableStrategies.add(showOutputCourseQueriesStrategy);
        availableStrategies.add(cancelOutputCourseStrategy);
        availableStrategies.add(showInputCourseQueriesStrategy);
        availableStrategies.add(showStudentsListStrategy);
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}

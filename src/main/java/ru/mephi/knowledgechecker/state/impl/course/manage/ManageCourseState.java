package ru.mephi.knowledgechecker.state.impl.course.manage;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.ParticipateInCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.input.ShowInputCourseQueriesStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.output.CancelOutputCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.output.ShowOutputCourseQueriesStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToCoursesListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestListStrategy;

@Component
public class ManageCourseState extends AbstractBotState {
    public ManageCourseState(ToAdminMenuStrategy toAdminMenuStrategy,
                             ToTestListStrategy toTestListStrategy,
                             ToCoursesListStrategy toCoursesListStrategy,
                             ParticipateInCourseStrategy participateInCourseStrategy,
                             ShowOutputCourseQueriesStrategy showOutputCourseQueriesStrategy,
                             CancelOutputCourseStrategy cancelOutputCourseStrategy,
                             ShowInputCourseQueriesStrategy showInputCourseQueriesStrategy) {
        availableStrategies.add(toAdminMenuStrategy);
        availableStrategies.add(toTestListStrategy);
        availableStrategies.add(toCoursesListStrategy);
        availableStrategies.add(participateInCourseStrategy);
        availableStrategies.add(showOutputCourseQueriesStrategy);
        availableStrategies.add(cancelOutputCourseStrategy);
        availableStrategies.add(showInputCourseQueriesStrategy);
    }
}

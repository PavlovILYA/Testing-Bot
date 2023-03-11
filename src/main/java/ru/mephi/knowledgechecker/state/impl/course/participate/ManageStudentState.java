package ru.mephi.knowledgechecker.state.impl.course.participate;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.input.AnswerToInputQueryStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.input.ShowInputCourseQueriesStrategy;

@Component
public class ManageStudentState extends AbstractBotState {
    public ManageStudentState(ShowInputCourseQueriesStrategy showInputCourseQueriesStrategy,
                              AnswerToInputQueryStrategy answerToInputQueryStrategy) {
        availableStrategies.add(showInputCourseQueriesStrategy);
        availableStrategies.add(answerToInputQueryStrategy);
    }
}

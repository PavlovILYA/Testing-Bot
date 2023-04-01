package ru.mephi.knowledgechecker.state.impl.students;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.students.AnswerToInputQueryStrategy;
import ru.mephi.knowledgechecker.strategy.impl.students.BlockStudentStrategy;
import ru.mephi.knowledgechecker.strategy.impl.students.ShowInputCourseQueriesStrategy;

@Component
public class ManageStudentState extends AbstractBotState {
    public ManageStudentState(ShowInputCourseQueriesStrategy showInputCourseQueriesStrategy,
                              AnswerToInputQueryStrategy answerToInputQueryStrategy,
                              BlockStudentStrategy blockStudentStrategy) {
        availableStrategies.add(showInputCourseQueriesStrategy);
        availableStrategies.add(answerToInputQueryStrategy);
        availableStrategies.add(blockStudentStrategy);
    }
}

package ru.mephi.knowledgechecker.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.QuestionType;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.test.Test;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "current_data")
@Builder(toBuilder = true)
public class CurrentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String state;
    @Enumerated(EnumType.STRING)
    private CreationPhaseType nextPhase;
    @OneToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;
    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
    private boolean needCheck;
    @OneToOne
    @JoinColumn(name = "open_question_id", referencedColumnName = "id")
    private OpenQuestion openQuestion;
    @OneToOne
    @JoinColumn(name = "variable_question_id", referencedColumnName = "id")
    private VariableQuestion variableQuestion;
    @Enumerated(EnumType.STRING)
    private QuestionType previousQuestionType;
    private Long lastMessageId;
    private Long menuMessageId;
    private Long clearReplyMessageId;
    private String searchKeyWords;
    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private User student;

    public void setState(Object state) {
        this.state = mapStateToBeanName(state.getClass());
    }

    private String mapStateToBeanName(Class stateClass) {
        String className = stateClass.getSimpleName().split("\\$")[0];
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}

package ru.mephi.knowledgechecker.model.user;

import lombok.*;
import ru.mephi.knowledgechecker.common.CreationPhaseType;
import ru.mephi.knowledgechecker.common.QuestionType;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Enumerated(EnumType.STRING)
    private CreationPhaseType nextPhase;
    @OneToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;
    private boolean needCheck;
    @OneToOne
    @JoinColumn(name = "open_question_id", referencedColumnName = "id")
    private OpenQuestion openQuestion;
    @OneToOne
    @JoinColumn(name = "variable_question_id", referencedColumnName = "id")
    private VariableQuestion variableQuestion;
    @Enumerated(EnumType.STRING)
    private QuestionType previousQuestionType;
}

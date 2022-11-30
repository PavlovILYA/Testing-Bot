package ru.mephi.knowledgechecker.model.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.knowledgechecker.model.answer.variable.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.variable.VariableQuestion;
import ru.mephi.knowledgechecker.model.user.User;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tests")
@Builder(toBuilder = true)
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String uniqueTitle;
    @ManyToOne // cascade
    @JoinColumn(table = "tests", name = "creator_id")
    private User creator;
    private String title;
    private Long fileId;
    private Integer maxQuestionsNumber;
    @Enumerated(value = EnumType.STRING)
    private TestType testType;
//    @OneToMany(targetEntity = VariableAnswer.class, // cascade,
//              fetch = FetchType.LAZY, orphanRemoval = true) <- many-to-many
//    List<VariableQuestion> variableQuestions;
}

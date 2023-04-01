package ru.mephi.knowledgechecker.model.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.test.Test;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "variable_questions")
public class VariableQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne // todo: fetch, cascade
    @JoinColumn(table = "variable_questions", name = "test_id")
    private Test test;
    private String text;
    private Long imageId; // from tg
    private Long audioId; // from tg
    private Long fileId;  // from tg
    private Integer maxAnswerNumber;
    @ManyToOne // todo: fetch, cascade
    @JoinColumn(table = "variable_questions", name = "correct_answer_id")
    private VariableAnswer correctAnswer;

    @ManyToMany // todo: fetch, cascade
    @JoinTable(
            name = "variable_questions_answers",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private List<VariableAnswer> wrongAnswers;
}

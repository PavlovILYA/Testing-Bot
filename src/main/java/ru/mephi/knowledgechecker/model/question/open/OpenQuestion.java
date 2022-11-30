package ru.mephi.knowledgechecker.model.question.open;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.knowledgechecker.model.answer.open.OpenAnswer;
import ru.mephi.knowledgechecker.model.test.Test;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "open_questions")
public class OpenQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne // todo: fetch, cascade
    @JoinColumn(table = "open_questions", name = "test_id")
    private Test test;
    private String text;
    private Long imageId; // from tg
    private Long audioId; // from tg
    private Long fileId;  // from tg
    private String correctAnswer;

    @OneToMany(mappedBy = "question") // todo: fetch, cascade
    Set<OpenAnswer> ratings;
}

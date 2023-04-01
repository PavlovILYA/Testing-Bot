package ru.mephi.knowledgechecker.model.question;

import lombok.*;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;
import ru.mephi.knowledgechecker.model.test.Test;

import javax.persistence.*;
import java.util.List;

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

    @ToString.Exclude
    @OneToMany(mappedBy = "question") // todo: fetch, cascade
    List<OpenAnswer> ratings;
}

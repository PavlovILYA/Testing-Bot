package ru.mephi.knowledgechecker.model.answer;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OpenAnswerKey implements Serializable {
    @Column(table = "open_answers", name = "question_id")
    private Long questionId;
    @Column(table = "open_answers", name = "user_id")
    private Long userId;
    @Column(table = "open_answers", name = "solving_id")
    private Long solvingId;
}

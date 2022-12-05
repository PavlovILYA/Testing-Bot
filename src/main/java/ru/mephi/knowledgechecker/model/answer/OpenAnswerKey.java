package ru.mephi.knowledgechecker.model.answer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@EqualsAndHashCode
public class OpenAnswerKey implements Serializable {
    @Column(table = "open_answers", name = "question_id")
    private Long questionId;
    @Column(table = "open_answers", name = "user_id")
    private Long userId;
}

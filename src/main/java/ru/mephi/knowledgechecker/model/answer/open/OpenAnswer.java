package ru.mephi.knowledgechecker.model.answer.open;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.knowledgechecker.model.question.open.OpenQuestion;
import ru.mephi.knowledgechecker.model.user.User;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "open_answers")
public class OpenAnswer {
    @EmbeddedId
    private OpenAnswerKey id;
    @ManyToOne  // todo: fetch, cascade
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private OpenQuestion question;
    @ManyToOne  // todo: fetch, cascade
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    private String text;
    private Long imageId; // from tg
    private Long audioId; // from tg
    private Long fileId;  // from tg
}

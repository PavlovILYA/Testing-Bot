package ru.mephi.knowledgechecker.model.question.variable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import ru.mephi.knowledgechecker.model.user.User;

import javax.persistence.*;

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
//    @ManyToOne // cascade
//    @JoinColumn(table = "variable_questions", name = "creator_id")
//    private Long testId;
    // todo
//    text VARCHAR(5000),
//    image_id BIGINT, -- from tg
//    audio_id BIGINT, -- from tg
//    file_id BIGINT,  -- from tg
//    max_answer_number INT,
//    correct_answer_id BIGINT,
//    PRIMARY KEY (id),
//    FOREIGN KEY (test_id) REFERENCES tests(id),
//    FOREIGN KEY (correct_answer_id) REFERENCES variable_answers(id)
}

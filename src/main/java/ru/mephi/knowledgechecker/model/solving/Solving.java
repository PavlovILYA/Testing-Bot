package ru.mephi.knowledgechecker.model.solving;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solving")
@Builder(toBuilder = true)
public class Solving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(table = "solving", name = "user_id", unique = true)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(table = "solving", name = "test_id")
    private Test test;
    private String openQuestionIds;
    private String openAnswerIds;
    private String variableQuestionIds;
    private String variableAnswerIds;
    private LocalDateTime startedAt;
}

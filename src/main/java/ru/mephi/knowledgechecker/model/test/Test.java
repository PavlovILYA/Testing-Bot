package ru.mephi.knowledgechecker.model.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.knowledgechecker.model.question.variable.VariableQuestion;
import ru.mephi.knowledgechecker.model.user.User;

import javax.persistence.*;
import java.util.Set;

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
    @ManyToOne  // todo: fetch, cascade
    @JoinColumn(table = "tests", name = "creator_id")
    private User creator;
    private String title;
    private Long fileId;
    private Integer maxQuestionsNumber;
    @Enumerated(value = EnumType.STRING)
    private TestType testType;

    @OneToMany(mappedBy = "test", // todo: fetch, cascade
              fetch = FetchType.LAZY, orphanRemoval = true)
    Set<VariableQuestion> variableQuestions;

    @ManyToMany(mappedBy = "addedTests") // а надо оно здесь вообще?
    Set<User> usingUsers;
}

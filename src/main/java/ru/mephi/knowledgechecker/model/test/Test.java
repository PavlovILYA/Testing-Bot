package ru.mephi.knowledgechecker.model.test;

import lombok.*;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)  // todo: fetch, cascade
    @JoinColumn(table = "tests", name = "creator_id")
    private User creator;
    private String title;
    private Long fileId;
    private Integer maxQuestionsNumber;
    @Enumerated(value = EnumType.STRING)
    private TestType testType;
    @Enumerated(value = EnumType.STRING)
    private VisibilityType visibility;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "test", // todo: fetch, cascade
              fetch = FetchType.LAZY, orphanRemoval = true)
    private List<VariableQuestion> variableQuestions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "test", // todo: fetch, cascade
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OpenQuestion> openQuestions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "addedTests") // а надо оно здесь вообще?
    private List<User> usingUsers;

    @ManyToOne
    @JoinColumn(table = "tests", name = "course_id")
    private Course course;
}

package ru.mephi.knowledgechecker.model.course.participation;

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
public class ParticipationKey implements Serializable {
    @Column(table = "course_participation", name = "user_id")
    private Long userId;
    @Column(table = "course_participation", name = "course_id")
    private Long courseId;
}

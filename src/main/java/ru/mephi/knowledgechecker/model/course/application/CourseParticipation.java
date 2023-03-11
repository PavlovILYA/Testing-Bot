package ru.mephi.knowledgechecker.model.course.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "course_participation")
public class CourseParticipation {
    @EmbeddedId
    private ParticipationKey id;
    private boolean approved;
}

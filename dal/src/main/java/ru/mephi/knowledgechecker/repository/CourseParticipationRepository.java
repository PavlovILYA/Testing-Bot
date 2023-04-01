package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.course.participation.CourseParticipation;
import ru.mephi.knowledgechecker.model.course.participation.ParticipationKey;

public interface CourseParticipationRepository extends JpaRepository<CourseParticipation, ParticipationKey> {
}

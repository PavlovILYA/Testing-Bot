package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.course.application.CourseParticipation;
import ru.mephi.knowledgechecker.model.course.application.ParticipationKey;
import ru.mephi.knowledgechecker.repository.CourseParticipationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseParticipationService {
    private final CourseParticipationRepository courseParticipationRepository;

    public CourseParticipation save(Long userId, Long courseId) {
        ParticipationKey id = ParticipationKey.builder()
                .userId(userId)
                .courseId(courseId)
                .build();
        CourseParticipation participation = courseParticipationRepository.findById(id).orElse(null);
        if (participation == null) {
            participation = courseParticipationRepository.save(CourseParticipation.builder()
                    .id(id)
                    .build());
        }
        log.info("Saved course application: {}", participation);
        return participation;
    }
}

package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.course.participation.CourseParticipation;
import ru.mephi.knowledgechecker.model.course.participation.ParticipationKey;
import ru.mephi.knowledgechecker.repository.CourseParticipationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseParticipationService {
    private final CourseParticipationRepository participationRepository;

    public CourseParticipation save(Long userId, Long courseId) {
        ParticipationKey id = ParticipationKey.builder()
                .userId(userId)
                .courseId(courseId)
                .build();
        CourseParticipation participation = participationRepository.findById(id).orElse(null);
        if (participation == null) {
            participation = participationRepository.save(CourseParticipation.builder()
                    .id(id)
                    .build());
        }
        log.info("Saved course participation: {}", participation);
        return participation;
    }

    public void remove(Long userId, Long courseId) {
        ParticipationKey id = ParticipationKey.builder()
                .userId(userId)
                .courseId(courseId)
                .build();
        participationRepository.deleteById(id);
        log.info("Removed course participation: {}", id);
    }
}

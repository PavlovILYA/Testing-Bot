package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;
import ru.mephi.knowledgechecker.model.answer.OpenAnswerKey;
import ru.mephi.knowledgechecker.repository.OpenAnswerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAnswerService {
    private final OpenAnswerRepository openAnswerRepository;

    public OpenAnswer get(Long userId, Long questionId, Long solvingId) {
        OpenAnswerKey key = OpenAnswerKey.builder()
                .questionId(questionId)
                .userId(userId)
                .solvingId(solvingId)
                .build();
        OpenAnswer answer = openAnswerRepository.findById(key).orElse(null);
        log.info("Get answer: {}", answer);
        return answer;
    }

    public OpenAnswer save(OpenAnswer answer) {
        answer = openAnswerRepository.save(answer);
        log.info("Saved answer: {}", answer);
        return answer;
    }
}

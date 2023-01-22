package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;
import ru.mephi.knowledgechecker.model.answer.OpenAnswerKey;
import ru.mephi.knowledgechecker.repository.OpenAnswerRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAnswerService {
    private final OpenAnswerRepository openAnswerRepository;

    public OpenAnswer get(Long userId, Long questionId) {
        OpenAnswerKey key = OpenAnswerKey.builder()
                .questionId(questionId)
                .userId(userId)
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

    public void removeByUserIdAndQuestionIds(Long userId, List<Long> questionIds) {
        log.info("Delete answers of user {} for questions: {}", userId, questionIds);
        for (Long questionId : questionIds) {
            OpenAnswerKey key = OpenAnswerKey.builder()
                    .questionId(questionId)
                    .userId(userId)
                    .build();
            openAnswerRepository.deleteById(key);
        }
    }
}

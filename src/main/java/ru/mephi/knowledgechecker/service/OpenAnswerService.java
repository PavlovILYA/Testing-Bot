package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;
import ru.mephi.knowledgechecker.repository.OpenAnswerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAnswerService {
    private final OpenAnswerRepository openAnswerRepository;

    public OpenAnswer get(Long id) {
        OpenAnswer answer = openAnswerRepository.findById(id).orElse(null);
        log.info("Get answer: {}", answer);
        return answer;
    }

    public OpenAnswer save(OpenAnswer answer) {
        answer = openAnswerRepository.save(answer);
        log.info("Saved answer: {}", answer);
        return answer;
    }
}

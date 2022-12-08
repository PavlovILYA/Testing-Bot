package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.repository.OpenQuestionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenQuestionService {
    private final OpenQuestionRepository openQuestionRepository;

    public OpenQuestion get(Long id) {
        OpenQuestion question = openQuestionRepository.findById(id).orElse(null);
        log.info("Get question: {}", question);
        return question;
    }

    public OpenQuestion save(OpenQuestion question) {
        question = openQuestionRepository.save(question);
        log.info("Saved question: {}", question);
        return question;
    }
}

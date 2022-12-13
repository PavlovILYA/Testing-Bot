package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.repository.VariableQuestionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariableQuestionService {
    private final VariableQuestionRepository variableQuestionRepository;

    public VariableQuestion get(Long id) {
        VariableQuestion question = variableQuestionRepository.findById(id).orElse(null);
        log.info("Get question: {}", question);
        return question;
    }

    public VariableQuestion save(VariableQuestion question) {
        question = variableQuestionRepository.save(question);
        log.info("Saved question: {}", question);
        return question;
    }
}

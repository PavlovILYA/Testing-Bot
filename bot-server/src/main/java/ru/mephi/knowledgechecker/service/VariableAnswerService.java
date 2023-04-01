package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.repository.VariableAnswerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariableAnswerService {
    private final VariableAnswerRepository variableAnswerRepository;

    public VariableAnswer get(Long id) {
        VariableAnswer answer = variableAnswerRepository.findById(id).orElse(null);
        log.info("Get answer: {}", answer);
        return answer;
    }

    public VariableAnswer save(VariableAnswer answer) {
        VariableAnswer answerFromDb = getByText(answer.getText());
        if (answerFromDb != null) {
            return answerFromDb;
        } else {
            answer = variableAnswerRepository.save(answer);
            log.info("Saved answer: {}", answer);
            return answer;
        }
    }

    public VariableAnswer getByText(String text) {
        VariableAnswer answer = variableAnswerRepository.findByText(text).orElse(null);
        log.info("Get (by text) answer: {}", answer);
        return answer;
    }
}

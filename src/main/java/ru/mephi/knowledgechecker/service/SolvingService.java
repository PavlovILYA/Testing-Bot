package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.repository.SolvingRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolvingService {
    private final SolvingRepository solvingRepository;

    public Solving getByUserId(Long userId) {
        Solving solving = solvingRepository.findByUserId(userId);
        log.info("Get solving: {}", solving);
        return solving;
    }

    public Solving save(Solving solving) {
        solving = solvingRepository.save(solving);
        log.info("Saved solving: {}", solving);
        return solving;
    }

    public Solving generateQuestions(User user, Test test) {
        int amount = test.getMaxQuestionsNumber();
        int allOpenAmount = test.getOpenQuestions().size();
        int allVariableAmount = test.getVariableQuestions().size();
        int openAmount = amount * allOpenAmount / (allOpenAmount + allVariableAmount);
        int variableAmount = amount - openAmount;
        openAmount = Math.min(openAmount, allOpenAmount);
        variableAmount = Math.min(variableAmount, allVariableAmount);
        return null; // todo
    }
}

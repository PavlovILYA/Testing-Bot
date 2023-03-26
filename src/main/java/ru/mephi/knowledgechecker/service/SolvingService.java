package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.solving.SolvingType;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.repository.SolvingRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.mephi.knowledgechecker.common.Constants.SEMICOLON;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolvingService {
    private final SolvingRepository solvingRepository;

    public Solving get(Long userId, Long testId, VisibilityType visibility) {
        Solving solving = solvingRepository.findByUserIdAndTestIdAndVisibility(userId, testId, visibility);
        log.info("Get solving: {}", solving);
        return solving;
    }

    public Solving save(Solving solving) {
        solving = solvingRepository.save(solving);
        log.info("Saved solving: {}", solving);
        return solving;
    }

    public void remove(Solving solving) {
        log.info("Delete solving: {}", solving);
        solvingRepository.delete(solving);
    }

    public Solving generateQuestions(User user, Test test, SolvingType solvingType) {
        int amount = test.getMaxQuestionsNumber();
        int allOpenAmount = test.getOpenQuestions().size();
        int allVariableAmount = test.getVariableQuestions().size();
        int openAmount = amount * allOpenAmount / (allOpenAmount + allVariableAmount);
        int variableAmount = amount - openAmount;
        openAmount = Math.min(openAmount, allOpenAmount);
        variableAmount = Math.min(variableAmount, allVariableAmount);

        String openQuestionIds = getRandomQuestionIds(test.getOpenQuestions().stream()
                .map(OpenQuestion::getId), openAmount, allOpenAmount);
        String variableQuestionIds = getRandomQuestionIds(test.getVariableQuestions().stream()
                .map(VariableQuestion::getId), variableAmount, allVariableAmount);

        Solving solving = solvingRepository.findByUserIdAndTestIdAndVisibility(user.getId(), test.getId(), test.getVisibility());

        if (solving != null) {
            solvingRepository.delete(solving);
        }
        solving = Solving.builder()
                .user(user)
                .test(test)
                .openQuestionIds(openQuestionIds)
                .openAnswerIds("")
                .variableQuestionIds(variableQuestionIds)
                .variableAnswerIds("")
                .variableAnswerResults("")
                .startedAt(LocalDateTime.now())
                .type(solvingType)
                .visibility(test.getVisibility())
                .build();

        solving = solvingRepository.save(solving);
        log.info("Generated test: {}", solving);
        return solving;
    }

    private String getRandomQuestionIds(Stream<Long> idStream, int amount, int allAmount) {
        List<String> ids = idStream
                .map(Object::toString)
                .collect(Collectors.toList());
        Collections.shuffle(ids, new Random());
        if (allAmount - amount <= 0) {
            return String.join(SEMICOLON, ids);
        }
        for (int i = 0; i < allAmount - amount; i++) {
            ids.remove(ids.size() - 1);
        }
        return String.join(SEMICOLON, ids);
    }
}

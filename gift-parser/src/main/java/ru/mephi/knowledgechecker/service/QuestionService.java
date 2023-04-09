package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.exception.QuestionDataException;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.repository.OpenQuestionRepository;
import ru.mephi.knowledgechecker.repository.VariableAnswerRepository;
import ru.mephi.knowledgechecker.repository.VariableQuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final OpenQuestionRepository openQuestionRepository;
    private final VariableQuestionRepository variableQuestionRepository;
    private final VariableAnswerRepository variableAnswerRepository;

    public void saveOpenQuestion(Test test, String text, String correctAnswer) {
        OpenQuestion question = openQuestionRepository.save(OpenQuestion.builder()
                .test(test)
                .text(text)
                .correctAnswer(correctAnswer)
                .build());
        log.info("(TEST:{}) OPEN QUESTION:\n{}", question.getTest().getId(), question.getText());
        log.info("ANSWER:\n{}", question.getCorrectAnswer());
    }

    public void saveVariableQuestion(Test test, String text, String correctStrAnswer, List<String> incorrectStrAnswers) {
        validateAnswers(text, correctStrAnswer, incorrectStrAnswers);

        VariableAnswer correctAnswer = safelySaveVariableAnswer(correctStrAnswer);

        List<VariableAnswer> incorrectAnswers = incorrectStrAnswers.stream()
                .map(this::safelySaveVariableAnswer)
                .collect(Collectors.toList());

        VariableQuestion question = variableQuestionRepository.save(VariableQuestion.builder()
                .test(test)
                .text(text)
                .maxAnswerNumber(incorrectStrAnswers.size() + 1)
                .correctAnswer(correctAnswer)
                .wrongAnswers(incorrectAnswers)
                .build());

        log.info("(TEST:{}) VARIABLE QUESTION:\n{}", question.getTest().getId(), question.getText());
        log.info("CORRECT ANSWER:\n{}", question.getCorrectAnswer().getText());
        log.info("INCORRECT ANSWERS:");
        question.getWrongAnswers().forEach(a -> log.info(a.getText()));
    }

    private void validateAnswers(String text, String correctAnswer, List<String> incorrectAnswers) {
        if (incorrectAnswers.contains(correctAnswer)) {
            throw new QuestionDataException("Вопрос: " + text +
                    "\nПравильный ответ совпадает с неправильным");
        }
        checkLength(correctAnswer);
        incorrectAnswers.forEach(this::checkLength);
        if (incorrectAnswers.size() != incorrectAnswers.stream().distinct().count()) {
            throw new QuestionDataException("Вопрос: " + text +
                    "\nЕсть дублирующиеся неправильные варианты ответа");
        }
    }

    private void checkLength(String variableAnswer) {
        if (variableAnswer.length() > 30) {
            throw new QuestionDataException("Вариативный ответ: " + variableAnswer +
                    "\nМаксимальная длина вариативного ответа 30 символов, попробуйте еще раз");
        }
    }

    private VariableAnswer safelySaveVariableAnswer(String strAnswer) {
        VariableAnswer answerFromDb = getByText(strAnswer);
        if (answerFromDb != null) {
            return answerFromDb;
        } else {
            VariableAnswer answer = variableAnswerRepository.save(VariableAnswer.builder()
                    .text(strAnswer)
                    .build());
            log.info("Saved answer: {}", answer);
            return answer;
        }
    }

    private VariableAnswer getByText(String text) {
        VariableAnswer answer = variableAnswerRepository.findByText(text).orElse(null);
        log.info("Get (by text) answer: {}", answer);
        return answer;
    }
}

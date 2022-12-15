package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.service.VariableAnswerService;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getVariableAnswerKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ShowQuestionStrategy extends AbstractMessageStrategy {
    private final SolvingService solvingService;
    private final VariableAnswerService variableAnswerService;

    public ShowQuestionStrategy(SolvingService solvingService,
                                VariableAnswerService variableAnswerService) {
        this.solvingService = solvingService;
        this.variableAnswerService = variableAnswerService;
    }

    @Override
    public boolean apply(Update update) {
        if (!super.apply(update)) {
            return false;
        }
        return true;
        // todo: используя previous
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        Solving solving = solvingService.getByUserId(update.getMessage().getId());
        // todo: add answer
        sendQuestion(solving, data);
    }

    public void sendQuestion(Solving solving, Map<String, Object> data) {
        List<Long> variableQuestionIds = parseIds(solving.getVariableQuestionIds());
        List<Long> variableAnswerIds = parseIds(solving.getVariableAnswerIds());
        List<Long> openQuestionIds = parseIds(solving.getOpenQuestionIds());
        List<Long> openAnswerIds = parseIds(solving.getOpenAnswerIds());

        if (variableQuestionIds.size() == variableAnswerIds.size()) {
            if (openQuestionIds.size() == openAnswerIds.size()) {
                data.remove("previousQuestionType");
                putStateToContext(solving.getUser().getId(), data);
                generateReport();
            } else {
                data.put("previousQuestionType", "open");
                putStateToContext(solving.getUser().getId(), data);
                sendOpenQuestion(solving, openQuestionIds, openAnswerIds);
            }
        } else {
            data.put("previousQuestionType", "variable");
            putStateToContext(solving.getUser().getId(), data);
            sendVariableQuestion(solving, variableQuestionIds, variableAnswerIds);
        }
    }

    private void sendVariableQuestion(Solving solving, List<Long> variableQuestionIds, List<Long> variableAnswerIds) {
        Long questionId = variableQuestionIds.get(variableAnswerIds.size());
        VariableQuestion question = solving.getTest()
                .getVariableQuestions().stream()
                .filter(q -> Objects.equals(q.getId(), questionId))
                .findFirst().orElse(null);
        assert question != null;
        MessageParams params =
                wrapMessageParams(solving.getUser().getId(),
                        question.getText(),
                        getVariableAnswerKeyboardMarkup(question));
        telegramApiClient.sendMessage(params);
    }

    private void sendOpenQuestion(Solving solving, List<Long> openQuestionIds, List<Long> openAnswerIds) {
        Long questionId = openQuestionIds.get(openAnswerIds.size());
        OpenQuestion question = solving.getTest()
                .getOpenQuestions().stream()
                .filter(q -> Objects.equals(q.getId(), questionId))
                .findFirst().orElse(null);
        assert question != null;
        MessageParams params =
                wrapMessageParams(solving.getUser().getId(),
                        question.getText(), null);
        telegramApiClient.sendMessage(params);
    }

    private void generateReport() {
        // todo
    }

    private List<Long> parseIds(String string) {
        return Arrays.stream(string.split(";"))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}

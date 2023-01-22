package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;
import ru.mephi.knowledgechecker.model.answer.OpenAnswerKey;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.service.OpenAnswerService;
import ru.mephi.knowledgechecker.service.OpenQuestionService;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.service.VariableAnswerService;
import ru.mephi.knowledgechecker.state.impl.menu.MainMenuState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;

import java.util.*;
import java.util.stream.Collectors;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getVariableAnswerKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class ShowQuestionStrategy extends AbstractMessageStrategy {
    private final SolvingService solvingService;
    private final VariableAnswerService variableAnswerService;
    private final OpenAnswerService openAnswerService;
    private final OpenQuestionService openQuestionService;
    private final ToMainMenuStrategy toMainMenuStrategy;

    public ShowQuestionStrategy(SolvingService solvingService,
                                VariableAnswerService variableAnswerService,
                                OpenAnswerService openAnswerService,
                                OpenQuestionService openQuestionService,
                                MainMenuState mainMenuState, ToMainMenuStrategy toMainMenuStrategy) {
        this.solvingService = solvingService;
        this.variableAnswerService = variableAnswerService;
        this.openAnswerService = openAnswerService;
        this.openQuestionService = openQuestionService;
        this.toMainMenuStrategy = toMainMenuStrategy;
        this.nextState = mainMenuState;
    }

    @Override
    public boolean apply(Update update) {
        if (!super.apply(update)) {
            return false;
        }
        return true;
        // todo: нужно что-то сделать, используя previous?
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        Solving solving = solvingService.getByUserId(update.getMessage().getFrom().getId());
        String answerText = update.getMessage().getText();
        saveAnswer(solving, data, answerText);
        sendQuestion(solving, data, update);
    }

    private void saveAnswer(Solving solving, Map<String, Object> data, String answerText) {
        switch ((String) data.get("previousQuestionType")) {
            case "open":
                saveOpenAnswer(solving, answerText);
                break;
            case "variable":
                saveVariableAnswer(solving, answerText);
                break;
            default:
        }
    }

    private void saveVariableAnswer(Solving solving, String answerText) {
        VariableAnswer answer = variableAnswerService.getByText(answerText);
        if (answer == null) {
            sendError(solving.getUser().getId());
            return;
        }

        // todo: стоит здесь проверять ответ?
        List<Long> variableQuestionIds = parseIds(solving.getVariableQuestionIds());
        List<Long> variableAnswerIds = parseIds(solving.getVariableAnswerIds());
        Long questionId = variableQuestionIds.get(variableAnswerIds.size());
        VariableQuestion question = solving.getTest()
                .getVariableQuestions().stream()
                .filter(q -> Objects.equals(q.getId(), questionId))
                .findFirst().orElse(null);
        assert question != null;

        if (question.getCorrectAnswer().equals(answer)) {
            solving.setVariableAnswerResults(concatIt(solving.getVariableAnswerResults(), 1L));
        } else {
            solving.setVariableAnswerResults(concatIt(solving.getVariableAnswerResults(), 0L));
        }

        solving.setVariableAnswerIds(concatIt(solving.getVariableAnswerIds(), answer.getId()));
        solvingService.save(solving);
    }

    private String concatIt(String previously, Long nextOne) {
        if (previously.length() == 0) {
            return nextOne.toString();
        }
        return previously + ";" + nextOne;
    }

    private void saveOpenAnswer(Solving solving, String answerText) {
        List<Long> openQuestionIds = parseIds(solving.getOpenQuestionIds());
        List<Long> openAnswerIds = parseIds(solving.getOpenAnswerIds());
        Long questionId = openQuestionIds.get(openAnswerIds.size());
        OpenQuestion question = solving.getTest()
                .getOpenQuestions().stream()
                .filter(q -> Objects.equals(q.getId(), questionId))
                .findFirst().orElse(null);
        assert question != null;

        OpenAnswer answer = OpenAnswer.builder()
                .id(OpenAnswerKey.builder()
                        .questionId(question.getId())
                        .userId(solving.getUser().getId())
                        .build())
                .user(solving.getUser())
                .question(question)
                .text(answerText)
                .build();
        log.info("CHECK before saving answer: {}", answer);
        answer = openAnswerService.save(answer);
        solving.setOpenAnswerIds(concatIt(solving.getOpenAnswerIds(), answer.getId().getQuestionId()));
        solvingService.save(solving);
    }

    private void sendError(long userId) {
        String message = "Неверный формат ответа 🥴";
        MessageParams params =
                wrapMessageParams(userId, message,
                        List.of(new MessageEntity("bold", 0, message.length())),
                        null);
        telegramApiClient.sendMessage(params);
        // todo: обновить keyboard?
    }

    public void sendQuestion(Solving solving, Map<String, Object> data, Update update) {
        List<Long> variableQuestionIds = parseIds(solving.getVariableQuestionIds());
        List<Long> variableAnswerIds = parseIds(solving.getVariableAnswerIds());
        List<Long> openQuestionIds = parseIds(solving.getOpenQuestionIds());
        List<Long> openAnswerIds = parseIds(solving.getOpenAnswerIds());

        if (variableQuestionIds.size() == variableAnswerIds.size()) {
            if (openQuestionIds.size() == openAnswerIds.size()) {
                data.remove("previousQuestionType");
                putStateToContext(solving.getUser().getId(), nextState, data);
                generateReport(solving, data, update);
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
        log.debug("CHECK variableQuestionIds: {}", variableQuestionIds);
        log.debug("CHECK variableAnswerIds: {}", variableAnswerIds);
        Long questionId = variableQuestionIds.get(variableAnswerIds.size());
        VariableQuestion question = solving.getTest()
                .getVariableQuestions().stream()
                .filter(q -> Objects.equals(q.getId(), questionId))
                .findFirst().orElse(null);
        assert question != null;
        String message = "Вопрос № " + (variableAnswerIds.size() + 1) + "\n\n";
        MessageParams params =
                wrapMessageParams(solving.getUser().getId(), message + question.getText(),
                        List.of(new MessageEntity("bold", 0, message.length()),
                                new MessageEntity("code", message.length(), question.getText().length())),
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
        String message = "Открытый вопрос № " + (openAnswerIds.size() + 1) + "\n\n";
        MessageParams params =
                wrapMessageParams(solving.getUser().getId(), message + question.getText(),
                        List.of(new MessageEntity("bold", 0, message.length()),
                                new MessageEntity("code", message.length(), question.getText().length())),
                        null);
        telegramApiClient.sendMessage(params);
    }

    private void generateReport(Solving solving, Map<String, Object> data, Update update) {
        sendVariableResults(solving);
        sendOpenResults(solving);
        clearSolving(solving);
        toMainMenuStrategy.process(update, data);
    }

    private void sendVariableResults(Solving solving) {
        if (solving.getVariableAnswerResults().isEmpty()) {
            return;
        }
        int allCount = parseIds(solving.getVariableAnswerResults()).size();
        Long correctCount = parseIds(solving.getVariableAnswerResults()).stream().reduce(0L, Long::sum);
        String boldMessage = "🏁 Тест завершен 🏁\n\n";
        String codeMessage = "Количество правильных ответов на вопросы с вариантами ответов:\n" +
                "✅ " + correctCount + "/" + allCount + " [" + ((double) correctCount / allCount * 100) + "%]";
        MessageParams params =
                wrapMessageParams(solving.getUser().getId(), boldMessage + codeMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("code", boldMessage.length(), codeMessage.length()),
                                new MessageEntity("spoiler", boldMessage.length(), codeMessage.length())),
                        null);
        telegramApiClient.sendMessage(params);
    }

    private void sendOpenResults(Solving solving) {
        if (solving.getOpenQuestionIds().isEmpty()) {
            return;
        }
        List<Long> questionIds = parseIds(solving.getOpenQuestionIds());
        List<Long> answersIds = parseIds(solving.getOpenAnswerIds());
        for (int i = 0; i < questionIds.size(); i++) {
            OpenQuestion question = openQuestionService.get(questionIds.get(i));
            OpenAnswer answer = openAnswerService.get(solving.getUser().getId(), answersIds.get(i));
            String boldMessage1 = "❓ Вопрос:\n";
            String boldMessage2 = "\n☑️ Ваш ответ:\n";
            String boldMessage3 = "\n️✅ Правильный ответ:\n";
            String message = boldMessage1 + question.getText() + boldMessage2 + answer.getText()
                    + boldMessage3 + question.getCorrectAnswer();
            MessageParams params =
                    wrapMessageParams(solving.getUser().getId(), message,
                            List.of(new MessageEntity("bold", 0, boldMessage1.length()),
                                    new MessageEntity("code", boldMessage1.length(), question.getText().length()),
                                    new MessageEntity("bold",
                                            boldMessage1.length() + question.getText().length(),
                                            boldMessage2.length()),
                                    new MessageEntity("code",
                                            boldMessage1.length() + question.getText().length() + boldMessage2.length(),
                                            answer.getText().length()),
                                    new MessageEntity("bold",
                                            message.length() - boldMessage3.length() - question.getCorrectAnswer().length(),
                                            boldMessage3.length()),
                                    new MessageEntity("spoiler",
                                            message.length() - question.getCorrectAnswer().length(),
                                            question.getCorrectAnswer().length())),
                            null);
            telegramApiClient.sendMessage(params);
        }
    }

    private void clearSolving(Solving solving) {
        List<Long> answersIds = parseIds(solving.getOpenAnswerIds());
        openAnswerService.removeByUserIdAndQuestionIds(solving.getUser().getId(), answersIds);
        solvingService.remove(solving);
    }

    private List<Long> parseIds(String string) {
        try {
            return Arrays.stream(string.split(";"))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }
}

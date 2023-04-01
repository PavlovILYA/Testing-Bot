package ru.mephi.knowledgechecker.strategy.impl.test.solve;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.ReplyKeyboardRemove;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;
import ru.mephi.knowledgechecker.model.answer.OpenAnswerKey;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.QuestionType;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.solving.SolvingType;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.OpenAnswerService;
import ru.mephi.knowledgechecker.service.OpenQuestionService;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.service.VariableAnswerService;
import ru.mephi.knowledgechecker.state.impl.menu.MainMenuState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.getOpenAnswerParams;
import static ru.mephi.knowledgechecker.common.IdsUtils.concatIt;
import static ru.mephi.knowledgechecker.common.IdsUtils.parseIds;
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
    public boolean apply(CurrentData data, Update update) {
        if (!super.apply(data, update)) {
            return false;
        }
        return true;
        // todo: нужно что-то сделать, используя previous?
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Solving solving = solvingService.get(
                data.getUser().getId(), data.getTest().getId(), data.getTest().getVisibility());
        String answerText = update.getMessage().getText();
        saveAnswer(solving, data, answerText);
        sendQuestion(solving, data, update);
    }

    private void saveAnswer(Solving solving, CurrentData data, String answerText) throws StrategyProcessException {
        switch (data.getPreviousQuestionType()) {
            case OPEN:
                saveOpenAnswer(solving, answerText);
                break;
            case VARIABLE:
                saveVariableAnswer(solving, answerText);
                break;
            default:
        }
    }

    private void saveVariableAnswer(Solving solving, String answerText) throws StrategyProcessException {
        VariableAnswer answer = variableAnswerService.getByText(answerText);
        if (answer == null) {
            throw new StrategyProcessException(solving.getUser().getId(), "Неверный формат ответа\n" +
                    "(Для удобства воспользуйтесь кастомной клавиатурой)");
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

        String result = "❔";
        if (question.getCorrectAnswer().equals(answer)) {
            solving.setVariableAnswerResults(concatIt(solving.getVariableAnswerResults(), 1L));
            result = "✅";
        } else {
            solving.setVariableAnswerResults(concatIt(solving.getVariableAnswerResults(), 0L));
            result = "❌";
        }
        if (solving.getType() == SolvingType.INSTANT_DEMONSTRATION_ANSWER) {
            telegramApiClient.sendMessage(wrapMessageParams(solving.getUser().getId(), result,
                    List.of(new MessageEntity(TextType.SPOILER, 0, 1)),
                    null));
        }

        solving.setVariableAnswerIds(concatIt(solving.getVariableAnswerIds(), answer.getId()));
        solvingService.save(solving);
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
                        .solvingId(solving.getId())
                        .build())
                .user(solving.getUser())
                .question(question)
                .text(answerText)
                .build();
        log.info("CHECK before saving answer: {}", answer);
        answer = openAnswerService.save(answer);
        solving.setOpenAnswerIds(concatIt(solving.getOpenAnswerIds(), answer.getId().getQuestionId()));
        solvingService.save(solving);

        if (solving.getType() == SolvingType.INSTANT_DEMONSTRATION_ANSWER) {
            telegramApiClient.sendMessage(wrapMessageParams(solving.getUser().getId(), question.getCorrectAnswer(),
                    List.of(new MessageEntity(TextType.SPOILER, 0, question.getCorrectAnswer().length())),
                    null));
        }
    }

    public void sendQuestion(Solving solving, CurrentData data, Update update) {
        List<Long> variableQuestionIds = parseIds(solving.getVariableQuestionIds());
        List<Long> variableAnswerIds = parseIds(solving.getVariableAnswerIds());
        List<Long> openQuestionIds = parseIds(solving.getOpenQuestionIds());
        List<Long> openAnswerIds = parseIds(solving.getOpenAnswerIds());
        int questionAmount = variableQuestionIds.size() + openQuestionIds.size();

        if (variableQuestionIds.size() == variableAnswerIds.size()) {
            if (openQuestionIds.size() == openAnswerIds.size()) {
                data.setPreviousQuestionType(null);
                data.setTest(null);
                data.setState(nextState);
                saveToContext(data);
                generateReport(solving, data, update);
            } else {
                data.setPreviousQuestionType(QuestionType.OPEN);
                saveToContext(data);
                sendOpenQuestion(solving, openQuestionIds, openAnswerIds, questionAmount, variableQuestionIds.size());
            }
        } else {
            data.setPreviousQuestionType(QuestionType.VARIABLE);
            saveToContext(data);
            sendVariableQuestion(solving, variableQuestionIds, variableAnswerIds, questionAmount);
        }
    }

    private void sendVariableQuestion(Solving solving, List<Long> variableQuestionIds,
                                      List<Long> variableAnswerIds, int questionAmount) {
        log.debug("CHECK variableQuestionIds: {}", variableQuestionIds);
        log.debug("CHECK variableAnswerIds: {}", variableAnswerIds);
        Long questionId = variableQuestionIds.get(variableAnswerIds.size());
        VariableQuestion question = solving.getTest()
                .getVariableQuestions().stream()
                .filter(q -> Objects.equals(q.getId(), questionId))
                .findFirst().orElse(null);
        assert question != null;
        String message = "❓ Вопрос № " + (variableAnswerIds.size() + 1) + " (из " + questionAmount + ")\n\n";
        SendMessageParams params = wrapMessageParams(solving.getUser().getId(), message + question.getText(),
                List.of(new MessageEntity(TextType.BOLD, 0, message.length()),
                        new MessageEntity(TextType.CODE, message.length(), question.getText().length())),
                getVariableAnswerKeyboardMarkup(question));
        telegramApiClient.sendMessage(params);
    }

    private void sendOpenQuestion(Solving solving, List<Long> openQuestionIds,
                                  List<Long> openAnswerIds, int questionAmount, int offset) {
        Long questionId = openQuestionIds.get(openAnswerIds.size());
        OpenQuestion question = solving.getTest()
                .getOpenQuestions().stream()
                .filter(q -> Objects.equals(q.getId(), questionId))
                .findFirst().orElse(null);
        assert question != null;
        String boldMessage = "❓ Вопрос № " + (offset + openAnswerIds.size() + 1) + " (из " + questionAmount + ")\n";
        String italicMessage = "💬 Дайте развернутый ответ\n\n";
        SendMessageParams params = wrapMessageParams(solving.getUser().getId(),
                boldMessage + italicMessage + question.getText(),
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length()),
                        new MessageEntity(TextType.CODE, boldMessage.length() + italicMessage.length(),
                                question.getText().length())),
                null);
        telegramApiClient.sendMessage(params);
    }

    private void generateReport(Solving solving, CurrentData data, Update update) {
        sendFinishCongrats(solving);
        if (solving.getType() == SolvingType.REPORT_GENERATING_AT_THE_END
                && solving.getVisibility() != VisibilityType.ESTIMATED) { // todo (отображать в любом случае для автора)
            sendVariableResults(solving);
            sendOpenResults(solving);
        }
        clearSolving(solving);
        try {
            toMainMenuStrategy.process(data, update);
        } catch (StrategyProcessException e) {
            toMainMenuStrategy.analyzeException(e);
        }
    }

    private void sendFinishCongrats(Solving solving) {
        String boldMessage = "🏁 Тест завершен 🏁\n";
        String codeMessage = "⏰ Время: " +
                Duration.between(solving.getStartedAt(), LocalDateTime.now()).toString()
                        .substring(2)
                        .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                        .toLowerCase();
        SendMessageParams params = wrapMessageParams(solving.getUser().getId(), boldMessage + codeMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.CODE, boldMessage.length(), codeMessage.length())),
                new ReplyKeyboardRemove(true));
        telegramApiClient.sendMessage(params);
    }

    private void sendVariableResults(Solving solving) {
        if (solving.getVariableAnswerResults().isEmpty()) {
            return;
        }
        int allCount = parseIds(solving.getVariableAnswerResults()).size();
        Long correctCount = parseIds(solving.getVariableAnswerResults()).stream().reduce(0L, Long::sum);
        String boldMessage = "Вопросы с готовыми ответами:\n";
        String codeMessage = "✅ " + correctCount + "/" + allCount + " [" + ((double) correctCount / allCount * 100) + "%]";
        String spoilerMessage = "\nОтветы: " + solving.getVariableAnswerResults();
        SendMessageParams params = wrapMessageParams(solving.getUser().getId(),
                boldMessage + codeMessage + spoilerMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.CODE, boldMessage.length(), codeMessage.length()),
                        new MessageEntity(TextType.SPOILER, boldMessage.length() + codeMessage.length(),
                                spoilerMessage.length())),
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
            OpenAnswer answer = openAnswerService.get(solving.getUser().getId(), answersIds.get(i), solving.getId());
            String boldMessage1 = "❓ Открытый вопрос:\n";
            String boldMessage2 = "\n☑️ Ваш ответ:\n";
            String boldMessage3 = "\n️✅ Правильный ответ:\n";
            telegramApiClient.sendMessage(getOpenAnswerParams(question, answer, solving.getUser().getId(),
                    boldMessage1, boldMessage2, boldMessage3, null));
        }
    }

    private void clearSolving(Solving solving) {
        if (solving.getVisibility() != VisibilityType.ESTIMATED) {
            solvingService.remove(solving);
        }
    }
}

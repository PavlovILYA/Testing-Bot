package ru.mephi.knowledgechecker.strategy.impl.test.check;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.ReplyKeyboardRemove;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.answer.MarkType;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.OpenAnswerService;
import ru.mephi.knowledgechecker.service.OpenQuestionService;
import ru.mephi.knowledgechecker.service.SolvingService;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.getOpenAnswerParams;
import static ru.mephi.knowledgechecker.common.IdsUtils.concatIt;
import static ru.mephi.knowledgechecker.common.IdsUtils.parseIds;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getCheckOpenAnswerKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class MarkOpenAnswerStrategy extends AbstractMessageStrategy {
    private final SolvingService solvingService;
    private final OpenQuestionService questionService;
    private final OpenAnswerService answerService;
    private final ShowWorksForCheckStrategy showWorksForCheckStrategy;

    public MarkOpenAnswerStrategy(SolvingService solvingService,
                                  OpenQuestionService questionService,
                                  OpenAnswerService answerService,
                                  @Lazy ShowWorksForCheckStrategy showWorksForCheckStrategy) {
        this.solvingService = solvingService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.showWorksForCheckStrategy = showWorksForCheckStrategy;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update) &&
                MarkType.of(update.getMessage().getText()) != null;
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Solving solving = solvingService.get(data.getStudent().getId(), data.getTest().getId(), VisibilityType.ESTIMATED);
        MarkType mark = MarkType.of(update.getMessage().getText());
        saveMark(solving, mark);
        solving = solvingService.save(solving);
        sendNextAnswer(data, solving, update);
    }

    private void saveMark(Solving solving, MarkType mark) {
        solving.setOpenAnswerResults(concatIt(solving.getOpenAnswerResults(), mark.getMark()));
    }

    public void sendNextAnswer(CurrentData data, Solving solving, Update update) throws StrategyProcessException {
        List<Long> questionIds = parseIds(solving.getOpenQuestionIds());
        List<Long> answerIds = parseIds(solving.getOpenAnswerIds());
        List<Long> results = parseIds(solving.getOpenAnswerResults());

        if (answerIds.size() == results.size()) {
            sendFinishCongrats(data.getUser().getId(), results);
            data.setStudent(null);
            solving.setChecked(true);
            solvingService.save(solving);
            clearInlineKeyboard(data);
            showWorksForCheckStrategy.process(data, update);
            return;
        }

        int currentIndex = results.size();
        OpenQuestion question = questionService.get(questionIds.get(currentIndex));
        OpenAnswer answer = answerService.get(solving.getUser().getId(), answerIds.get(currentIndex), solving.getId());
        String boldMessage1 = "❓ Открытый вопрос " + (currentIndex + 1) + " (из " + answerIds.size() + "):\n";
        String boldMessage2 = "\n☑️ Ответ " + data.getStudent().getUsername() + ":\n";
        String boldMessage3 = "\n️✅ Правильный ответ:\n";
        telegramApiClient.sendMessage(getOpenAnswerParams(question, answer, data.getUser().getId(),
                boldMessage1, boldMessage2, boldMessage3, getCheckOpenAnswerKeyboardMarkup()));
    }

    private void sendFinishCongrats(Long authorId, List<Long> results) {
        String boldMessage = "🏁 Проверка открытых вопросов завершена 🏁\n";
        long max = results.size() * 5L;
        Long mark = results.stream().reduce(0L, Long::sum);
        String codeMessage = "⏰ Студент набрал " + mark + " (из " + max + ")";
        SendMessageParams params = wrapMessageParams(authorId, boldMessage + codeMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.CODE, boldMessage.length(), codeMessage.length())),
                new ReplyKeyboardRemove(true));
        telegramApiClient.sendMessage(params);
    }
}

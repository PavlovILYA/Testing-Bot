package ru.mephi.knowledgechecker.strategy.impl.test.create.question.open;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.CreationPhaseType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.OpenQuestionService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.addingQuestionParams;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ReadOpenQuestionInfoStrategy extends AbstractMessageStrategy {
    private final OpenQuestionService openQuestionService;

    public ReadOpenQuestionInfoStrategy(OpenQuestionService openQuestionService,
                                        @Lazy QuestionAddingState questionAddingState) {
        this.openQuestionService = openQuestionService;
        this.nextState = questionAddingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Test test = data.getTest();
        switch (data.getNextPhase()) {
            case TEXT:
                readText(data, test, update.getMessage().getText());
                break;
            case CORRECT_ANSWER:
                readCorrectAnswer(data, test, update.getMessage().getText());
                break;
            default: // todo: add attachment
        }
    }

    private void readText(CurrentData data, Test test, String text) {
        OpenQuestion question = OpenQuestion.builder()
                .text(text)
                .test(test)
                .build();
        question = openQuestionService.save(question);
        data.setOpenQuestion(question);
        data.setNextPhase(CreationPhaseType.CORRECT_ANSWER);

        String message = "Введите правильный ответ";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length()),
                        new MessageEntity(TextType.UNDERLINE, 8, 10)),
                null);
        sendMessageAndSave(params, data);
    }

    private void readCorrectAnswer(CurrentData data, Test test, String correctAnswer) {
        OpenQuestion question = data.getOpenQuestion();
        question.setCorrectAnswer(correctAnswer);
        openQuestionService.save(question);
        data.setOpenQuestion(null);
        data.setNextPhase(null);
        data.setNeedCheck(true);

        SendMessageParams params = addingQuestionParams(test, data.getUser().getId(), data.getCourse());
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

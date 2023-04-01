package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.VariableAnswerService;
import ru.mephi.knowledgechecker.service.VariableQuestionService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.variable.WrongVariableAnswerAddingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import static ru.mephi.knowledgechecker.common.CommonMessageParams.addingWrongAnswerParams;

@Component
public class ReadWrongVariableAnswerStrategy extends AbstractMessageStrategy {
    private final VariableQuestionService variableQuestionService;
    private final VariableAnswerService variableAnswerService;

    public ReadWrongVariableAnswerStrategy(VariableQuestionService variableQuestionService,
                                           VariableAnswerService variableAnswerService,
                                           @Lazy WrongVariableAnswerAddingState wrongVariableAnswerAddingState) {
        this.variableQuestionService = variableQuestionService;
        this.variableAnswerService = variableAnswerService;
        this.nextState = wrongVariableAnswerAddingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        VariableQuestion question = data.getVariableQuestion();
        String answerText = update.getMessage().getText();
        if (question.getCorrectAnswer().getText().equals(answerText)) {
            throw new StrategyProcessException(data.getUser().getId(),
                    "Это правильный вариант ответа, попробуйте еще раз ввести пример неверного");
        }
        if (answerText.length() > 30) {
            throw new StrategyProcessException(data.getUser().getId(),
                    "Максимальная длина вариативного ответа 30 символов, попробуйте еще раз");
        }
        VariableAnswer answer = variableAnswerService.getByText(answerText);
        if (answer == null) {
            answer = VariableAnswer.builder()
                    .text(answerText)
                    .build();
            answer = variableAnswerService.save(answer);
        }
        question.getWrongAnswers().add(answer);
        try {
            question = variableQuestionService.save(question);
            data.setVariableQuestion(question);
        } catch (RuntimeException e) {
            throw new StrategyProcessException(data.getUser().getId(),
                    "Этот вариант ответа Вы уже использовали для этого вопроса, попробуйте еще раз");
        }

        SendMessageParams params = addingWrongAnswerParams(question, data.getUser().getId());
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

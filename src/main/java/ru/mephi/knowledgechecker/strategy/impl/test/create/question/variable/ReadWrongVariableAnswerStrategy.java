package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.VariableAnswerService;
import ru.mephi.knowledgechecker.service.VariableQuestionService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.variable.WrongVariableAnswerAddingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddWrongVariableAnswerInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

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
    public boolean apply(Update update) {
        return super.apply(update);
    }

    @Override
    public void process(User user, Update update) throws StrategyProcessException {
        CurrentData data = user.getData();
        VariableQuestion question = data.getVariableQuestion();
        String answerText = update.getMessage().getText();
        if (question.getCorrectAnswer().getText().equals(answerText)) {
            throw new StrategyProcessException(user.getId(),
                    "Это правильный вариант ответа, попробуйте еще раз ввести пример неверного");
        }
        if (answerText.length() > 30) {
            throw new StrategyProcessException(user.getId(),
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
            throw new StrategyProcessException(user.getId(),
                    "Этот вариант ответа Вы уже использовали для этого вопроса, попробуйте еще раз");
        }
        saveToContext(nextState, data);

        String boldMessage = "Добавление неверного ответа";
        String italicMessage =
                "\n\nНа данный момент добавлено " + question.getWrongAnswers().size() + " неверных ответов\n" +
                "Максимальное количество отображаемых неверных вопросов: " + (question.getMaxAnswerNumber() - 1);
        MessageParams params = wrapMessageParams(user.getId(), boldMessage + italicMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.UNDERLINE, 11, 9),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                getAddWrongVariableAnswerInlineKeyboardMarkup());
        telegramApiClient.sendMessage(params);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.service.VariableAnswerService;
import ru.mephi.knowledgechecker.service.VariableQuestionService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.variable.WrongVariableAnswerAddingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.List;
import java.util.Map;

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
    public void process(Update update, Map<String, Object> data) {
        VariableQuestion question = variableQuestionService.get((Long) data.get("questionId"));
        if (question.getCorrectAnswer().getText().equals(update.getMessage().getText())) {
            sendError(update.getMessage().getFrom().getId(), "Это правильный вариант ответа, " +
                    "попробуйте еще раз ввести пример неверного");
            return;
        }
        if (update.getMessage().getText().length() > 30) {
            sendError(update.getMessage().getFrom().getId(), "Максимальная длина вариативного ответа 30 символов, " +
                    "попробуйте еще раз");
            return;
        }
        VariableAnswer answer = variableAnswerService.getByText(update.getMessage().getText());
        if (answer == null) {
            answer = VariableAnswer.builder()
                    .text(update.getMessage().getText())
                    .build();
            answer = variableAnswerService.save(answer);
        }
        question.getWrongAnswers().add(answer);
        try {
            question = variableQuestionService.save(question);
        } catch (RuntimeException e) {
            sendError(update.getMessage().getFrom().getId(), "Этот вариант ответа Вы уже использовали для этого вопроса, " +
                    "попробуйте еще раз");
            return;
        }
        String boldMessage = "Добавление неверного ответа";
        String italicMessage =
                "\n\nНа данный момент добавлено " + question.getWrongAnswers().size() + " неверных ответов\n" +
                "Максимальное количество отображаемых неверных вопросов: " + (question.getMaxAnswerNumber() - 1);
        MessageParams params =
                wrapMessageParams(update.getMessage().getFrom().getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("underline", 11, 9),
                                new MessageEntity("italic", boldMessage.length(), italicMessage.length())),
                        getAddWrongVariableAnswerInlineKeyboardMarkup());
        putStateToContext(update.getMessage().getFrom().getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

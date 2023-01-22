package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.QuestionAddingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.TO_QUESTION_ADDING;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddQuestionInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ToQuestionAddingStrategy extends AbstractCallbackQueryStrategy {
    private final TestService testService;

    public ToQuestionAddingStrategy(TestService testService,
                                    @Lazy QuestionAddingState questionAddingState) {
        this.testService = testService;
        this.nextState = questionAddingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(TO_QUESTION_ADDING);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        Test test = testService.getByUniqueTitle((String) data.get("testId"));
        int questionCount = test.getVariableQuestions().size();
        questionCount += test.getOpenQuestions().size();
        String boldMessage = "Добавление вопроса";
        String italicMessage =
                "\n\nНа данный момент сохранено " + questionCount + " вопросов\n" +
                "Максимальное количество отображаемых вопросов: " + test.getMaxQuestionsNumber();
        MessageParams params =
                wrapMessageParams(update.getCallbackQuery().getFrom().getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("italic", boldMessage.length(), italicMessage.length())),
                        getAddQuestionInlineKeyboardMarkup());
        data.remove("next");
        putStateToContext(update.getCallbackQuery().getFrom().getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

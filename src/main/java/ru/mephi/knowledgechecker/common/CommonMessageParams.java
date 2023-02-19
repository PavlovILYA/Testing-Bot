package ru.mephi.knowledgechecker.common;

import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.test.Test;

import java.util.List;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddQuestionInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

public class CommonMessageParams {
    public static SendMessageParams addingQuestionParams(Test test, Long userId) {
        int questionCount = test.getVariableQuestions().size();
        questionCount += test.getOpenQuestions().size();
        String boldMessage = "Добавление вопроса";
        String italicMessage =
                "\n\nНа данный момент сохранено вопросов: " + questionCount +
                        "\nМаксимальное количество отображаемых вопросов: " + test.getMaxQuestionsNumber();
        return  wrapMessageParams(userId, boldMessage + italicMessage,
                        List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                                new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                        getAddQuestionInlineKeyboardMarkup());
    }
}

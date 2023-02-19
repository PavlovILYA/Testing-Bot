package ru.mephi.knowledgechecker.common;

import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.test.Test;

import java.util.List;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddQuestionInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddWrongVariableAnswerInlineKeyboardMarkup;
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

    public static SendMessageParams addingWrongAnswerParams(VariableQuestion question, Long userId) {
        String boldMessage = "Добавление неверного ответа";
        String italicMessage = "\n\nНа данный момент добавлено неверных ответов: " + question.getWrongAnswers().size()
                + "\nМаксимальное количество отображаемых неверных вопросов: " + (question.getMaxAnswerNumber() - 1);
        return wrapMessageParams(userId, boldMessage + italicMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.UNDERLINE, 11, 9),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                getAddWrongVariableAnswerInlineKeyboardMarkup());
    }
}

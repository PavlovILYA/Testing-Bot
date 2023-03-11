package ru.mephi.knowledgechecker.common;

import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.test.Test;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_PRIVATE_TEST_LIST;
import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddQuestionInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddWrongVariableAnswerInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

public class CommonMessageParams {
    public static SendMessageParams addingQuestionParams(Test test, Long userId, Course course) {
        String doneCallbackData = TO_PUBLIC_TEST_LIST.name();
        if (course != null) {
            doneCallbackData = TO_PRIVATE_TEST_LIST.name();
        }
        int questionCount = test.getVariableQuestions().size();
        questionCount += test.getOpenQuestions().size();
        String boldMessage = "Добавление вопроса";
        String italicMessage =
                "\n\nНа данный момент сохранено вопросов: " + questionCount +
                        "\nМаксимальное количество отображаемых вопросов: " + test.getMaxQuestionsNumber();
        return  wrapMessageParams(userId, boldMessage + italicMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                getAddQuestionInlineKeyboardMarkup(doneCallbackData));
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

    public static SendMessageParams askSearchQueryParams(Long userId) {
        String boldMessage = "🔎️ Введите поисковой запрос\n\n";
        String italicMessage = "(Введите ключевое выражение, которое вероятнее всего содержится в названии)" +
                "\n❗️ Чтобы объединить выборки по нескольким запросам, введите несколько ключевых выражений, " +
                "разделенных точкой с запятой";
        return wrapMessageParams(userId, boldMessage + italicMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                null);
    }

    public static SendMessageParams nothingIsFoundParams(Long userId) {
        String message = "По данному запросу ничего не найдено 🤷🏼‍";
        return wrapMessageParams(userId, message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length())),
                null);
    }
}

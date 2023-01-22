package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.service.VariableAnswerService;
import ru.mephi.knowledgechecker.service.VariableQuestionService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.variable.WrongVariableAnswerAddingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getAddWrongVariableAnswerInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class ReadVariableQuestionStrategy extends AbstractMessageStrategy {
    private final UserService userService;
    private final TestService testService;
    private final VariableQuestionService variableQuestionService;
    private final VariableAnswerService variableAnswerService;

    public ReadVariableQuestionStrategy(UserService userService,
                                        TestService testService,
                                        VariableQuestionService variableQuestionService,
                                        VariableAnswerService variableAnswerService,
                                        @Lazy WrongVariableAnswerAddingState wrongVariableAnswerAddingState) {

        this.userService = userService;
        this.testService = testService;
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
        User user = userService.get(update.getMessage().getFrom().getId());
        Test test = testService.getByUniqueTitle((String) data.get("testId"));
        switch ((String) data.get("next")) {
            case "text":
                readText(update.getMessage().getText(), data, user, test);
                break;
            case "correctAnswer":
                readCorrectAnswer(update.getMessage().getText(), data, user);
                break;
            case "maxAnswerNumber":
                readMaxAnswerNumber(Integer.parseInt(update.getMessage().getText()), data, user);
                break;
            default: // todo: add attachment
        }
    }

    private void readText(String text, Map<String, Object> data, User user, Test test) {
        VariableQuestion question = VariableQuestion.builder()
                .text(text)
                .test(test)
                .build();
        question = variableQuestionService.save(question);
        String boldMessage = "Введите правильный ответ";
        String italicMessage = "\n\nПредпочтительно вводить короткие варианты ответа: A, B, etc.";
        MessageParams params =
                wrapMessageParams(user.getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("underline", 8, 10),
                                new MessageEntity("italic", boldMessage.length(), italicMessage.length())),
                        null);
        data.put("next", "correctAnswer");
        data.put("questionId", question.getId());
        putStateToContext(user.getId(), data);
        telegramApiClient.sendMessage(params);
    }

    private void readCorrectAnswer(String correctAnswerText, Map<String, Object> data, User user) {
        VariableQuestion question = variableQuestionService.get((Long) data.get("questionId"));
        VariableAnswer answer = VariableAnswer.builder()
                .text(correctAnswerText)
                .build();
        answer = variableAnswerService.save(answer);
        question.setCorrectAnswer(answer);
        variableQuestionService.save(question);
        String boldMessage = "Введите максимальное количество неверных ответов";
        String italicMessage = "\n\n(Максимум 9)";
        MessageParams params =
                wrapMessageParams(user.getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("italic", boldMessage.length(), italicMessage.length())),
                        null);
        data.put("next", "maxAnswerNumber");
        putStateToContext(user.getId(), data);
        telegramApiClient.sendMessage(params);
    }

    private void readMaxAnswerNumber(Integer maxAnswerNumber, Map<String, Object> data, User user) {
        VariableQuestion question = variableQuestionService.get((Long) data.get("questionId"));
        question.setMaxAnswerNumber(maxAnswerNumber + 1);
        question = variableQuestionService.save(question);
        String boldMessage = "Добавление неверного ответа";
        String italicMessage =
                "\n\nНа данный момент добавлено " + question.getWrongAnswers().size() + " неверных ответов\n" +
                "Максимальное количество отображаемых неверных вопросов: " + (question.getMaxAnswerNumber() - 1);
        MessageParams params =
                wrapMessageParams(user.getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("underline", 11, 9),
                                new MessageEntity("italic", boldMessage.length(), italicMessage.length())),
                        getAddWrongVariableAnswerInlineKeyboardMarkup());
        data.remove("next");
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

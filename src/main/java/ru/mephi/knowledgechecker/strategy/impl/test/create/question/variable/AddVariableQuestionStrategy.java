package ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.variable.VariableQuestionInfoReadingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.ADD_VARIABLE_QUESTION;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddVariableQuestionStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;

    public AddVariableQuestionStrategy(UserService userService,
                                       @Lazy VariableQuestionInfoReadingState variableQuestionInfoReadingState) {
        this.userService = userService;
        this.nextState = variableQuestionInfoReadingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(ADD_VARIABLE_QUESTION);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        User user = userService.get(update.getCallbackQuery().getFrom().getId());
        String boldMessage = "Введите содержание вопроса";
        String italicMessage =
                "\n\nПредпочтительно добавить в формулировку вопроса варианты ответа:\n" +
                "A: Ответ 1\n" +
                "B: Ответ 2\n" +
                "etc.";
        //String boldAndUnderlineMessage = "\nМаксимальная длина варианта ответа – 30 символов";
        MessageParams params =
                wrapMessageParams(user.getId(), boldMessage + italicMessage,
                        List.of(new MessageEntity("bold", 0, boldMessage.length()),
                                new MessageEntity("italic", boldMessage.length(), italicMessage.length())),
                        null);
        data.put("next", "text");
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

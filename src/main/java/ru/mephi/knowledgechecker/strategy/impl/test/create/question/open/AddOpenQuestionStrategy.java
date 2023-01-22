package ru.mephi.knowledgechecker.strategy.impl.test.create.question.open;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.create.question.open.OpenQuestionInfoReadingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.ADD_OPEN_QUESTION;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Component
public class AddOpenQuestionStrategy extends AbstractCallbackQueryStrategy {
    private final UserService userService;

    public AddOpenQuestionStrategy(UserService userService,
                                   @Lazy OpenQuestionInfoReadingState openQuestionInfoReadingState) {
        this.userService = userService;
        this.nextState = openQuestionInfoReadingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(ADD_OPEN_QUESTION);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        User user = userService.get(update.getCallbackQuery().getFrom().getId());
        String message = "Введите содержание вопроса";
        MessageParams params =
                wrapMessageParams(user.getId(), message,
                        List.of(new MessageEntity("bold", 0, message.length())),
                        null);
        data.put("next", "text");
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

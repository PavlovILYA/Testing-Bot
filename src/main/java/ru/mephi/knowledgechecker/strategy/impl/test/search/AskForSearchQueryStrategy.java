package ru.mephi.knowledgechecker.strategy.impl.test.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.test.search.TestSearchAttemptState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.Constants.FIND_PUBLIC_TEST;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class AskForSearchQueryStrategy extends AbstractCallbackQueryStrategy {
    public AskForSearchQueryStrategy(@Lazy TestSearchAttemptState testSearchAttemptState) {
        nextState = testSearchAttemptState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && (update.getCallbackQuery().getData().equals(FIND_PUBLIC_TEST));
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String boldMessage = "🔎️ Введите поисковой запрос\n\n";
        String italicMessage = "(Введите ключевое выражение, которое вероятнее всего содержится в названии теста)" +
                "\n❗️ Чтобы объединить выборки по нескольким запросам, введите несколько ключевых выражений, " +
                "разделенных точкой с запятой";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), boldMessage + italicMessage,
                List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                        new MessageEntity(TextType.ITALIC, boldMessage.length(), italicMessage.length())),
                null);
        deleteMenu(data);
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.DataType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractActionStrategy;

import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestListInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class ToPublicTestListStrategy extends AbstractActionStrategy {
    private final UserService userService;

    public ToPublicTestListStrategy(UserService userService,
                                    @Lazy PublicTestListState nextState) {
        this.userService = userService;
        this.nextState = nextState;
    }

    @Override
    public boolean apply(Update update) {
        return update.getCallbackQuery() != null
                && update.getCallbackQuery().getData().equals(PUBLIC_TEST_LIST)
                ||
                update.getMessage() != null
                && update.getMessage().getText().equals(PUBLIC_TEST_LIST);
    }

    @Override
    public void process(Update update, Map<DataType, Object> data) throws StrategyProcessException {
        Long userId;
        if (update.getCallbackQuery() != null) {
            userId = update.getCallbackQuery().getFrom().getId();
        } else {
            userId = update.getMessage().getChat().getId();
        }
        User user = userService.get(userId);
        String text = "🔽\nГЛАВНОЕ МЕНЮ\n⬇️️\nПУБЛИЧНЫЕ ТЕСТЫ";
        MessageParams params =
                wrapMessageParams(userId, text, List.of(new MessageEntity(TextType.BOLD, 0, text.length())),
                        getPublicTestListInlineKeyboardMarkup(user));
        putStateToContext(userId, nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

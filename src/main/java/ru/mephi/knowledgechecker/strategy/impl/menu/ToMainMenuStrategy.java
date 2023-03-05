package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.CommandType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.impl.menu.MainMenuState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractActionStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_MAIN_MENU;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getStartKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MAIN_MENU;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;
import static ru.mephi.knowledgechecker.strategy.impl.AbstractBotCommandStrategy.BOT_COMMAND;

@Slf4j
@Component
public class ToMainMenuStrategy extends AbstractActionStrategy {

    public ToMainMenuStrategy(@Lazy MainMenuState nextState) {
        this.nextState = nextState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return update.getCallbackQuery() != null
                && update.getCallbackQuery().getData().equals(TO_MAIN_MENU.name())
                ||
                update.getMessage() != null
                && update.getMessage().getEntities() != null
                && update.getMessage().getEntities().stream()
                .anyMatch(e -> e.getType().equals(BOT_COMMAND))
                && update.getMessage().getText().equals(CommandType.START.getName());
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), MAIN_MENU.getTitle(),
                List.of(new MessageEntity(TextType.BOLD, 0, MAIN_MENU.getTitle().length())),
                getStartKeyboardMarkup());
        data.setState(nextState);
        sendMenuAndSave(params, data);
    }
}

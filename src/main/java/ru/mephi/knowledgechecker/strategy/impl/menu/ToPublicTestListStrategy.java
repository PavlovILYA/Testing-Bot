package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.CallbackDataType.DELETE_TEST;
import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestMenuInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class ToPublicTestListStrategy extends AbstractCallbackQueryStrategy {

    private final TestService testService;

    public ToPublicTestListStrategy(TestService testService,
                                    @Lazy PublicTestListState nextState) {
        this.testService = testService;
        this.nextState = nextState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && (
                        update.getCallbackQuery().getData().equals(TO_PUBLIC_TEST_LIST.name())
                        ||
                        update.getCallbackQuery().getData().equals(DELETE_TEST.name())
                );
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Test test = data.getTest();
        if (update.getCallbackQuery().getData().equals(DELETE_TEST.name())) {
            testService.delete(test.getId());
        }
        if (data.isNeedCheck()) {
            if (test.getOpenQuestions().size() + test.getVariableQuestions().size() == 0) {
                throw new StrategyProcessException(data.getUser().getId(),
                        "Необходимо добавить как минимум один вопрос",
                        update.getCallbackQuery().getId());
            }
            data.setNeedCheck(false);
        }
        data.setTest(null);

        Page<String> publicTests = testService.getCreatedTests(data.getUser().getId());
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), PUBLIC_TEST_LIST.getTitle(),
                List.of(new MessageEntity(TextType.BOLD, 0, PUBLIC_TEST_LIST.getTitle().length())),
                getPublicTestMenuInlineKeyboardMarkup(publicTests));
        data.setState(nextState);
        sendMenuAndSave(params, data);
    }
}

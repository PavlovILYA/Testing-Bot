package ru.mephi.knowledgechecker.strategy.impl.test.create.manual;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.user.CreationPhaseType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.create.manual.TestInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.test.create.AbstractSaveTestToDbStrategy;

import java.util.List;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class SaveManualTestToDbStrategy extends AbstractSaveTestToDbStrategy {
    public SaveManualTestToDbStrategy(TestService testService,
                                      @Lazy TestInfoReadingState testInfoReadingState) {
        super(testService);
        this.nextState = testInfoReadingState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        super.process(data, update);
        data.setNextPhase(CreationPhaseType.TITLE);

        String message = "Введите полное (неуникальное) название теста"; // todo
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length()),
                        new MessageEntity(TextType.UNDERLINE, 16, 12)),
                null);
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

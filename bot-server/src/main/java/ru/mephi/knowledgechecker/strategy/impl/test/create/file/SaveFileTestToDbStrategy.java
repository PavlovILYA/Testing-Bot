package ru.mephi.knowledgechecker.strategy.impl.test.create.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.create.file.ImportTestState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.test.create.AbstractSaveTestToDbStrategy;

@Slf4j
@Component
public class SaveFileTestToDbStrategy extends AbstractSaveTestToDbStrategy {
    public SaveFileTestToDbStrategy(TestService testService,
                                    @Lazy ImportTestState importTestState) {
        super(testService);
        this.nextState = importTestState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        super.process(data, update);
        data.setState(nextState);
        sendMessageAndSave("Отправьте GIFT-файл с вопросами", data);
    }
}

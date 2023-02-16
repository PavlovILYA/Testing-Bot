package ru.mephi.knowledgechecker.strategy.impl.test.create;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.CreationPhaseType;
import ru.mephi.knowledgechecker.common.DataType;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.create.TestInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_PREFIX;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class ReadUniqueTestNameStrategy extends AbstractMessageStrategy {
    private final TestService testService;
    private final UserService userService;

    public ReadUniqueTestNameStrategy(TestService testService, UserService userService,
                                      @Lazy TestInfoReadingState testInfoReadingState) {
        this.testService = testService;
        this.userService = userService;
        this.nextState = testInfoReadingState;
    }

    @Override
    public boolean apply(Update update) {
        return super.apply(update);
    }

    @Override
    public void process(Update update, Map<DataType, Object> data) throws StrategyProcessException {
        String uniqueTestName = update.getMessage().getText();
        User user = userService.get(update.getMessage().getFrom().getId());
        if (testService.getByUniqueTitle(uniqueTestName) != null) {
            throw new StrategyProcessException(update.getMessage().getFrom().getId(),
                    "Тест с таким названием уже существует, попробуйте еще раз");
        }
        if ((uniqueTestName + ":" + PUBLIC_TEST_PREFIX).getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new StrategyProcessException(update.getMessage().getFrom().getId(),
                    "Длина уникального названия теста больше 30, попробуйте еще раз");
        }
        Test test = Test.builder()
                .uniqueTitle(uniqueTestName)
                .creator(user)
                .testType(TestType.PUBLIC)
                .build();
        test = testService.save(test);
        log.info("Unique test title: {}, userId: {}", test.getUniqueTitle(), user.getId());
        String message = "Введите полное (неуникальное) название теста";
        MessageParams params =
                wrapMessageParams(user.getId(), message,
                        List.of(new MessageEntity(TextType.BOLD, 0, message.length()),
                                new MessageEntity(TextType.UNDERLINE, 16, 12)),
                        null);
        data.clear();
        data.put(DataType.TEST_ID, test.getUniqueTitle());
        data.put(DataType.NEXT_CREATION_PHASE, CreationPhaseType.TITLE);
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

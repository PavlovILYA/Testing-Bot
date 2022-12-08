package ru.mephi.knowledgechecker.strategy.impl.test.create;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.test.create.TestInfoReadingState;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.util.Map;

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
    public void process(Update update, Map<String, Object> data) {
        String uniqueTestName = update.getMessage().getText();
        User user = userService.get(update.getMessage().getFrom().getId());
        Test test = Test.builder()
                .uniqueTitle(uniqueTestName)
                .creator(user)
                .testType(TestType.PUBLIC)
                .build();
        test = testService.save(test);
        log.info("Unique test title: {}, userId: {}", test.getUniqueTitle(), user.getId());
        MessageParams params =
                wrapMessageParams(user.getId(), "Введите полное (неуникальное) название теста", null);
        data.clear();
        data.put("testId", test.getUniqueTitle());
        data.put("next", "title");
        putStateToContext(user.getId(), nextState, data);
        telegramApiClient.sendMessage(params);
    }
}

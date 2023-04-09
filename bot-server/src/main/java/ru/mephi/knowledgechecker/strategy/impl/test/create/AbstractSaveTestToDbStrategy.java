package ru.mephi.knowledgechecker.strategy.impl.test.create;

import lombok.extern.slf4j.Slf4j;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.nio.charset.StandardCharsets;

import static ru.mephi.knowledgechecker.common.Constants.COLON;
import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_PREFIX;

@Slf4j
public abstract class AbstractSaveTestToDbStrategy extends AbstractMessageStrategy {
    protected final TestService testService;

    protected AbstractSaveTestToDbStrategy(TestService testService) {
        this.testService = testService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update);
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        String uniqueTestName = update.getMessage().getText();
        if (testService.getByUniqueTitle(uniqueTestName) != null) {
            throw new StrategyProcessException(data.getUser().getId(),
                    "Тест с таким названием уже существует, попробуйте еще раз");
        }
        if ((uniqueTestName + COLON + PUBLIC_TEST_PREFIX).getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new StrategyProcessException(data.getUser().getId(),
                    "Длина уникального названия теста больше 30, попробуйте еще раз");
        }

        Course course = null;
        TestType publicity = TestType.PUBLIC;
        VisibilityType visibilityType = VisibilityType.TRAIN;
        if (data.getCourse() != null) {
            course = data.getCourse();
            publicity = TestType.PRIVATE;
            visibilityType = VisibilityType.INVISIBLE;
        }
        Test test = Test.builder()
                .uniqueTitle(uniqueTestName)
                .creator(data.getUser())
                .testType(publicity)
                .course(course)
                .visibility(visibilityType)
                .build();
        test = testService.save(test);
        log.info("Unique test title: {}, userId: {}", test.getUniqueTitle(), data.getUser().getId());

        data.setTest(test);
    }
}

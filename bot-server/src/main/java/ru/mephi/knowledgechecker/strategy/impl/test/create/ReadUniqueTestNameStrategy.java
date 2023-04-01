package ru.mephi.knowledgechecker.strategy.impl.test.create;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.TestType;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.CreationPhaseType;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.test.create.TestInfoReadingState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static ru.mephi.knowledgechecker.common.Constants.COLON;
import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_PREFIX;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class ReadUniqueTestNameStrategy extends AbstractMessageStrategy {
    private final TestService testService;

    public ReadUniqueTestNameStrategy(TestService testService,
                                      @Lazy TestInfoReadingState testInfoReadingState) {
        this.testService = testService;
        this.nextState = testInfoReadingState;
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
        data.setNextPhase(CreationPhaseType.TITLE);

        String message = "Введите полное (неуникальное) название теста";
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message,
                List.of(new MessageEntity(TextType.BOLD, 0, message.length()),
                        new MessageEntity(TextType.UNDERLINE, 16, 12)),
                null);
        data.setState(nextState);
        sendMessageAndSave(params, data);
    }
}

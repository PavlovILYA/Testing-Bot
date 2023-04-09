package ru.mephi.knowledgechecker.strategy.impl.test.create.file;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.parser.ParserResponse;
import ru.mephi.knowledgechecker.dto.telegram.income.FileDto;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.httpclient.ParserApiClient;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.menu.TestListState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

import java.io.File;

import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getOwnPrivateTestListKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestMenuKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_COURSE;
import static ru.mephi.knowledgechecker.common.MenuTitleType.PUBLIC_TEST_LIST;

@Component
public class ImportTestStrategy extends AbstractMessageStrategy {
    private final TelegramApiClient telegramApiClient;
    private final ParserApiClient parserApiClient;
    private final TestService testService;

    public ImportTestStrategy(TelegramApiClient telegramApiClient,
                              ParserApiClient parserApiClient,
                              TestService testService,
                              @Lazy TestListState testListState) {
        this.telegramApiClient = telegramApiClient;
        this.parserApiClient = parserApiClient;
        this.testService = testService;
        this.nextState = testListState;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getMessage().getDocument() != null;
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        Test test = data.getTest();
        FileDto fileDto = telegramApiClient.getFileDto(update.getMessage().getDocument().getFileId());
        File file = telegramApiClient.downloadFile(fileDto.getFileId() + ".gift", fileDto.getFilePath());
        ParserResponse parserResponse = parserApiClient.importTest(data.getUser().getId(), test.getId(), file);
        file.delete();
        if (parserResponse.getStatus() != 201) {
            testService.delete(test.getId()); // todo отмена || удаление всех вопросов!
            test = testService.save(copy(test, data.getUser()));
            data.setTest(test);
            saveToContext(data);
            throw new StrategyProcessException(data.getUser().getId(), parserResponse.getMessage());
        }

        test.setMaxQuestionsNumber(parserResponse.getQuestionsNumber());
        testService.save(test);
        data.setTest(null);
        data.setState(nextState);
        if (data.getCourse() == null) {
            Page<String> publicTests = testService.getCreatedTests(data.getUser().getId());
            sendMenuAndSave(data, PUBLIC_TEST_LIST.getTitle(), getPublicTestMenuKeyboardMarkup(publicTests));
        } else {
            Course course = data.getCourse();
            Page<String> privateTestsPage = testService.getTestsByCourse(course);
            sendMenuAndSave(data, MANAGE_COURSE.getTitle() + course.getTitle() + " – ТЕСТЫ",
                    getOwnPrivateTestListKeyboardMarkup(privateTestsPage, course.getId()));
        }
    }

    private Test copy(Test test, User user) {
        return  Test.builder()
                .uniqueTitle(test.getUniqueTitle())
                .creator(user)
                .testType(test.getTestType())
                .course(test.getCourse())
                .visibility(test.getVisibility())
                .build();
    }
}

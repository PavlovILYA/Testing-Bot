package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.service.CourseService;
import ru.mephi.knowledgechecker.service.TestService;
import ru.mephi.knowledgechecker.state.impl.menu.TestListState;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;
import ru.mephi.knowledgechecker.strategy.impl.AbstractCallbackQueryStrategy;

import static ru.mephi.knowledgechecker.common.CallbackDataType.DELETE_TEST;
import static ru.mephi.knowledgechecker.common.CallbackDataType.TO_PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.Constants.COLON;
import static ru.mephi.knowledgechecker.common.Constants.OWN_COURSE_PREFIX;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getManageCourseInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.KeyboardMarkups.getPublicTestMenuInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.MenuTitleType.MANAGE_COURSE;
import static ru.mephi.knowledgechecker.common.MenuTitleType.PUBLIC_TEST_LIST;

@Slf4j
@Component
public class ToTestListStrategy extends AbstractCallbackQueryStrategy {

    private final TestService testService;
    private final CourseService courseService;

    public ToTestListStrategy(TestService testService, CourseService courseService,
                              @Lazy TestListState testListState) {
        this.nextState = testListState;
        this.testService = testService;
        this.courseService = courseService;
    }

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && (
                        update.getCallbackQuery().getData().equals(TO_PUBLIC_TEST_LIST.name())
                        ||
                        update.getCallbackQuery().getData().equals(DELETE_TEST.name())
                        ||
                        update.getCallbackQuery().getData().split(COLON)[0].equals(OWN_COURSE_PREFIX)
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

        data.setState(nextState);
        if (!update.getCallbackQuery().getData().split(COLON)[0].equals(OWN_COURSE_PREFIX)) {
            Page<String> publicTests = testService.getCreatedTests(data.getUser().getId());
            sendMenuAndSave(data, PUBLIC_TEST_LIST.getTitle(), getPublicTestMenuInlineKeyboardMarkup(publicTests));
        } else {
            Long id = Long.parseLong(update.getCallbackQuery().getData().split(COLON)[1]);
            Course course = courseService.getById(id);
            data.setCourse(course);
            String message = MANAGE_COURSE.getTitle() + course.getTitle();
            Page<String> privateTestsPage = testService.getTestsByCourse(course);
            sendMenuAndSave(data, message, getManageCourseInlineKeyboardMarkup(privateTestsPage));
        }
    }
}

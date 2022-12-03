package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class StartMenuState extends AbstractState {
    @Override
    @PostConstruct
    public void initializeAvailableStates() {
        super.initializeAvailableStates();
        availableStates.add(applicationContext.getBean(PublicTestListState.class));
        availableStates.add(applicationContext.getBean(CoursesListState.class));
        availableStates.add(applicationContext.getBean(AdminMenuState.class));
        log.info("availableStates: {}", availableStates);
    }

    @Override
    public void process(Update update) {
    }
}

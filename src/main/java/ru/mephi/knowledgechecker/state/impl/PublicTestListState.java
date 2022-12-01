package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class PublicTestListState extends AbstractState {
    @Override
    @PostConstruct
    public void initializeAvailableStates() {
        availableStates.add(applicationContext.getBean(CoursesListState.class));

        log.info("ApplicationContext: {}", applicationContext);
        log.info("availableStates: {}", availableStates);
    }
    // todo
}

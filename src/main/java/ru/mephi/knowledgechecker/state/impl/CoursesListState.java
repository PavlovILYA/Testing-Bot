package ru.mephi.knowledgechecker.state.impl;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CoursesListState extends AbstractState {
    @Override
    @PostConstruct
    public void initializeAvailableStates() {
    }
    // todo
}

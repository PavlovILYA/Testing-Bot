package ru.mephi.knowledgechecker.state.impl;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;

import javax.annotation.PostConstruct;

@Component
public class AdminMenuState extends AbstractState {
    @Override
    @PostConstruct
    public void initializeAvailableStates() {
        super.initializeAvailableStates();
    }

    @Override
    public void process(Update update) {
    }
}

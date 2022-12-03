package ru.mephi.knowledgechecker.state.impl;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;

import javax.annotation.PostConstruct;

@Component
public class UnknownState extends AbstractState {
    @Override
    @PostConstruct
    public void initializeAvailableStates() {
    }

    @Override
    public void process(Update update) {
    }
}

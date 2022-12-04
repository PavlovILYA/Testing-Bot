package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.state.StateContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotManagerService {
    private final StateContext stateContext;

    public void process(Update update) {
        stateContext.process(update);
        // todo провалидировать здесь update на id
    }
}

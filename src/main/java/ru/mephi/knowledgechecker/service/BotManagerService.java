package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.strategy.UpdateContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotManagerService {
    private final UpdateContext updateContext;

    public void process(Update update) {
        updateContext.process(update);
    }
}

package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Message;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.state.ParamsWrapper;

import javax.annotation.PostConstruct;

import static ru.mephi.knowledgechecker.state.Constants.START_COMMAND;

@Slf4j
@Component
public class StartMenuState extends AbstractState {
    @Override
    @PostConstruct
    public void initializeAvailableStates() {
        super.initializeAvailableStates();
        // todo переделать инъекцию через конструктор и @Qualifier
        availableStates.add(applicationContext.getBean(PublicTestListState.class));
        availableStates.add(applicationContext.getBean(CoursesListState.class));
        availableStates.add(applicationContext.getBean(AdminMenuState.class));
        log.info("availableStates: {}", availableStates);
    }

    @Override
    public void processCommand(Message message) {
        if (message.getText().equals(START_COMMAND)) {
            MessageParams params = ParamsWrapper.wrapMessageParams(message.getFrom().getId(), "Приветствую! 👒");
            telegramApiClient.sendMessage(params);
        } else {
            super.processCommand(message);
        }
    }
}

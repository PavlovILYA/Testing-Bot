package ru.mephi.knowledgechecker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.knowledgechecker.dto.telegram.Update;
import ru.mephi.knowledgechecker.service.UpdateService;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateService updateService;

    @PostMapping
    public void giveUpdate(@RequestBody Update update) {
        log.info("Update: {}", update);
        analyzeUpdate(update);
    }

    private void analyzeUpdate(Update update) {
        if (update.getMessage() != null) {
            updateService.processMessage(update.getMessage());
        }
        if (update.getCallbackQuery() != null) {
            updateService.processCallbackQuery(update.getCallbackQuery());
        }
    }
}

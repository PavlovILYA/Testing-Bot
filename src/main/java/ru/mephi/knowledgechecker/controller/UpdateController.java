package ru.mephi.knowledgechecker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.knowledgechecker.dto.telegram.Update;

@Slf4j
@RestController
@RequestMapping("/")
public class UpdateController {
    @PostMapping
    public void giveUpdates(@RequestBody Update update) {
        log.info("Updates: {}", update);
    }
}

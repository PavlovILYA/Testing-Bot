package ru.mephi.knowledgechecker.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.mephi.knowledgechecker.dto.webhook.WebHookCreateDto;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotConfig {
    private final RestTemplate restTemplate;
    @Value("${telegram.api}")
    private String telegramApi;
    @Value("${webhook.url}")
    private String webhookUrl;

    @PostConstruct
    public void postConstruct() {
        WebHookCreateDto webHookCreateDto = WebHookCreateDto.builder()
                .url(webhookUrl)
                .build();
        ResponseEntity<Object> responseDeleteWebhook = restTemplate
                .postForEntity(telegramApi + "/deleteWebhook", null, Object.class);
        ResponseEntity<Object> responseSetWebhook = restTemplate
                .postForEntity(telegramApi + "/setWebhook", webHookCreateDto, Object.class);

        log.info("Delete webhook: {}", responseDeleteWebhook.getBody());
        log.info("Set webhook again: {}", responseSetWebhook.getBody());
    }
}

package ru.mephi.knowledgechecker.httpclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mephi.knowledgechecker.dto.telegram.income.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramApiClient {
    private final RestTemplate restTemplate;
    @Value(("${telegram.api}"))
    private String telegramApi;

    public void sendMessage(Object sendMessageParams) {
        log.info("send to Telegram API: {}", sendMessageParams);
        ResponseEntity<Message> responseSendMessage = restTemplate
                .postForEntity(telegramApi + "/sendMessage", sendMessageParams, Message.class);
    }
}

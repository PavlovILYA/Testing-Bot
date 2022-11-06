package ru.mephi.knowledgechecker.httpclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mephi.knowledgechecker.dto.telegram.Message;
import ru.mephi.knowledgechecker.dto.telegram.SendMessageParams;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramApiClient {
    private final RestTemplate restTemplate;
    @Value(("${telegram.api}"))
    private String telegramApi;

    public void sendMessage(SendMessageParams sendMessageParams) {
        ResponseEntity<Message> responseSendMessage = restTemplate
                .postForEntity(telegramApi + "/sendMessage", sendMessageParams, Message.class);
    }
}

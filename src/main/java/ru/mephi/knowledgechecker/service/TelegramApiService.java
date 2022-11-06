package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mephi.knowledgechecker.dto.telegram.Message;
import ru.mephi.knowledgechecker.dto.telegram.SendMessageParams;

@Service
@RequiredArgsConstructor
public class TelegramApiService {
    private final RestTemplate restTemplate;
    @Value(("${telegram.api}"))
    private String telegramApi;

    public void sendMessage(SendMessageParams sendMessageParams) {
        ResponseEntity<Message> responseSendMessage = restTemplate
                .postForEntity(telegramApi+"/sendMessage", sendMessageParams, Message.class);
    }
}

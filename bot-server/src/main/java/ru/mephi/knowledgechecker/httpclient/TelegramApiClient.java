package ru.mephi.knowledgechecker.httpclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.mephi.knowledgechecker.dto.telegram.income.FileDto;
import ru.mephi.knowledgechecker.dto.telegram.income.FileResponse;
import ru.mephi.knowledgechecker.dto.telegram.income.Response;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.*;

import java.io.File;
import java.io.FileOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramApiClient {
    private final RestTemplate restTemplate;
    @Value(("${telegram.api}"))
    private String telegramApi;
    @Value(("${telegram.file.api}"))
    private String telegramFileApi;

    public Long sendMessage(SendMessageParams params) {
        log.info("[To Telegram API] send message: {}", params);
        ResponseEntity<Response> response = restTemplate
                .postForEntity(telegramApi + "/sendMessage", params, Response.class);
        log.info("Response from TG: {}", response.getBody().getResult());
        return response.getBody().getResult().getId();
    }

    public Long editMessageText(EditMessageTextParams params) {
        log.info("[To Telegram API] edit message text: {}", params);
        ResponseEntity<Response> response = restTemplate
                .postForEntity(telegramApi + "/editMessageText", params, Response.class);
        log.info("Response from TG: {}", response.getBody().getResult());
        return response.getBody().getResult().getId();
    }

    public void editMessageReplyMarkup(EditMessageReplyMarkupParams params) {
        log.info("[To Telegram API] edit buttons: {}", params);
        ResponseEntity<Object> response = restTemplate
                .postForEntity(telegramApi + "/editMessageReplyMarkup", params, Object.class);
    }

    public void deleteMessage(DeleteMessageParams params) {
        log.info("[To Telegram API] delete message: {}", params);
        try {
            ResponseEntity<String> response = restTemplate
                    .postForEntity(telegramApi + "/deleteMessage", params, String.class);
            log.info("Response from TG: {}", response.getBody());
        } catch (HttpClientErrorException e) {
            editMessageText(new EditMessageTextParams(params));
        }
    }

    public void answerCallbackQuery(SendPopupParams params) {
        log.info("[To Telegram API] send popup: {}", params);
        try {
            ResponseEntity<Object> response = restTemplate
                    .postForEntity(telegramApi + "/answerCallbackQuery", params, Object.class);
        } catch (HttpClientErrorException e) {
            log.warn("ERROR: {}", e.getMessage());
        }
    }

    public void sendDocument(Long userId, File file) {
        LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("chat_id", userId);
        params.add("document", new FileSystemResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(params, headers);

        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                telegramApi + "/sendDocument",
                HttpMethod.POST,
                requestEntity,
                Response.class);
    }

    public FileDto getFileDto(String fileId) {
        LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("file_id", fileId);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(params);
        log.info("[To Telegram API] get file: {}", fileId);
        ResponseEntity<FileResponse> responseEntity = restTemplate.exchange(
                telegramApi + "/getFile",
                HttpMethod.POST,
                requestEntity,
                FileResponse.class);
        log.info("Response from TG: {}", responseEntity.getBody().getResult());
        return responseEntity.getBody().getResult();
    }

    public File downloadFile(String fileName, String filePath) {
        log.info("[To Telegram API] download file: {}", filePath);
        return restTemplate.execute(telegramFileApi + "/" + filePath, HttpMethod.GET, null, clientHttpResponse -> {
            File ret = new File(fileName);
            ret.createNewFile();
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });
    }
}

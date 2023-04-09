package ru.mephi.knowledgechecker.httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.mephi.knowledgechecker.dto.parser.ParserResponse;
import ru.mephi.knowledgechecker.model.test.Test;

import java.io.File;
import java.io.FileOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParserApiClient {
    private final RestTemplate restTemplate;
    @Value(("${parser.api}"))
    private String parserApi;

    public File exportTest(Long userId, Test test) {
        String fileUrl = parserApi + "/users/" + userId + "/tests/" + test.getId();
        log.info("[To Parser API] {}", fileUrl);
        File file = restTemplate.execute(fileUrl, HttpMethod.GET, null, clientHttpResponse -> {
            File ret = new File(test.getUniqueTitle() + ".gift");
            ret.createNewFile();
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });
        log.info("Response from PARSER: {}", file.getAbsolutePath());
        return file;
    }

    public ParserResponse importTest(Long userId, Long testId, File file) {
        String fileUrl = parserApi + "/users/" + userId + "/tests/" + testId;

        LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("file", new FileSystemResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(params, headers);
        ResponseEntity<ParserResponse> responseEntity;
        log.info("[To Parser API] {}", fileUrl);
        try {
            responseEntity = restTemplate.exchange(
                    fileUrl,
                    HttpMethod.POST,
                    requestEntity,
                    ParserResponse.class);
        } catch (HttpStatusCodeException e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(e.getResponseBodyAsString(), ParserResponse.class);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
        log.info("Response from PARSER: {}", responseEntity.getBody());

        return responseEntity.getBody();
    }
}

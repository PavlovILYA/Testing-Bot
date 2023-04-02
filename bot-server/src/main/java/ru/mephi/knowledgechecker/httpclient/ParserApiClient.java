package ru.mephi.knowledgechecker.httpclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

    public File getFile(Long userId, Test test) {
        String fileUrl = parserApi + "/users/" + userId + "/tests/" + test.getId();
        log.info("[To Parser API] {}", fileUrl);
        File file = restTemplate.execute(fileUrl, HttpMethod.GET, null, clientHttpResponse -> {
            File ret = new File(test.getUniqueTitle() + ".gift");
            ret.createNewFile();
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });
        log.info("Response from TG: {}", file.getAbsolutePath());
        return file;
    }
}

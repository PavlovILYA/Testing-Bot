package ru.mephi.giftparser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {
    public Long saveFile(Long userId, MultipartFile file) {
        return 0L;
    }

    public File loadAsResource(Long userId, Long testId) throws IOException {
        return new File(getClass().getClassLoader().getResource("db.txt").getFile());
//        String data = FileUtils.readFileToString(file, "UTF-8");
//        return data.getBytes(StandardCharsets.UTF_8);
    }
}

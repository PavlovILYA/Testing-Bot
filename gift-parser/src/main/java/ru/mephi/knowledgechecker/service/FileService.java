package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.converter.FileConverter;
import ru.mephi.knowledgechecker.exception.TestNotFoundException;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.repository.TestRepository;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final TestRepository testRepository;
    private final FileConverter fileConverter;

    public int importFile(Long userId, Long testId, String fileContent) {
        Test test = testRepository.findByIdAndCreatorId(testId, userId)
                .orElseThrow(() -> new TestNotFoundException(userId, testId));
        return fileConverter.parseFileContent(test, fileContent);
    }

    public File exportFile(Long userId, Long testId) throws IOException {
        Test test = testRepository.findByIdAndCreatorId(testId, userId)
                .orElseThrow(() -> new TestNotFoundException(userId, testId));
        File file = fileConverter.convertToFile(test);
        log.info("Converted test {} of user {}: {}", testId, userId, file.getAbsolutePath());
        return file;
    }
}

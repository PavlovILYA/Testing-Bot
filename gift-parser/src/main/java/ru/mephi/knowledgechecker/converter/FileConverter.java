package ru.mephi.knowledgechecker.converter;

import ru.mephi.knowledgechecker.model.test.Test;

import java.io.File;
import java.io.IOException;

public interface FileConverter {
    File convertToFile(Test test) throws IOException;

    int parseFileContent(Test test, String fileContent);
}

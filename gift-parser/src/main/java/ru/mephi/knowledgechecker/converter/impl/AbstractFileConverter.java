package ru.mephi.knowledgechecker.converter.impl;

import ru.mephi.knowledgechecker.converter.FileConverter;
import ru.mephi.knowledgechecker.exception.FileParsingException;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.test.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class AbstractFileConverter implements FileConverter {
    @Override
    public File convertToFile(Test test) throws IOException {
        StringBuilder builder = new StringBuilder();
        convertTestTitle(builder, test.getUniqueTitle());
        convertVariableQuestions(builder, test.getVariableQuestions());
        convertOpenQuestions(builder, test.getOpenQuestions());

        File tmpFile = File.createTempFile(test.getUniqueTitle() + "__", getSuffix());
        try (FileWriter writer = new FileWriter(tmpFile, false)) {
            writer.write(builder.toString());
            writer.flush();
        } catch (IOException e) {
            throw new FileParsingException(e.getMessage());
        }
        return tmpFile;
    }

    @Override
    public Long parseFile(Long userId, File file) {
        if (!file.getName().endsWith(getSuffix())) {
            throw new FileParsingException("Invalid file format!");
        }
        return parseFileContent(userId, file);
    }

    protected abstract String getSuffix();

    protected abstract void convertVariableQuestions(StringBuilder builder, List<VariableQuestion> variableQuestions);

    protected abstract void convertOpenQuestions(StringBuilder builder, List<OpenQuestion> openQuestions);

    protected abstract void convertTestTitle(StringBuilder builder, String title);

    protected abstract Long parseFileContent(Long userId, File file);
}

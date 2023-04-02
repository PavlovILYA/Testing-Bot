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
        convertVariableQuestions(builder, test.getVariableQuestions());
        convertOpenQuestions(builder, test.getOpenQuestions());

        File tmpFile = File.createTempFile(test.getUniqueTitle() + "__", ".gift");
        try (FileWriter writer = new FileWriter(tmpFile, false)) {
            writer.write(builder.toString());
            writer.flush();
        } catch (IOException ex) {
            throw new FileParsingException(ex.getMessage());
        }
        return tmpFile;
    }

    protected abstract void convertVariableQuestions(StringBuilder builder, List<VariableQuestion> variableQuestions);

    protected abstract void convertOpenQuestions(StringBuilder builder, List<OpenQuestion> openQuestions);
}

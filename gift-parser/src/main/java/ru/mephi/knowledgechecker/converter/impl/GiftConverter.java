package ru.mephi.knowledgechecker.converter.impl;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.exception.FileParsingException;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
public class GiftConverter extends AbstractFileConverter {
    private static final String TITLE = "::";
    private static final char TITLE_PART = ':';
    private static final char BEGIN_ANSWERS = '{';
    private static final char END_ANSWERS = '}';
    private static final char INCORRECT_ANSWER = '~';
    private static final char CORRECT_ANSWER = '=';
    private static final String COMMENT = "//";
    private static final char COMMENT_PART = '/';
    private static final char ANSWER_COMMENT = '#';
    private static final String TRUE = "TRUE";
    private static final char T = 'T';
    private static final String FALSE = "FALSE";
    private static final char F = 'F';
    private static final char LIKELIHOOD = '%';
    private static final String NEXT_LINE = "\n";
    private static final String Q = "Q";

    @Override
    protected String getSuffix() {
        return ".gift";
    }

    @Override
    protected void convertTestTitle(StringBuilder builder, String title) {
        builder.append(COMMENT + " ").append(title).append(NEXT_LINE);
    }

    @Override
    protected void convertVariableQuestions(StringBuilder builder, List<VariableQuestion> variableQuestions) {
        for (int i = 0; i < variableQuestions.size(); i++) {
            VariableQuestion question = variableQuestions.get(i);
            builder.append(TITLE).append(Q).append(i).append(TITLE).append(" ")
                    .append(question.getText()).append(BEGIN_ANSWERS).append(NEXT_LINE)
                    .append(CORRECT_ANSWER).append(question.getCorrectAnswer().getText()).append(NEXT_LINE);
            for (VariableAnswer answer : question.getWrongAnswers()) {
                builder.append(INCORRECT_ANSWER).append(answer.getText()).append(NEXT_LINE);
            }
            builder.append(END_ANSWERS).append(NEXT_LINE).append(NEXT_LINE);
        }
    }

    @Override
    protected void convertOpenQuestions(StringBuilder builder, List<OpenQuestion> openQuestions) {
        for (int i = 0; i < openQuestions.size(); i++) {
            OpenQuestion question = openQuestions.get(i);
            builder.append(TITLE).append(Q).append(i).append(TITLE).append(" ")
                    .append(question.getText()).append(BEGIN_ANSWERS).append(NEXT_LINE)
                    .append(CORRECT_ANSWER).append(question.getText()).append(NEXT_LINE)
                    .append(END_ANSWERS).append(NEXT_LINE).append(NEXT_LINE);
        }
    }

    @Override
    protected Long parseFileContent(Long userId, File file) { // todo название теста получать http-параметром
        try (FileReader reader = new FileReader(file)) {
            int c;
            int p = -1;
            while ((c = reader.read()) != -1) {
                p = parseSymbol(p, c, reader);
            }
        } catch (IOException e) {
            throw new FileParsingException(e.getMessage());
        }
        return null;
    }

    private char parseSymbol(int p, int c, FileReader reader) throws IOException {
        switch ((char) c) {
            case TITLE_PART:
                readTitle(p, c, reader);
                break;
            case BEGIN_ANSWERS:
                break;
            case END_ANSWERS:
                break;
            case INCORRECT_ANSWER:
                break;
            case CORRECT_ANSWER:
                break;
            case COMMENT_PART:
                break;
            case ANSWER_COMMENT:
                break;
            case T:
                break;
            case F:
                break;
            case LIKELIHOOD:
                break;
            default:
                throw new FileParsingException("Unknown symbol: " + c);
        }
        return 0;
    }

    private void readTitle(int p, int c, FileReader reader) throws IOException {
//        if ((char) p != TITLE_PART) {
//            reader.read()
//        }
    }
}

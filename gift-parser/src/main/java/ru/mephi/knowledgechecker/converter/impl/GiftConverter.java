package ru.mephi.knowledgechecker.converter.impl;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;

import java.io.File;
import java.util.List;

@Component
public class GiftConverter extends AbstractFileConverter {
    public static final String TITLE = "::";
    public static final String TITLE_PART = ":";
    public static final String BEGIN_ANSWERS = "{";
    public static final String BEGIN_ANSWERS_RE = "\\{";
    public static final String END_ANSWERS = "}";
    public static final String END_ANSWERS_RE = "\\}";
    public static final String INCORRECT_ANSWER = "~";
    public static final String CORRECT_ANSWER = "=";
    public static final String COMMENT = "//";
    public static final String COMMENT_PART = "/";
    public static final String ANSWER_COMMENT = "#";
    public static final String TRUE = "TRUE";
    public static final String T = "T";
    public static final String TRUE_RU = "Верно";
    public static final String FALSE = "FALSE";
    public static final String F = "F";
    public static final String FALSE_RU = "Неверно";
    public static final String LIKELIHOOD = "%";
    public static final String NEXT_LINE = "\n";
    public static final String Q = "Q";
    public static final List<String> BINARY_ANSWERS = List.of(TRUE, T, FALSE, F);

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
//        try (FileReader reader = new FileReader(file)) {
//            int c;
//            int p = -1;
//            while ((c = reader.read()) != -1) {
//                p = parseSymbol(p, c, reader);
//            }
//        } catch (IOException e) {
//            throw new FileParsingException(e.getMessage());
//        }
        return null;
    }

//    protected Long parseFileContentOld(Long userId, File file) { // todo название теста получать http-параметром
//        try (FileReader reader = new FileReader(file)) {
//            int c;
//            int p = -1;
//            while ((c = reader.read()) != -1) {
//                p = parseSymbol(p, c, reader);
//            }
//        } catch (IOException e) {
//            throw new FileParsingException(e.getMessage());
//        }
//        return null;
//    }
//
//    private char parseSymbol(int p, int c, FileReader reader) throws IOException {
//        switch ((char) c) {
//            case TITLE_PART:
//                readTitle(p, c, reader);
//                break;
//            case BEGIN_ANSWERS:
//                break;
//            case END_ANSWERS:
//                break;
//            case INCORRECT_ANSWER:
//                break;
//            case CORRECT_ANSWER:
//                break;
//            case COMMENT_PART:
//                break;
//            case ANSWER_COMMENT:
//                break;
//            case T:
//                break;
//            case F:
//                break;
//            case LIKELIHOOD:
//                break;
//            default:
//                throw new FileParsingException("Unknown symbol: " + c);
//        }
//        return 0;
//    }
//
//    private void readTitle(int p, int c, FileReader reader) throws IOException {
////        if ((char) p != TITLE_PART) {
////            reader.read()
////        }
//    }
}

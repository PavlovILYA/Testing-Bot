package ru.mephi.knowledgechecker.converter.impl;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;

import java.util.List;

@Component
public class GiftConverter extends AbstractFileConverter {
    public static final String TITLE = "::";
    public static final String BEGIN_ANSWERS = "{";
    public static final String END_ANSWERS = "}";
    public static final String INCORRECT_ANSWER = "~";
    public static final String CORRECT_ANSWER = "=";
    public static final String COMMENT = "//";
    public static final String ANSWER_COMMENT = "#";
    public static final String TRUE = "TRUE";
    public static final String T = "T";
    public static final String FALSE = "FALSE";
    public static final String F = "F";
    public static final String LIKELIHOOD = "%";
    public static final String NEXT_LINE = "\n";
    public static final String Q = "Q";

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
}

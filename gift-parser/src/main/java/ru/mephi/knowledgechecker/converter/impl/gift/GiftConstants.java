package ru.mephi.knowledgechecker.converter.impl.gift;

import java.util.List;

public class GiftConstants {
    public static final String TITLE = "::";
    public static final String BEGIN_ANSWERS = "{";
    public static final String BEGIN_ANSWERS_RE = "\\{";
    public static final String END_ANSWERS = "}";
    public static final String END_ANSWERS_RE = "\\}";
    public static final String INCORRECT_ANSWER = "~";
    public static final String CORRECT_ANSWER = "=";
    public static final String COMMENT = "//";
    public static final String ANSWER_COMMENT = "#";
    public static final String TRUE = "TRUE";
    public static final String T = "T";
    public static final String TRUE_RU = "Верно";
    public static final String FALSE = "FALSE";
    public static final String F = "F";
    public static final String FALSE_RU = "Неверно";
    public static final String LIKELIHOOD = "%";
    public static final String PERCENT_REGEXP = LIKELIHOOD + "\\d+\\.?\\d*" + LIKELIHOOD + ".*";
    public static final String NEXT_LINE = "\n";
    public static final String Q = "Q";
    public static final List<String> BINARY_ANSWERS = List.of(TRUE, T, FALSE, F);
}

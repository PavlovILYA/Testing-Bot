package ru.mephi.knowledgechecker.converter.impl.gift;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.converter.impl.AbstractFileConverter;
import ru.mephi.knowledgechecker.exception.FileParsingException;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.service.QuestionService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.mephi.knowledgechecker.converter.impl.gift.GiftConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GiftConverter extends AbstractFileConverter {
    private final QuestionService questionService;
    private final ThreadLocal<Test> threadLocalTest = new ThreadLocal<>();

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
            builder.append(TITLE).append("open").append(Q).append(i).append(TITLE).append(" ")
                    .append(question.getText()).append(BEGIN_ANSWERS).append(NEXT_LINE)
                    .append(CORRECT_ANSWER).append(question.getCorrectAnswer()).append(NEXT_LINE)
                    .append(END_ANSWERS).append(NEXT_LINE).append(NEXT_LINE);
        }
    }

    @Override
    public int parseFileContent(Test test, String fileContent) {
        fileContent = fileContent.replaceAll(COMMENT + ".*\n", "");
        log.info("file content without comments:\n{}", fileContent);
        String[] questions = parseQuestions(fileContent);
        if (questions.length == 0) {
            throw new FileParsingException("Document contains no questions!");
        }
        threadLocalTest.set(test);
        Arrays.stream(questions).forEach(this::parseQuestion);
        return questions.length;
    }

    private String[] parseQuestions(String fileContent) {
        return fileContent.split("\n{2,}");
    }

    private void parseQuestion(String question) {
        log.info("{}\n-------------------", question);
        StringBuilder builder = new StringBuilder(question.strip());
        String title = parseTitle(builder);
        log.info("title:{}", title);
        log.info("rest:{}", builder);

        String text = parseQuestionText(builder);
        log.info("text:{}", text);
        log.info("!ANSWERS:{}", builder);

        parseAnswers(builder.toString().replace("\n", "").strip(), text);

        log.info("===================");
    }

    private String parseTitle(StringBuilder builder) {
        Pattern titlePattern = Pattern.compile(TITLE + ".+" + TITLE);
        Matcher titleMatcher = titlePattern.matcher(builder);
        if (titleMatcher.find()) {
            String title = titleMatcher.group().replace(TITLE, "").strip();
            builder.replace(titleMatcher.start(), titleMatcher.end(), "");
            return title;
        } else {
            return null;
        }
    }

    private String parseQuestionText(StringBuilder builder) {
        String question = builder.toString().strip();
        String replacement;
        String commonRegEx = BEGIN_ANSWERS_RE + "[^" + END_ANSWERS_RE + "]*" + END_ANSWERS_RE;
        if (Pattern.compile("(.*\\n*.*)+" + commonRegEx).matcher(question).matches()) {
            replacement = "";
        } else if (Pattern.compile("(.*\\n*.*)+" + commonRegEx + "(.*\\n*.*)+").matcher(question).matches()) {
            replacement = "____";
        } else {
            throw new FileParsingException("Parsing question text error");
        }

        Matcher matcher = Pattern.compile("(.*\\n*.*)+" + BEGIN_ANSWERS_RE).matcher(builder);
        matcher.find();
        builder.replace(matcher.start(), matcher.end(), "");
        matcher = Pattern.compile(END_ANSWERS_RE + "(.*\\n*.*)+").matcher(builder);
        matcher.find();
        builder.replace(matcher.start(), matcher.end(), "");

        return question.replaceAll(commonRegEx, replacement).strip();
    }

    private void parseAnswers(String strWithAnswers, String questionText) {
        String[] answers = Arrays.stream(strWithAnswers
                        .split(CORRECT_ANSWER + "|" + INCORRECT_ANSWER))
                .filter(s -> !s.isBlank())
                .map(s -> s.replaceAll(ANSWER_COMMENT + ".*", "")) // todo add comments to db?
                .toArray(String[]::new);
        log.info("answers size: {}", answers.length);
        Arrays.stream(answers).forEach(System.out::println);
        if (answers.length == 0) {
            saveEssay(questionText);
        } else if (answers.length == 1 && BINARY_ANSWERS.contains(answers[0].toUpperCase().strip())) {
            saveBinaryQuestion(questionText, answers[0].toUpperCase().strip());
        } else {
            List<String> correctAnswers = new ArrayList<>();
            List<String> incorrectAnswers = new ArrayList<>();
            for (String answer : answers) {
                if (strWithAnswers.contains(INCORRECT_ANSWER + answer)) {
                    incorrectAnswers.add(answer.strip());
                } else if (strWithAnswers.contains(CORRECT_ANSWER + answer)) {
                    correctAnswers.add(answer.strip());
                }
            }
            saveGeneralQuestion(questionText, correctAnswers, incorrectAnswers);
        }
    }

    private void saveEssay(String questionText) {
        questionService.saveOpenQuestion(threadLocalTest.get(), questionText, "Студент должен написать эссе");
    }

    private void saveBinaryQuestion(String questionText, String answer) {
        if (answer.equals(T) || answer.equals(TRUE)) {
            questionService.saveVariableQuestion(threadLocalTest.get(), questionText, TRUE_RU, List.of(FALSE_RU));
        } else {
            questionService.saveVariableQuestion(threadLocalTest.get(), questionText, FALSE_RU, List.of(TRUE_RU));
        }
    }

    private void saveGeneralQuestion(String questionText,
                                            List<String> correctAnswers, List<String> incorrectAnswers) {
        if (!correctAnswers.isEmpty() && incorrectAnswers.isEmpty()) {
            questionService.saveOpenQuestion(threadLocalTest.get(), questionText, String.join(";", correctAnswers));
        } else if (!incorrectAnswers.isEmpty() && correctAnswers.isEmpty()) {
            if (incorrectAnswers.stream().filter(s -> s.matches(PERCENT_REGEXP)).count() < 2) {
                throw new FileParsingException("Invalid question format!");
            }
            combinePercentAnswers(questionText, incorrectAnswers);
        } else {
            if (checkAnyPercent(correctAnswers) ||
                    checkAnyPercent(incorrectAnswers) ||
                    correctAnswers.size() != 1 ||
                    incorrectAnswers.isEmpty()) { // todo %?
                throw new FileParsingException("Invalid question format!");
            }
            questionService.saveVariableQuestion(threadLocalTest.get(), questionText,
                    correctAnswers.get(0), incorrectAnswers);
        }
    }

    private void combinePercentAnswers(String questionText, List<String> answers) {
        Random random = new Random();
        List<String> correct = answers.stream().filter(s -> s.matches(PERCENT_REGEXP))
                .map(s -> s.replaceAll(LIKELIHOOD + "\\d+\\.?\\d*" + LIKELIHOOD, ""))
                .collect(Collectors.toList());
        List<String> incorrect = answers.stream().filter(s -> !s.matches(PERCENT_REGEXP)).collect(Collectors.toList());
        String editedCorrectAnswer = String.join(", ", correct);
        List<String> editedAnswers = new ArrayList<>();
        if (incorrect.isEmpty()) {
            editedAnswers.addAll(correct);
        } else {
            for (String i : incorrect) {
                List<String> iList = new ArrayList<>(correct);
                random.setSeed(incorrect.indexOf(i) * 500000L);
                iList.remove(random.nextInt(correct.size()));
                iList.add(i);
                Collections.shuffle(iList);
                editedAnswers.add(String.join(", ", iList));
            }
        }
        editedAnswers.add(editedCorrectAnswer);
        Collections.shuffle(editedAnswers);
        int correctIndex = editedAnswers.indexOf(editedCorrectAnswer) + 1;
        StringBuilder builder = new StringBuilder(questionText);
        List<String> editedIncorrectAnswers = new ArrayList<>();
        for (int i = 0; i < editedAnswers.size(); i++) {
            builder.append("\n").append(i + 1).append(": ").append(editedAnswers.get(i));
            if (i + 1 != correctIndex) {
                editedIncorrectAnswers.add(String.valueOf(i + 1));
            }
        }
        questionService.saveVariableQuestion(threadLocalTest.get(), builder.toString(),
                String.valueOf(correctIndex), editedIncorrectAnswers);
    }

    private boolean checkAnyPercent(List<String> answers) {
        return answers.stream().anyMatch(s -> s.matches(PERCENT_REGEXP));
    }
}

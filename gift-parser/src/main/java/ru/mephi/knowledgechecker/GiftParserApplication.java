package ru.mephi.knowledgechecker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mephi.knowledgechecker.exception.FileParsingException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.mephi.knowledgechecker.converter.impl.GiftConverter.*;

@SpringBootApplication
public class GiftParserApplication {

	public static void main(String[] args) {
		String initialString = "::Вопрос 1::Кто похоронен в могиле Гранта?\n" +
				"{\n" +
				"~Грант\n" +
				"~Джефферсон =Никто\n" +
				"}\n\n" +
				"::Вопрос 2::Ленин {~похоронен =родился ~живет} в Симбирске" +
				"\n\n" +
				"::Вопрос 3::Какие цвета используются в светофоре? {\n" +
				"~синий\n" +
				"~%50%красный ~%50%желтый ~фиолетовый\n" +
				"~белый\n" +
				"}" +
				"\n\n" +
				"::Вопрос 4::Кто похоронен в могиле Гранта?{\n" +
				"=никто\n" +
				"=никого\n" +
				"}" +
				"\n\n" +
				"::Вопрос 5::Ленин _____________ в Симбирске{\n" +
				"=родился\n" +
				"}" +
				"\n\n" +
				"::Вопрос 6::Иисус Христос из: { =%25%Вифлеема#Он родился тут, но вырос в\n" +
				"другом городе.\n" +
				"=%50%Галилея#Вы должны быть более\n" +
				"определенным.\n" +
				"=%100%Назарета#Да! Это правильный ответ!\n" +
				"}" +
				"\n\n" +
				"::Вопрос 7::Ленин родился в Симбирске{TRUE}" +
				"\n\n" +
				"::Вопрос 8::Напишите коротко биографию Гранта {}";

		String[] questions = parseQuestions(initialString);
		if (questions.length == 0) {
			throw new FileParsingException("Document contains no questions!");
		}
		Arrays.stream(questions).forEach(GiftParserApplication::parseQuestion);

//		SpringApplication.run(GiftParserApplication.class, args);
	}

	private static String[] parseQuestions(String fileContent) {
		return fileContent.split("\n{2,}"); // validate?
	}

	private static void parseQuestion(String question) {
		System.out.println(question + "\n-------------------");
		// todo del //comment
		StringBuilder builder = new StringBuilder(question.replace("\n", "").strip());
		String title = parseTitle(builder);
		System.out.println("title:" + title);
		System.out.println("rest:" + builder);

		String text = parseQuestionText(builder);
		System.out.println("text:" + text);
		System.out.println("!ANSWERS:" + builder);

		parseAnswers(builder.toString().strip(), text);

		System.out.println("===================");
	}

	private static String parseTitle(StringBuilder builder) {
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

	private static String parseQuestionText(StringBuilder builder) {
		String question = builder.toString().strip();
		String replacement;
		String commonRegEx = BEGIN_ANSWERS_RE + "[^" + END_ANSWERS_RE + "]*" + END_ANSWERS_RE;
		if (Pattern.compile(".*" + commonRegEx).matcher(question).matches()) {
			replacement = "";
		} else if (Pattern.compile(".*" + commonRegEx + ".*").matcher(question).matches()) {
			replacement = "____";
		} else {
			throw new FileParsingException("Parsing question text error");
		}

		Matcher matcher = Pattern.compile(".*" + BEGIN_ANSWERS_RE).matcher(builder);
		matcher.find();
		builder.replace(matcher.start(), matcher.end(), "");
		matcher = Pattern.compile(END_ANSWERS_RE + ".*").matcher(builder);
		matcher.find();
		builder.replace(matcher.start(), matcher.end(), "");

		return question.replaceAll(commonRegEx, replacement);
	}

	private static void parseAnswers(String strWithAnswers, String questionText) {
		String[] answers = Arrays.stream(strWithAnswers
				.split(CORRECT_ANSWER + "|" + INCORRECT_ANSWER))
				.filter(s -> !s.isBlank())
				.map(s -> s.replaceAll(ANSWER_COMMENT + ".*", "")) // todo add comments to db?
				.toArray(String[]::new);
		System.out.println("answers size: " + answers.length);
		Arrays.stream(answers).forEach(System.out::println);
		if (answers.length == 0) {
			saveEssay(questionText);
		} else if (answers.length == 1 && BINARY_ANSWERS.contains(answers[0].toUpperCase().trim())) {
			saveBinaryQuestion(questionText, answers[0].toUpperCase().trim());
		} else {
			List<String> correctAnswers = new ArrayList<>();
			List<String> incorrectAnswers = new ArrayList<>();
			for (String answer : answers) {
				if (strWithAnswers.contains(INCORRECT_ANSWER + answer)) {
					incorrectAnswers.add(answer.trim());
				} else if (strWithAnswers.contains(CORRECT_ANSWER + answer)) {
					correctAnswers.add(answer.trim());
				}
			}
			saveGeneralQuestion(questionText, correctAnswers, incorrectAnswers);
		}
	}

	private static void saveEssay(String questionText) {
		saveOpenQuestion(questionText, "Студент должен написать эссе");
	}

	private static void saveBinaryQuestion(String questionText, String answer) {
		if (answer.equals(T) || answer.equals(TRUE)) {
			saveVariableQuestion(questionText, TRUE_RU, List.of(FALSE_RU));
		} else {
			saveVariableQuestion(questionText, FALSE_RU, List.of(TRUE_RU));
		}
	}

	private static void saveGeneralQuestion(String questionText,
											List<String> correctAnswers, List<String> incorrectAnswers) {
		if (!correctAnswers.isEmpty() && incorrectAnswers.isEmpty()) {
			saveOpenQuestion(questionText, String.join(";", correctAnswers));
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
			saveVariableQuestion(questionText, correctAnswers.get(0), incorrectAnswers);
		}
	}

	private static void combinePercentAnswers(String questionText, List<String> answers) {
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
		saveVariableQuestion(builder.toString(), String.valueOf(correctIndex), editedIncorrectAnswers);
	}

	private static boolean checkAnyPercent(List<String> answers) {
		return answers.stream().anyMatch(s -> s.matches(PERCENT_REGEXP));
	}

	private static void saveOpenQuestion(String question, String correctAnswer) {
		System.out.println("OPEN QUESTION:\n" + question);
		System.out.println("ANSWER:\n" + correctAnswer);
	}

	private static void saveVariableQuestion(String question, String correctAnswer, List<String> incorrectAnswers) {
		System.out.println("VARIABLE QUESTION:\n" + question);
		System.out.println("CORRECT ANSWER:\n" + correctAnswer);
		System.out.println("INCORRECT ANSWERS:");
		incorrectAnswers.forEach(System.out::println);
	}
}

package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.question.VariableQuestion;

public interface VariableQuestionRepository extends JpaRepository<VariableQuestion, Long> {
}

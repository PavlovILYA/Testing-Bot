package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;

public interface VariableAnswerRepository extends JpaRepository<VariableAnswer, Long> {
}

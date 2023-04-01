package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.answer.VariableAnswer;

import java.util.Optional;

public interface VariableAnswerRepository extends JpaRepository<VariableAnswer, Long> {
    Optional<VariableAnswer> findByText(String text);
}

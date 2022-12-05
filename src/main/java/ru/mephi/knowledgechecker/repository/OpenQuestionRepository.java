package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.question.OpenQuestion;

public interface OpenQuestionRepository extends JpaRepository<OpenQuestion, Long> {
}

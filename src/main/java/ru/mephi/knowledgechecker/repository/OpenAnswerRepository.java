package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;

public interface OpenAnswerRepository extends JpaRepository<OpenAnswer, Long> {
}

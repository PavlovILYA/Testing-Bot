package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.answer.OpenAnswer;
import ru.mephi.knowledgechecker.model.answer.OpenAnswerKey;

public interface OpenAnswerRepository extends JpaRepository<OpenAnswer, OpenAnswerKey> {
    void deleteById(OpenAnswerKey id);
}

package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.test.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
}

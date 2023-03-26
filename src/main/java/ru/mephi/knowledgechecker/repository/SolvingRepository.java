package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.test.VisibilityType;

public interface SolvingRepository extends JpaRepository<Solving, Long> {
    Solving findByUserIdAndTestIdAndVisibility(Long userId, Long testId, VisibilityType visibility);
}

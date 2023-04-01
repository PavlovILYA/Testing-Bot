package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mephi.knowledgechecker.model.solving.Solving;
import ru.mephi.knowledgechecker.model.test.VisibilityType;

public interface SolvingRepository extends JpaRepository<Solving, Long> {
    Solving findByUserIdAndTestIdAndVisibility(Long userId, Long testId, VisibilityType visibility);

    @Query(value = "SELECT COUNT(s) FROM Solving AS s" +
            "  WHERE s.test.id = :testId" +
            "    AND s.visibility = :visibilityType" +
            "    AND s.checked = false")
    long getUncheckedWorksCount(Long testId, VisibilityType visibilityType);
}

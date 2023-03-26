package ru.mephi.knowledgechecker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u FROM User AS u" +
            "  JOIN CourseParticipation AS cp ON u.id = cp.id.userId" +
            "  WHERE cp.id.courseId = :courseId" +
            "    AND cp.approved = :approved")
    Page<User> getParticipantsByCourseId(Long courseId, boolean approved, Pageable pageable);

    @Query(value = "SELECT s.user FROM Solving AS s" +
            "  WHERE s.test.id = :testId" +
            "    AND s.visibility = :visibility" +
            "    AND s.checked = false")
    Page<User>  findStudentsForCheck(Long testId, VisibilityType visibility, Pageable pageable);
}

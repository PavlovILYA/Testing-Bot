package ru.mephi.knowledgechecker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mephi.knowledgechecker.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u FROM User AS u" +
            "  JOIN CourseParticipation AS cp ON u.id = cp.id.userId" +
            "  WHERE cp.id.courseId = :courseId" +
            "    AND cp.approved = :approved")
    Page<User> getParticipantsByCourseId(Long courseId, boolean approved, Pageable pageable);
}

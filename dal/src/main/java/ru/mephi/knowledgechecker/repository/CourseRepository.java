package ru.mephi.knowledgechecker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mephi.knowledgechecker.model.course.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT c FROM Course AS c" +
            "  WHERE c.creator.id = :creatorId")
    Page<Course> getCreatedCourses(Long creatorId, Pageable pageable);

    @Query(value = "SELECT c FROM Course AS c" +
            "  JOIN CourseParticipation AS cp ON c.id = cp.id.courseId" +
            "  WHERE cp.id.userId = :userId" +
            "    AND cp.approved = :approved" +
            "    AND c.creator.id <> :userId")
    Page<Course> getCoursesByParticipantId(Long userId, boolean approved, Pageable pageable);

    @Query(value = "SELECT * FROM courses AS c" +
            "  WHERE c.creator_id <> :userId AND " +
            "        regexp_match(c.title, :keyWords)", nativeQuery = true)
    Page<Course> getCoursesByKeyWords(String keyWords, Long userId, Pageable pageable);
}

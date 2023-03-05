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
}

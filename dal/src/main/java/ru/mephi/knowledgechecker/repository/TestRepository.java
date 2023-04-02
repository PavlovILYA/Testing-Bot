package ru.mephi.knowledgechecker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.VisibilityType;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {
    Test findByUniqueTitle(String uniqueTitle);

    Optional<Test> findByIdAndCreatorId(Long testId, Long creatorId);

    @Query(value = "SELECT t.unique_title FROM tests AS t" +
            "  WHERE t.creator_id <> :userId AND " +
            "        regexp_match(t.unique_title, :keyWords)", nativeQuery = true)
    Page<String> getTestsByKeyWords(String keyWords, Long userId, Pageable pageable);

    @Query(value = "SELECT t.unique_title FROM tests AS t" +
            "  WHERE t.creator_id = :userId AND t.test_type LIKE 'PUBLIC'", nativeQuery = true)
    Page<String> getCreatedPublicTests(Long userId, Pageable pageable);

    @Query(value = "SELECT t.uniqueTitle FROM Test AS t" +
            "  WHERE t.course = :course")
    Page<String> getTestsByCourse(Course course, Pageable pageable);

    @Query(value = "SELECT t.uniqueTitle FROM Test AS t" +
            "  WHERE t.course = :course" +
            "  AND t.visibility = :visibility")
    Page<String> getTestsByCourseAndVisibility(Course course, VisibilityType visibility, Pageable pageable);

    @Query(value = "SELECT COUNT(t) FROM Test AS t" +
            "  WHERE t.course = :course" +
            "  AND t.visibility = :visibility")
    long getTestCount(Course course, VisibilityType visibility);
}

package ru.mephi.knowledgechecker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mephi.knowledgechecker.model.test.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
    Test findByUniqueTitle(String uniqueTitle);

    @Query(value = "SELECT t.unique_title FROM tests AS t" +
            "  WHERE t.creator_id <> :userId AND " +
            "        regexp_match(t.unique_title, :keyWords)", nativeQuery = true)
    Page<String> getTestsByKeyWords(String keyWords, Long userId, Pageable pageable);

    @Query(value = "SELECT t.unique_title FROM tests AS t" +
            "  WHERE t.creator_id = :userId", nativeQuery = true)
    Page<String> getCreatedTests(Long userId, Pageable pageable);
}

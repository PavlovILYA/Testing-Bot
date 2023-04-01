package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.user.CurrentData;

public interface CurrentDataRepository extends JpaRepository<CurrentData, Long> {
    CurrentData findByUserId(Long userId);
}

package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.solving.Solving;

public interface SolvingRepository extends JpaRepository<Solving, Long> {
    Solving findByUserId(Long userId);
}

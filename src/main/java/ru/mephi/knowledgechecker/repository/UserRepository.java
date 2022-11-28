package ru.mephi.knowledgechecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.knowledgechecker.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

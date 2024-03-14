package com.online.bookstore.repository;

import com.online.bookstore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(final String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(final String email);
}

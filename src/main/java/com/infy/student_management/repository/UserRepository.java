package com.infy.student_management.repository;

import com.infy.student_management.entity.Role;
import com.infy.student_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByRole(Role role);

    Optional<User> findByEmail(String email);
}

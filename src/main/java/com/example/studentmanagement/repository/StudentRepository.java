package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data generates the SQL for these methods from their names.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    Optional<Student> findByEmail(String email);
}

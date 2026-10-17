package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.exception.DuplicateEmailException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

/**
 * Business rules live here: duplicate-email checks, "not found" handling, and updates.
 */
@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(StudentRequest request) {
        String email = normalizeEmail(request.email());
        if (repository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
        Student student = new Student(request.firstName().trim(), request.lastName().trim(),
                email, request.course().trim());
        return repository.save(student);
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student update(Long id, StudentRequest request) {
        Student student = findById(id);
        String email = normalizeEmail(request.email());
        // The email may only clash with a DIFFERENT student (keeping your own email is fine).
        boolean emailTakenByOther = repository.findByEmail(email)
                .map(existing -> !existing.getId().equals(id))
                .orElse(false);
        if (emailTakenByOther) {
            throw new DuplicateEmailException(email);
        }
        student.setFirstName(request.firstName().trim());
        student.setLastName(request.lastName().trim());
        student.setEmail(email);
        student.setCourse(request.course().trim());
        return repository.save(student);
    }

    public void delete(Long id) {
        Student student = findById(id);
        repository.delete(student);
    }

    /** Emails are stored lower-case so "A@x.com" and "a@x.com" count as duplicates. */
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

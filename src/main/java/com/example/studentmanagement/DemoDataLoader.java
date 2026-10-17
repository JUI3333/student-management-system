package com.example.studentmanagement;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Adds two sample students, but ONLY when the "demo" profile is active, so tests stay clean.
 * Start with: --spring.profiles.active=demo
 */
@Component
@Profile("demo")
public class DemoDataLoader implements CommandLineRunner {

    private final StudentRepository repository;

    public DemoDataLoader(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        repository.save(new Student("Asha", "Patil", "asha.patil@example.com", "Information Technology"));
        repository.save(new Student("Rohan", "Kulkarni", "rohan.kulkarni@example.com", "Computer Engineering"));
    }
}

package com.example.studentmanagement.controller;
import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * HTTP layer: receives requests, calls the service, and chooses the status code.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) {
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        Student saved = service.create(request);
        URI location = URI.create("/api/students/" + saved.getId());
        return ResponseEntity.created(location).body(StudentResponse.from(saved));
    }
    @GetMapping
    public List<StudentResponse> getAll() {
        return service.findAll().stream().map(StudentResponse::from).toList();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return StudentResponse.from(service.findById(id));
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return StudentResponse.from(service.update(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.studentmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.exception.DuplicateEmailException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests: the repository is a Mockito fake, so no database or Spring is needed.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    private StudentRequest request(String email) {
        return new StudentRequest("Asha", "Patil", email, "Information Technology");
    }

    @Test
    void createSavesStudentWithNormalizedEmail() {
        when(repository.existsByEmail("asha@example.com")).thenReturn(false);
        when(repository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = service.create(request("  Asha@Example.com "));

        assertThat(saved.getEmail()).isEqualTo("asha@example.com");
        assertThat(saved.getFirstName()).isEqualTo("Asha");
    }

    @Test
    void createRejectsDuplicateEmail() {
        when(repository.existsByEmail("asha@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request("asha@example.com")))
                .isInstanceOf(DuplicateEmailException.class);
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void findAllReturnsEveryStudent() {
        List<Student> students = List.of(
                new Student("A", "B", "a@example.com", "IT"),
                new Student("C", "D", "c@example.com", "CS"));
        when(repository.findAll()).thenReturn(students);

        assertThat(service.findAll()).hasSize(2);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L)).isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void updateChangesFields() {
        Student existing = new Student("Old", "Name", "old@example.com", "Old Course");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.findByEmail("asha@example.com")).thenReturn(Optional.empty());
        when(repository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = service.update(1L, request("asha@example.com"));

        assertThat(updated.getFirstName()).isEqualTo("Asha");
        assertThat(updated.getEmail()).isEqualTo("asha@example.com");
        assertThat(updated.getCourse()).isEqualTo("Information Technology");
    }

    @Test
    void updateRejectsEmailUsedByAnotherStudent() {
        Student existing = new Student("Old", "Name", "old@example.com", "IT");
        Student other = new Student("Other", "Person", "asha@example.com", "IT");
        setId(existing, 1L);
        setId(other, 2L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.findByEmail("asha@example.com")).thenReturn(Optional.of(other));

        assertThatThrownBy(() -> service.update(1L, request("asha@example.com")))
                .isInstanceOf(DuplicateEmailException.class);
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void updateAllowsKeepingOwnEmail() {
        Student existing = new Student("Old", "Name", "asha@example.com", "IT");
        setId(existing, 1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.findByEmail("asha@example.com")).thenReturn(Optional.of(existing));
        when(repository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = service.update(1L, request("asha@example.com"));

        assertThat(updated.getFirstName()).isEqualTo("Asha");
    }

    @Test
    void updateThrowsWhenStudentMissing() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(5L, request("asha@example.com")))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void deleteRemovesExistingStudent() {
        Student existing = new Student("A", "B", "a@example.com", "IT");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(repository).delete(existing);
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(7L)).isInstanceOf(StudentNotFoundException.class);
        verify(repository, never()).delete(any(Student.class));
    }

    /** The id is normally set by the database, so tests set it with reflection. */
    private void setId(Student student, Long id) {
        try {
            var field = Student.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(student, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}

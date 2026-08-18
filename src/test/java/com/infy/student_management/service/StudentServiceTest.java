package com.infy.student_management.service;

import com.infy.student_management.dto.StudentDto;
import com.infy.student_management.entity.Student;
import com.infy.student_management.exception.StudentAlreadyExistsException;
import com.infy.student_management.exception.StudentNotFoundException;
import com.infy.student_management.repository.StudentRepository;
import com.infy.student_management.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Captor
    private ArgumentCaptor<Student> studentCaptor;

    @Test
    @DisplayName("Should return all students")
    void shouldReturnAllStudents(){

        Student student1 = new Student();
        Student student2 = new Student();

        List<Student> students = List.of(student1,student2);

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(students,result);

        verify(studentRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no students exist")
    void shouldReturnEmptyListWhenNoStudentExists(){

        when(studentRepository.findAll()).thenReturn(List.of());

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(studentRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("Should return student with given email")
    void shouldReturnStudentByEmail(){

        String email = "anurag@test.com";
        Student student = new Student();
        student.setId(1);
        student.setEmail(email);
        student.setName("Anurag Singh");
        student.setAge("27");

        when(studentRepository.findByEmail(email))
                .thenReturn(Optional.of(student));

        StudentDto result = studentService.getStudent(email);

        assertNotNull(result);
        assertEquals("Anurag Singh",result.getName());
        assertEquals("27",result.getAge());
        assertEquals(email,result.getEmail());

        verify(studentRepository,times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw exception when student not found with email")
    void shouldThrowExceptionWhenStudentNotFoundByEmail(){

        String email = "unkown@test.com";

        when(studentRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception = assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudent(email)
        );

        assertEquals(
                "Student does not exists with email: "+email,
                exception.getMessage()
        );

        verify(studentRepository,times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should create student successfully when email id is not present")
    void shouldCreateStudentSuccessfully(){

        StudentDto studentDto = new StudentDto();
        studentDto.setName("Anurag");
        studentDto.setEmail("anurag@test.com");
        studentDto.setAge("27");

        when(studentRepository.findByEmail(studentDto.getEmail()))
                .thenReturn(Optional.empty());

        studentService.createStudent(studentDto);

        verify(studentRepository,times(1))
                .findByEmail(studentDto.getEmail());
        verify(studentRepository,times(1))
                .save(studentCaptor.capture());

        Student savedStudent = studentCaptor.getValue();
        assertNotNull(savedStudent);
        assertEquals("Anurag",savedStudent.getName());
        assertEquals("anurag@test.com",savedStudent.getEmail());
        assertEquals("27",savedStudent.getAge());
    }

    @Test
    @DisplayName("Should throw exception when student already exists")
    void shouldThrowExceptionWhenStudentAlreadyExists(){

        StudentDto studentDto = new StudentDto();
        studentDto.setName("Anurag");
        studentDto.setEmail("anurag@test.com");
        studentDto.setAge("27");

        Student existingStudent = new Student();
        existingStudent.setId(1);
        existingStudent.setName(studentDto.getName());
        existingStudent.setEmail(studentDto.getEmail());
        existingStudent.setAge(studentDto.getAge());

        when(studentRepository.findByEmail(studentDto.getEmail()))
                .thenReturn(Optional.of(existingStudent));

        StudentAlreadyExistsException exception = assertThrows(
                StudentAlreadyExistsException.class,
                ()-> studentService.createStudent(studentDto)
        );
        assertEquals(
                "Student already exists with mail id: "+studentDto.getEmail(),
                exception.getMessage()
        );

        verify(studentRepository,times(1))
                .findByEmail(studentDto.getEmail());

        verify(studentRepository,never())
                .save(any(Student.class));
    }

    @Test
    @DisplayName("Should update the student successfully")
    void shouldUpdateStudentSuccessfully(){

        StudentDto studentDto = new StudentDto();
        studentDto.setName("Anurag Singh");
        studentDto.setEmail("anurag@test.com");
        studentDto.setAge("27");

        Student studentToUpdate = new Student();
        studentToUpdate.setId(1);
        studentToUpdate.setName("Anurag");
        studentToUpdate.setEmail("anurag@test.com");
        studentToUpdate.setAge("27");

        when(studentRepository.findByEmail(studentDto.getEmail()))
                .thenReturn(Optional.of(studentToUpdate));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(studentToUpdate);

        boolean updateStatus = studentService.updateStudent(studentDto);

        assertTrue(updateStatus);

        verify(studentRepository,times(1))
                .findByEmail(studentDto.getEmail());
        verify(studentRepository,times(1))
                .save(studentCaptor.capture());

        Student updatedStudent = studentCaptor.getValue();
        assertNotNull(updatedStudent);
        assertEquals(1,updatedStudent.getId());
        assertEquals("Anurag Singh",updatedStudent.getName());
        assertEquals("anurag@test.com",updatedStudent.getEmail());
        assertEquals("27",updatedStudent.getAge());
    }

    @Test
    @DisplayName("Should throw exception when trying to update non existing student")
    void shouldThrowExceptionWhenUpdatingNonExistingStudent(){
        StudentDto studentDto = new StudentDto();
        studentDto.setName("Anurag");
        studentDto.setEmail("invalid@test.com");
        studentDto.setAge("25");

        when(studentRepository.findByEmail(studentDto.getEmail()))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception = assertThrows(
                StudentNotFoundException.class,
                () -> studentService.updateStudent(studentDto)
        );

        assertEquals("Student does not exists with email: "+studentDto.getEmail(),
                exception.getMessage());
        verify(studentRepository,times(1))
                .findByEmail(studentDto.getEmail());
        verify(studentRepository,never())
                .save(any(Student.class));
    }

    @Test
    @DisplayName("Should delete student successfully")
    void shouldDeleteStudentSuccessfully(){

        String email = "anurag@test.com";

        Student student = new Student();
        student.setId(1);
        student.setName("Anurag");
        student.setEmail("anurag@test.com");
        student.setAge("27");

        when(studentRepository.findByEmail(email))
                .thenReturn(Optional.of(student));

        boolean result = studentService.deleteStudent(email);

        assertTrue(result);

        verify(studentRepository,times(1))
                .findByEmail(email);
        verify(studentRepository,times(1))
                .deleteById(student.getId());
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existing student")
    void shouldThrowExceptionWhenDeletingNonExistingStudent(){

        String email = "unkown@test.com";

        when(studentRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception = assertThrows(
                StudentNotFoundException.class,
                () -> studentService.deleteStudent(email)
        );

        assertEquals(
                "Student does not exists with email: "+email,
                exception.getMessage()
        );

        verify(studentRepository,times(1))
                .findByEmail(email);
        verify(studentRepository,never())
                .deleteById(anyInt());
    }
}

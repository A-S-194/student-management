package com.infy.student_management.service.impl;

import com.infy.student_management.dto.StudentDto;
import com.infy.student_management.entity.Student;
import com.infy.student_management.exception.StudentAlreadyExistsException;
import com.infy.student_management.exception.StudentNotFoundException;
import com.infy.student_management.mapper.StudentMapper;
import com.infy.student_management.repository.StudentRepository;
import com.infy.student_management.service.IStudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements IStudentService {

    private StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void createStudent(StudentDto studentDto) {
        Student student = StudentMapper.mapToStudent(studentDto, new Student());
        Optional<Student> optionalStudent = studentRepository.findByEmail(studentDto.getEmail());
        if (optionalStudent.isPresent()) {
            throw new StudentAlreadyExistsException("Student already exists with mail id: " + studentDto.getEmail());
        }
        studentRepository.save(student);
    }

    @Override
    public StudentDto getStudent(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(
                () ->new StudentNotFoundException("Student does not exists with email: "+email)
        );
        return StudentMapper.mapToStudentDto(student, new StudentDto());
    }

    @Override
    public boolean updateStudent(StudentDto studentDto) {
        Student student = studentRepository.findByEmail(studentDto.getEmail()).orElseThrow(
                () ->new StudentNotFoundException("Student does not exists with email: "+studentDto.getEmail())
        );
        StudentMapper.mapToStudent(studentDto,student);
        student = studentRepository.save(student);
        return true;
    }

    @Override
    public boolean deleteStudent(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(
                () ->new StudentNotFoundException("Student does not exists with email: "+email)
        );
        studentRepository.deleteById(student.getId());
        return true;
    }

}

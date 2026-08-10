package com.infy.student_management.service;

import com.infy.student_management.dto.StudentDto;
import com.infy.student_management.entity.Student;

import java.util.List;

public interface IStudentService {

    List<Student> getAllStudents();

    void createStudent(StudentDto studentDto);

    StudentDto getStudent(String email);

    boolean updateStudent(StudentDto studentDto);

    boolean deleteStudent(String email);
}

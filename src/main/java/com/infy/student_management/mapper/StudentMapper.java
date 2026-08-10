package com.infy.student_management.mapper;

import com.infy.student_management.dto.StudentDto;
import com.infy.student_management.entity.Student;

public class StudentMapper {

    public static StudentDto mapToStudentDto(Student student, StudentDto studentDto) {
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());
        studentDto.setAge(student.getAge());
        return studentDto;
    }

    public static Student mapToStudent(StudentDto studentDto, Student student) {
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setAge(studentDto.getAge());
        return student;
    }
}

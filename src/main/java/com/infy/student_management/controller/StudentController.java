package com.infy.student_management.controller;

import com.infy.student_management.constants.StudentConstants;
import com.infy.student_management.dto.ResponseDto;
import com.infy.student_management.dto.StudentDto;
import com.infy.student_management.entity.Student;
import com.infy.student_management.service.IStudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class StudentController {

    private IStudentService iStudentService;

    @GetMapping("/student")
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(iStudentService.getAllStudents(), HttpStatus.OK);

    }

    @CrossOrigin
    @PostMapping("/student")
    public ResponseEntity<ResponseDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        iStudentService.createStudent(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(StudentConstants.STATUS_201, StudentConstants.MESSAGE_201));
    }

    @GetMapping(value = "/student", params = "email")
    public ResponseEntity<StudentDto> getStudent(@RequestParam
                                                 @Email(message = "Enter valid email id")
                                                 String email) {
        StudentDto studentDto = iStudentService.getStudent(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(studentDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateStudent(@Valid @RequestBody StudentDto studentDto) {
        boolean isUpdated = iStudentService.updateStudent(studentDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(StudentConstants.STATUS_200, StudentConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(StudentConstants.STATUS_417, StudentConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteStudent(@RequestParam
                                                     @Email(message = "Email id is invalid")
                                                     String email) {
        boolean isDeleted = iStudentService.deleteStudent(email);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(StudentConstants.STATUS_200, StudentConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(StudentConstants.STATUS_417, StudentConstants.MESSAGE_417_DELETE));
        }
    }
}

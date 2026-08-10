package com.infy.student_management.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentDto {


    @NotBlank(message = "Student name cannot be empty or null")
    @Size(min = 5, max = 30, message = "The length of student name should be between 5 and 30")
    private String name;

    @NotBlank(message = "Email address cannot be null or empty")
    @Email(message = "Provide valid email address")
    private String email;

    @NotBlank(message = "Age cannot be null or empty")
    @Min(value = 18, message = "The minimum age is 18 years")
    @Max(value = 35, message = "The maximum age is 35 years")
    private String age;
}

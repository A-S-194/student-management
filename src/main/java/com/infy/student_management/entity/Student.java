package com.infy.student_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String age;
}

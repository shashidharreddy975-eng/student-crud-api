package com.softmason.studentcrud.repository;

import com.softmason.studentcrud.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
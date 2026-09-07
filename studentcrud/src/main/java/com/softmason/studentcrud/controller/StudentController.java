package com.softmason.studentcrud.controller;

import com.softmason.studentcrud.entity.Student;
import com.softmason.studentcrud.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CREATE STUDENT
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // GET ALL STUDENTS WITH PAGINATION AND SORTING
    @GetMapping
    public Map<String, Object> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Student> studentPage = studentRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("students", studentPage.getContent());
        response.put("currentPage", studentPage.getNumber());
        response.put("totalItems", studentPage.getTotalElements());
        response.put("totalPages", studentPage.getTotalPages());
        response.put("pageSize", studentPage.getSize());

        return response;
    }

    // GET STUDENT BY ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // UPDATE STUDENT
    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Long id,
            @RequestBody Student student
    ) {

        Student existingStudent =
                studentRepository.findById(id).orElse(null);

        if (existingStudent != null) {
            existingStudent.setName(student.getName());
            existingStudent.setEmail(student.getEmail());

            return studentRepository.save(existingStudent);
        }

        return null;
    }

    // DELETE STUDENT
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {

        studentRepository.deleteById(id);

        return "Student deleted successfully";
    }
}


package com.softmason.studentcrud.controller;

import com.softmason.studentcrud.dto.StudentDTO;
import com.softmason.studentcrud.entity.Student;
import com.softmason.studentcrud.repository.StudentRepository;
import com.softmason.studentcrud.response.ResponseGlobal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ResponseGlobal> createStudent(
            @Valid @RequestBody StudentDTO studentDTO) {

        Student student = new Student();

        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());

        Student savedStudent = studentRepository.save(student);

        StudentDTO responseDTO = new StudentDTO(
                savedStudent.getId(),
                savedStudent.getName(),
                savedStudent.getEmail()
        );

        return ResponseEntity.ok(
                new ResponseGlobal(
                        true,
                        "Student created successfully",
                        responseDTO
                )
        );
    }

    // GET ALL - Pagination + Sorting
    @GetMapping
    public ResponseEntity<ResponseGlobal> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Student> studentPage = studentRepository.findAll(pageable);

        List<StudentDTO> students = studentPage.getContent()
                .stream()
                .map(student -> new StudentDTO(
                        student.getId(),
                        student.getName(),
                        student.getEmail()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ResponseGlobal(
                        true,
                        "Students fetched successfully",
                        students
                )
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseGlobal> getStudentById(
            @PathVariable Long id) {

        Optional<Student> student = studentRepository.findById(id);

        if (student.isPresent()) {

            Student s = student.get();

            StudentDTO responseDTO = new StudentDTO(
                    s.getId(),
                    s.getName(),
                    s.getEmail()
            );

            return ResponseEntity.ok(
                    new ResponseGlobal(
                            true,
                            "Student found successfully",
                            responseDTO
                    )
            );
        }

        return ResponseEntity.ok(
                new ResponseGlobal(
                        false,
                        "Student not found",
                        null
                )
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponseGlobal> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDTO studentDTO) {

        Optional<Student> optionalStudent =
                studentRepository.findById(id);

        if (optionalStudent.isPresent()) {

            Student student = optionalStudent.get();

            student.setName(studentDTO.getName());
            student.setEmail(studentDTO.getEmail());

            Student updatedStudent =
                    studentRepository.save(student);

            StudentDTO responseDTO = new StudentDTO(
                    updatedStudent.getId(),
                    updatedStudent.getName(),
                    updatedStudent.getEmail()
            );

            return ResponseEntity.ok(
                    new ResponseGlobal(
                            true,
                            "Student updated successfully",
                            responseDTO
                    )
            );
        }

        return ResponseEntity.ok(
                new ResponseGlobal(
                        false,
                        "Student not found",
                        null
                )
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseGlobal> deleteStudent(
            @PathVariable Long id) {

        Optional<Student> student =
                studentRepository.findById(id);

        if (student.isPresent()) {

            studentRepository.deleteById(id);

            return ResponseEntity.ok(
                    new ResponseGlobal(
                            true,
                            "Student deleted successfully",
                            null
                    )
            );
        }

        return ResponseEntity.ok(
                new ResponseGlobal(
                        false,
                        "Student not found",
                        null
                )
        );
    }
}
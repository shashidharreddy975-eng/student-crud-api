package
com.softmason.studentcrud.service;

import
        com.softmason.studentcrud.repository.StudentRepository;

import
        org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final
    StudentRepository
    studentRepository;

    public
    StudentService(com.softmason.studentcrud.repository.StudentRepository
                   studentRepository) {
        this.studentRepository =
                studentRepository;
    }

}


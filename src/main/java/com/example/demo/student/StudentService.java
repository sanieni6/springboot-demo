package com.example.demo.student;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import jakarta.transaction.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


	public List<Student> getStudents() {
		return studentRepository.findAll();
	}

    public void addNewStudent(Student student) {

        Optional<Student> StudentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(StudentOptional.isPresent()){
            throw new IllegalStateException("email taken");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException("student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(
        Long studentId,
        String name,
        String email
    ) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException(
            "student with id " + studentId + " does not exists"
        ));

        if(name != null && name.length() > 0 && !name.equals(student.getName())){
            student.setName(name);
        }

        if(email != null && email.length() > 0 && !email.equals(student.getEmail())){
            Optional<Student> StudentOptional = studentRepository.findStudentByEmail(email);
            if(StudentOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }
}

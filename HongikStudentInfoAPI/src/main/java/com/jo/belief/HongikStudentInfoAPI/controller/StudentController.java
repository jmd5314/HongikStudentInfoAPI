package com.jo.belief.HongikStudentInfoAPI.controller;

import com.jo.belief.HongikStudentInfoAPI.entity.Student;
import com.jo.belief.HongikStudentInfoAPI.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/degree")
    public ResponseEntity<String> getDegreeByName(@RequestParam String name){
        List<Student> students = studentRepository.findByName(name);
        if (students.isEmpty()) {
            return ResponseEntity.status(404).body("No such student");
        } else if (students.size() > 1) {
            return ResponseEntity.status(400).body("There are multiple students with the same name. Please provide an email address instead.");
        }
        return ResponseEntity.ok(students.get(0).getName() + " : " + students.get(0).getDegree());
    }

    @GetMapping("/email")
    public ResponseEntity<String> getEmailByName(@RequestParam String name) {
        List<Student> students = studentRepository.findByEmail(name);
        if (students.isEmpty()) {
            return ResponseEntity.status(404).body("No such student");
        } else if (students.size() > 1) {
            return ResponseEntity.status(400).body("There are multiple students with the same name. Please provide an email address instead.");
        }
        return ResponseEntity.ok(students.get(0).getName()+":"+students.get(0).getEmail());
    }

    @GetMapping("/stat")
    public ResponseEntity<String> getStudentCountByDegree(@RequestParam String degree) {
        List<Student> students = studentRepository.findByDegree(degree);
        return ResponseEntity.ok("Number of "+degree+"student :"+students.size());
    }

    @PutMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestParam String name, @RequestParam String email, @RequestParam Integer graduation) {
        List<Student> existingStudents = studentRepository.findByName(name);
        if (!existingStudents.isEmpty()) {
            return ResponseEntity.status(400).body("Already registered");
        }
        Student newStudent = new Student();
        newStudent.setName(name);
        newStudent.setEmail(email);
        newStudent.setGraduation(graduation);
        studentRepository.save(newStudent);

        return ResponseEntity.ok("Registration successful");
    }
}

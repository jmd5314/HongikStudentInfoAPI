package com.jo.belief.HongikStudentInfoAPI.controller;

import com.jo.belief.HongikStudentInfoAPI.Service.StudentService;
import com.jo.belief.HongikStudentInfoAPI.entity.Student;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @PostConstruct
    public void init(){
        studentService.crawlAndLoad();
    }
    @PreDestroy
    public void end(){
        studentService.close();
    }
    @GetMapping("/degree")
    public ResponseEntity<String> getDegreeByName(@RequestParam String name){
        List<Student> students = studentService.findByName(name);
        if (students.isEmpty()) {
            return ResponseEntity.status(404).body("No such student");
        } else if (students.size() > 1) {
            return ResponseEntity.status(400).body("There are multiple students with the same name. Please provide an email address instead.");
        }
        return ResponseEntity.ok(students.get(0).getName() + " : " + students.get(0).getDegree());
    }

    @GetMapping("/email")
    public ResponseEntity<String> getEmailByName(@RequestParam String name) {
        List<Student> students = studentService.findByEmail(name);
        if (students.isEmpty()) {
            return ResponseEntity.status(404).body("No such student");
        } else if (students.size() > 1) {
            return ResponseEntity.status(400).body("There are multiple students with the same name. Please provide an email address instead.");
        }
        return ResponseEntity.ok(students.get(0).getName()+":"+students.get(0).getEmail());
    }

    @GetMapping("/stat")
    public ResponseEntity<String> getStudentCountByDegree(@RequestParam String degree) {
        List<Student> students = studentService.findByDegree(degree);
        return ResponseEntity.ok("Number of "+degree+"student :"+students.size());
    }

    @PutMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestParam String name, @RequestParam String email, @RequestParam Integer graduation) {
        List<Student> existingStudents = studentService.findByName(name);
        if (!existingStudents.isEmpty()) {
            return ResponseEntity.status(400).body("Already registered");
        }
        Student newStudent = new Student();
        newStudent.setName(name);
        newStudent.setEmail(email);
        newStudent.setGraduation(graduation);
        studentService.save(newStudent);

        return ResponseEntity.ok("Registration successful");
    }
}

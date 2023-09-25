package com.jo.belief.HongikStudentInfoAPI.Service;

import com.jo.belief.HongikStudentInfoAPI.entity.Student;
import com.jo.belief.HongikStudentInfoAPI.repository.StudentRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Transactional
    public void crawlAndLoad() {
        try {
            // 웹 페이지에서 학생 정보 크롤링
            Document document = Jsoup.connect("https://apl.hongik.ac.kr/lecture/dbms").get();
            Element studentList = document.select("ul.student-list").first();

            // 각 학생 정보를 데이터베이스에 저장
            for (Element studentElement : studentList.children()) {
                String name = studentElement.select("li.student-name").text();
                String email = studentElement.select("li.student-email").text();
                String degree = studentElement.select("li.student-degree").text();
                int graduation = Integer.parseInt(studentElement.select("li.student-graduation").text());

                // Student 엔티티에 저장
                Student student = new Student();
                student.setName(name);
                student.setEmail(email);
                student.setDegree(degree);
                student.setGraduation(graduation);

                studentRepository.save(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public void save(Student student){
        studentRepository.save(student);
    }
    public List<Student> findByName(String name){
        return studentRepository.findByName(name);
    }
    public List<Student> findByEmail(String email){
        return studentRepository.findByEmail(email);
    }
    public List<Student> findByDegree(String degree){
        return studentRepository.findByDegree(degree);
    }
    @Transactional
    public void close(){
        studentRepository.close();
    }
}

package com.jo.belief.HongikStudentInfoAPI.Service;

import com.jo.belief.HongikStudentInfoAPI.entity.Student;
import com.jo.belief.HongikStudentInfoAPI.repository.StudentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@EnableScheduling
@Transactional(readOnly = true)
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void crawlAndLoad() {
        try {
            String url = "https://apl.hongik.ac.kr/lecture/dbms";
            Document document = Jsoup.connect(url).get();

            processStudents(document, "PhD Students", "Phd");
            processStudents(document, "Master Students", "Master");
            processStudents(document, "Undergraduate Students", "Undergraduate");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void processStudents(Document document, String degreeTitle, String degree) {
        Elements students = document.select("h2:contains(" + degreeTitle + ") + ul li");
        for (Element studentElement : students) {
            String[] studentInfo = studentElement.text().split(", ");
                Student student = new Student();
                student.setDegree(degree);
                student.setName(studentInfo[0]);
                student.setEmail(studentInfo[1]);
                student.setGraduation(Integer.valueOf(studentInfo[2]));
                studentRepository.save(student);
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

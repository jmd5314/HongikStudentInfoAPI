package com.jo.belief.HongikStudentInfoAPI.repository;

import com.jo.belief.HongikStudentInfoAPI.entity.Student;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepository {
    private final EntityManager em;
    public List<Student> findByName(String name){
        return em.createQuery("select s form Student s",)
    };
    public List<Student> findByEmail(String email){

    };
    public List<Student> findByDegree(String degree){

    };
}
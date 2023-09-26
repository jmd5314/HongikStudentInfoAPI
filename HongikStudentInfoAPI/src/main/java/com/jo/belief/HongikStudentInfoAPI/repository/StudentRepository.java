package com.jo.belief.HongikStudentInfoAPI.repository;

import com.jo.belief.HongikStudentInfoAPI.entity.Student;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepository {
    private final EntityManager em;
    public StudentRepository(EntityManager em) {
        this.em = em;
    }
    public void deleteAll() {
        em.createQuery("DELETE FROM Student").executeUpdate();
    }
    public void save(Student student){
        em.persist(student);
    }
    public List<Student> findByName(String name){
        return em.createQuery("select s from Student s where s.name = :name",Student.class)
                .setParameter("name", name)
                .getResultList();
    }
    public List<Student> findByEmail(String email){
        return em.createQuery("select s from Student s where s.email = :email",Student.class)
                .setParameter("email", email)
                .getResultList();
    }
    public List<Student> findByDegree(String degree){
        return em.createQuery("select s from Student s where s.degree = :degree",Student.class)
                .setParameter("degree", degree)
                .getResultList();
    }
    public void close() {
        em.close();
    }
}
package com.StudyBuddy.Student.repository;

import com.StudyBuddy.Student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface StudentRepository extends JpaRepository<Student, Long> {
}
package com.StudyBuddy.Student.repository;

import com.StudyBuddy.Student.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    boolean existsByStudentIdAndStudyGroupId(Long studentId, Long studyGroupId);

    long countByStudyGroupId(Long studyGroupId);

    void deleteByStudentIdAndStudyGroupId(Long studentId, Long studyGroupId);

    List<Membership> findByStudyGroupId(Long studyGroupId);
}
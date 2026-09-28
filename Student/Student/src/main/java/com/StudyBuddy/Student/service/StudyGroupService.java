package com.StudyBuddy.Student.service;

import com.StudyBuddy.Student.entity.Membership;
import com.StudyBuddy.Student.entity.StudyGroup;
import com.StudyBuddy.Student.entity.Student;
import com.StudyBuddy.Student.entity.Subject;
import com.StudyBuddy.Student.repository.MembershipRepository;
import com.StudyBuddy.Student.repository.StudyGroupRepository;
import com.StudyBuddy.Student.repository.StudentRepository;
import com.StudyBuddy.Student.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MembershipRepository membershipRepository;

    public StudyGroupService(
            StudyGroupRepository studyGroupRepository,
            StudentRepository studentRepository,
            SubjectRepository subjectRepository,
            MembershipRepository membershipRepository) {

        this.studyGroupRepository = studyGroupRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.membershipRepository = membershipRepository;
    }

    public StudyGroup createGroup(
            String name,
            int maxMembers,
            Long subjectId,
            Long creatorId) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Student creator = studentRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudyGroup group = new StudyGroup();

        group.setName(name);
        group.setMaxMembers(maxMembers);
        group.setSubject(subject);
        group.setCreator(creator);

        StudyGroup savedGroup = studyGroupRepository.save(group);

        Membership membership = new Membership();
        membership.setStudent(creator);
        membership.setStudyGroup(savedGroup);

        membershipRepository.save(membership);

        return savedGroup;
    }

    public List<StudyGroup> getAllGroups() {
        return studyGroupRepository.findAll();
    }

    public StudyGroup getGroupById(Long groupId) {

        return studyGroupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Study group not found"));
    }

    @Transactional
    public StudyGroup updateGroup(
            Long groupId,
            String name,
            int maxMembers,
            Long subjectId) {

        StudyGroup group =
                studyGroupRepository.findById(groupId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Study group not found"));

        Subject subject =
                subjectRepository.findById(subjectId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subject not found"));

        long currentMembers =
                membershipRepository.countByStudyGroupId(groupId);

        if (maxMembers < currentMembers) {

            throw new RuntimeException(
                    "Maximum members cannot be less than current members");
        }

        group.setName(name);
        group.setMaxMembers(maxMembers);
        group.setSubject(subject);

        return studyGroupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId) {

        StudyGroup group =
                studyGroupRepository.findById(groupId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Study group not found"));

        List<Membership> members =
                membershipRepository.findByStudyGroupId(groupId);

        membershipRepository.deleteAll(members);

        studyGroupRepository.delete(group);
    }

    public List<StudyGroup> getGroupsBySubject(Long subjectId) {
        return studyGroupRepository.findBySubjectId(subjectId);
    }

    public void joinGroup(Long groupId, Long studentId) {

        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Study group not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (membershipRepository.existsByStudentIdAndStudyGroupId(
                studentId, groupId)) {

            throw new RuntimeException("Student already joined this group");
        }

        long currentMembers =
                membershipRepository.countByStudyGroupId(groupId);

        if (currentMembers >= group.getMaxMembers()) {
            throw new RuntimeException("Study group is full");
        }

        Membership membership = new Membership();

        membership.setStudent(student);
        membership.setStudyGroup(group);

        membershipRepository.save(membership);
    }

    @Transactional
    public void leaveGroup(Long groupId, Long studentId) {

        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Study group not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        boolean isMember =
                membershipRepository.existsByStudentIdAndStudyGroupId(
                        studentId,
                        groupId
                );

        if (!isMember) {
            throw new RuntimeException(
                    "Student " + studentId +
                            " is not a member of study group " + groupId
            );
        }

        membershipRepository.deleteByStudentIdAndStudyGroupId(
                studentId,
                groupId
        );
    }

    public List<Membership> getMembers(Long groupId) {
        return membershipRepository.findByStudyGroupId(groupId);
    }

    public void removeMember(
            Long groupId,
            Long creatorId,
            Long studentId) {

        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Study group not found"));

        if (!group.getCreator().getId().equals(creatorId)) {
            throw new RuntimeException(
                    "Only the group creator can remove members");
        }

        if (!membershipRepository.existsByStudentIdAndStudyGroupId(
                studentId, groupId)) {

            throw new RuntimeException("Student is not a member");
        }

        membershipRepository.deleteByStudentIdAndStudyGroupId(
                studentId, groupId);
    }
}
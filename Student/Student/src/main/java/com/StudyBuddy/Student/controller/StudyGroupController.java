package com.StudyBuddy.Student.controller;

import com.StudyBuddy.Student.dto.JoinGroupRequest;
import com.StudyBuddy.Student.dto.LeaveGroupRequest;
import com.StudyBuddy.Student.dto.RemoveMemberRequest;
import com.StudyBuddy.Student.dto.StudyGroupRequest;
import com.StudyBuddy.Student.dto.StudyGroupUpdateRequest;
import com.StudyBuddy.Student.entity.Membership;
import com.StudyBuddy.Student.entity.StudyGroup;
import com.StudyBuddy.Student.service.StudyGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-groups")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    public StudyGroupController(
            StudyGroupService studyGroupService) {

        this.studyGroupService = studyGroupService;
    }

    @PostMapping("/create")
    public StudyGroup createGroup(
            @RequestBody StudyGroupRequest request) {

        return studyGroupService.createGroup(
                request.getName(),
                request.getMaxMembers(),
                request.getSubjectId(),
                request.getCreatorId()
        );
    }

    @GetMapping
    public List<StudyGroup> getAllGroups() {

        return studyGroupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public StudyGroup getGroupById(
            @PathVariable Long groupId) {

        return studyGroupService.getGroupById(groupId);
    }

    @GetMapping("/subject/{subjectId}")
    public List<StudyGroup> getGroupsBySubject(
            @PathVariable Long subjectId) {

        return studyGroupService.getGroupsBySubject(subjectId);
    }

    @PutMapping("/{groupId}")
    public StudyGroup updateGroup(
            @PathVariable Long groupId,
            @RequestBody StudyGroupUpdateRequest request) {

        return studyGroupService.updateGroup(
                groupId,
                request.getName(),
                request.getMaxMembers(),
                request.getSubjectId()
        );
    }

    @DeleteMapping("/{groupId}")
    public String deleteGroup(
            @PathVariable Long groupId) {

        studyGroupService.deleteGroup(groupId);

        return "Study group deleted successfully";
    }

    @PostMapping("/join")
    public String joinGroup(
            @RequestBody JoinGroupRequest request) {

        studyGroupService.joinGroup(
                request.getGroupId(),
                request.getStudentId()
        );

        return "Student joined the group successfully";
    }

    @DeleteMapping("/leave")
    public String leaveGroup(
            @RequestBody LeaveGroupRequest request) {

        studyGroupService.leaveGroup(
                request.getGroupId(),
                request.getStudentId()
        );

        return "Student left the group successfully";
    }

    @GetMapping("/{groupId}/members")
    public List<Membership> getMembers(
            @PathVariable Long groupId) {

        return studyGroupService.getMembers(groupId);
    }

    @DeleteMapping("/remove")
    public String removeMember(
            @RequestBody RemoveMemberRequest request) {

        studyGroupService.removeMember(
                request.getGroupId(),
                request.getCreatorId(),
                request.getStudentId()
        );

        return "Member removed successfully";
    }
}
package com.StudyBuddy.Student.controller;

import com.StudyBuddy.Student.entity.Membership;
import com.StudyBuddy.Student.repository.MembershipRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipRepository membershipRepository;

    public MembershipController(
            MembershipRepository membershipRepository) {

        this.membershipRepository = membershipRepository;
    }

    @GetMapping
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    @GetMapping("/group/{groupId}")
    public List<Membership> getGroupMembers(
            @PathVariable Long groupId) {

        return membershipRepository.findByStudyGroupId(groupId);
    }
}
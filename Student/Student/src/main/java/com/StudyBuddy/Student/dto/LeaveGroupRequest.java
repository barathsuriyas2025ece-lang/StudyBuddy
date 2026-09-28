package com.StudyBuddy.Student.dto;

public class LeaveGroupRequest {

    private Long groupId;
    private Long studentId;

    public LeaveGroupRequest() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
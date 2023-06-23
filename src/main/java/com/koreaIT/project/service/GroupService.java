package com.koreaIT.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.GroupRepository;
import com.koreaIT.project.vo.Group;

@Service
public class GroupService {
	GroupRepository groupRepository;

	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	public List<Group> getgroups() {
		return groupRepository.getgroups();
	}

	public List<Group> getGroupsByGrade(String grade) {
		return groupRepository.getGroupsByGrade(grade);
	}

	public List<Group> getGroupsByTeacherID(int groupTeacherId) {
		return groupRepository.getGroupsByTeacherID(groupTeacherId);
	}

	public Group getGroupById(int id) {
		return groupRepository.getGroupById(id);
	}

	public int getStudentNumById(int id) {
		return groupRepository.getStudentNumById(id);
	}

	public void doMakeGroup(String grade, String groupName, String groupDay, int groupTeacherId, String textbook) {
		groupRepository.doMakeGroup(grade, groupName, groupDay, groupTeacherId, textbook);
	}

	public void doDeleteGroup(int classId) {
		groupRepository.doDeleteGroup(classId);
	}



}

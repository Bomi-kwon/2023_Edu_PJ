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



}
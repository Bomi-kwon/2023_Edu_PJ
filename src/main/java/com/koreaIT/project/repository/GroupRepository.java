package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Group;

@Mapper
public interface GroupRepository {
	public List<Group> getgroups();

	public List<Group> getGroupsByGrade(String grade);
}

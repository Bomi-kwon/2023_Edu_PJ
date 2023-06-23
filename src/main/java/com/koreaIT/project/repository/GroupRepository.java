package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Group;

@Mapper
public interface GroupRepository {
	public List<Group> getgroups();

	public List<Group> getGroupsByGrade(String grade);

	public List<Group> getGroupsByTeacherID(int groupTeacherId);

	public Group getGroupById(int id);

	public int getStudentNumById(int id);

	public void doMakeGroup(String grade, String groupName, String groupDay, int groupTeacherId, String textbook);

	public void doDeleteGroup(int classId);
}

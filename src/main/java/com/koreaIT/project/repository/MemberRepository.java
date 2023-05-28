package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Member;

@Mapper
public interface MemberRepository {

	List<Member> getMembers();

	void doMemberJoin(String loginID, String loginPW, String name, String cellphoneNum, String email, int authLevel);

	Member getMemberByLoginID(String loginID);

	Member getMemberById(int id);

	List<Member> getStudentsByClass(int classId);

	void doMemberModify(int id, String name, String cellphoneNum, String email);

	void doPasswordModify(int id, String loginPW);

	void doMemberDrop(int id);

	List<Member> getMembersByAuthLevel(int authLevel);

	Member getMemberByNameAndEmail(String name, String email);

	

}

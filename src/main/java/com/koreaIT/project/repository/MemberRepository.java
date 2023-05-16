package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Member;

@Mapper
public interface MemberRepository {

	List<Member> getMembers();

	void doMemberJoin(String loginID, String loginPW, String name, String cellphoneNum, String email);

	Member getMemberByLoginID(String loginID);

	

}

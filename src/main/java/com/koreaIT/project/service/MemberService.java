package com.koreaIT.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.MemberRepository;
import com.koreaIT.project.vo.Member;

@Service
public class MemberService {
	MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public List<Member> getMembers() {
		return memberRepository.getMembers();
	}

	public void doMemberJoin(String loginID, String loginPW, String name, String cellphoneNum, String email) {
		memberRepository.doMemberJoin(loginID, loginPW, name, cellphoneNum, email);
	}

	public Member getMemberByLoginID(String loginID) {
		return memberRepository.getMemberByLoginID(loginID);
	}

	

}

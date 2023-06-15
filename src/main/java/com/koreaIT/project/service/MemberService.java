package com.koreaIT.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.koreaIT.project.repository.MemberRepository;
import com.koreaIT.project.util.Util;
import com.koreaIT.project.vo.Member;
import com.koreaIT.project.vo.MessageDTO;
import com.koreaIT.project.vo.MessageDTO.MessageDTOBuilder;
import com.koreaIT.project.vo.ResultData;
import com.koreaIT.project.vo.SmsResponseDTO;

@Service
public class MemberService {
	
	@Value("${custom.siteName}")
	private String siteName;
	@Value("${custom.siteMainUri}")
	private String siteMainUri;
	
	private MemberRepository memberRepository;
	private MailService mailService;
	private MessageService messageService;
	private CouponService couponService;
	private FileService fileService;

	public MemberService(MemberRepository memberRepository, MailService mailService, 
			MessageService messageService, CouponService couponService, FileService fileService) {
		this.memberRepository = memberRepository;
		this.mailService = mailService;
		this.messageService = messageService;
		this.couponService = couponService;
		this.fileService = fileService;
	}

	public List<Member> getMembers() {
		return memberRepository.getMembers();
	}

	public void doMemberJoin(String loginID, String loginPW, String name, String cellphoneNum, String email, int authLevel) {
		memberRepository.doMemberJoin(loginID, loginPW, name, cellphoneNum, email, authLevel);
	}
	
	public int getLastId() {
		return memberRepository.getLastId();
	}

	public Member getMemberByLoginID(String loginID) {
		return memberRepository.getMemberByLoginID(loginID);
	}

	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}

	public List<Member> getStudentsByClass(int classId) {
		return memberRepository.getStudentsByClass(classId);
	}

	public void doMemberModify(int id, String name, String cellphoneNum, String email) {
		memberRepository.doMemberModify(id, name, cellphoneNum, email);
	}

	public void doPasswordModify(int id, String loginPW) {
		memberRepository.doPasswordModify(id, loginPW);
	}

	public void doMemberDrop(int id) {
		memberRepository.doMemberDrop(id);
	}

	public List<Member> getMembersByAuthLevel(int authLevel) {
		return memberRepository.getMembersByAuthLevel(authLevel);
	}

	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);
	}

	public ResultData notifyTempLoginPwByEmail(Member member) {
		String title = "[" + siteName + "] 임시 패스워드 발송";
		String tempPassword = Util.getTempPassword(8);
		String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
		body += "<a style='font-size:2rem;' href=\"" + siteMainUri + "/project/member/memberlogin\" target=\"_blank\">로그인 하러가기</a>";

		ResultData sendRd = mailService.send(member.getEmail(), title, body);

		if (sendRd.isFail()) {
			return sendRd;
		}

		setTempPassword(member, tempPassword);

		return ResultData.from("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다");
	}
	
	private void setTempPassword(Member member, String tempPassword) {
		memberRepository.doPasswordModify(member.getId(), Util.sha256(tempPassword));
	}
	
	public ResultData notifyTempCouponByMessage(Member member, String deadLine, int studentId) throws Exception {
		String couponPassword = Util.getTempPassword(6);
		
		MessageDTO messageDTO = MessageDTO.builder()
				.to(member.getCellphoneNum())
				.content(Util.f("쿠폰 번호는 %s 입니다.", couponPassword))
				.build();
		
		SmsResponseDTO response = messageService.sendMessage(messageDTO);
		
		if(response.getStatusName().equals("success") == false) {
			return ResultData.from("F-1", "쿠폰번호 문자 전송에 실패하였습니다.");
		}
		
		couponService.doGiveCoupon(deadLine, studentId, couponPassword);
		
		return ResultData.from("S-1", Util.f("쿠폰번호를 %s 학생에게 문자로 보냈습니다.", member.getName()));
	}

	public List<Member> getMemberByClassId(int classId) {
		return memberRepository.getMemberByClassId(classId);
	}

	public List<Member> getStudentsByNameKeyWord(String keyWord) {
		return memberRepository.getStudentsByNameKeyWord(keyWord);
	}

	public void doRegister(int memberId, int classId) {
		memberRepository.doRegister(memberId, classId);
	}

	public Member getMemberByName(String name) {
		return memberRepository.getMemberByName(name);
	}

	public int getImageByMemberId(int memberId) {
		return fileService.getImageByMemberId(memberId);
	}

	

	
	

	

}

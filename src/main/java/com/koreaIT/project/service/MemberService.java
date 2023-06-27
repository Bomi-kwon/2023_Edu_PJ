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

	/**
	 * 새로운 비밀번호 멤버의 이메일로 보내기
	 * @param member 멤버 정보
	 * @return 성공 메시지 (resultdata)
	 * @todo 나중에 사이트 배포하면 yml의 siteMainUri 바꿔주기
	 */
	public ResultData notifyTempLoginPwByEmail(Member member) {
		String title = "[" + siteName + "] 임시 패스워드 발송";
		
		// 8자리 문자+숫자 조합 랜덤 문자열 발생시키기!!
		String tempPassword = Util.getTempPassword(8);
		
		// 메일 내용은 웹사이트에서 보여질 것이므로
		// html문법으로 작성하기
		String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
		body += "<a style='font-size:2rem;' href=\"" + siteMainUri + "/project/member/memberlogin\" target=\"_blank\">로그인 하러가기</a>";

		
		ResultData sendRd = mailService.send(member.getEmail(), title, body);

		if (sendRd.isFail()) {
			return sendRd;
		}

		// DB에 저장된 비밀번호도 새로운 비밀번호로 바꿔줘야함!!
		setTempPassword(member, tempPassword);

		return ResultData.from("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다");
	}
	
	private void setTempPassword(Member member, String tempPassword) {
		memberRepository.doPasswordModify(member.getId(), Util.sha256(tempPassword));
	}
	
	/**
	 * 6자리 무작위 문자조합 쿠폰번호를 학생의 핸드폰 번호로 문자보내는 기능
	 * @param member 어떤 학생에게 줄건지
	 * @param deadLine 쿠폰 유효기간
	 * @return 쿠폰 발행 성공 여부와 메시지 (type : resultdata)
	 * @throws Exception
	 * @todo 쿠폰 사용 여부를 table에 column으로 추가해서 쿠폰 여러장 받을 수 있도록 바꾸기
	 */
	public ResultData notifyTempCouponByMessage(Member member, String deadLine) throws Exception {
		
		// 쿠폰번호로 6자리 문자+숫자 무작위 조합 만들어냄
		String couponPassword = Util.getTempPassword(6);
		
		// 문자 메시지 내용 build하기
		MessageDTO messageDTO = MessageDTO.builder()
				.to(member.getCellphoneNum())
				.content(Util.f("안녕하세요 %s님^^ 수강신청을 위한 쿠폰 번호는 %s 입니다.", member.getName(), couponPassword))
				.build();
		
		// 문자 보내고 난 결과를 받아옴 (결과에는 성공 여부도 담겨있음)
		SmsResponseDTO response = messageService.sendMessage(messageDTO);
		
		if(response.getStatusName().equals("success") == false) {
			return ResultData.from("F-1", "쿠폰번호 문자 전송에 실패하였습니다.");
		}
		
		// 문자 성공적으로 보냈으면 쿠폰 테이블에 insert 하기
		// 한번 쿠폰 받으면 다시 못 받음.
		couponService.doGiveCoupon(deadLine, member.getId(), couponPassword);
		
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

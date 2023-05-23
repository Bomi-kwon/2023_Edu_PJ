package com.koreaIT.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreaIT.project.service.GroupService;
import com.koreaIT.project.service.MemberService;
import com.koreaIT.project.util.Util;
import com.koreaIT.project.vo.Group;
import com.koreaIT.project.vo.Member;
import com.koreaIT.project.vo.Rq;

@Controller
public class ProjectMemberController {
	private MemberService memberService;
	private GroupService groupService;
	private Rq rq;
	
	@Autowired
	public ProjectMemberController(MemberService memberService, Rq rq, 
			GroupService groupService) {
		this.memberService = memberService;
		this.groupService = groupService;
		this.rq = rq;
	}
	
	@RequestMapping("/project/member/memberlist")
	public String memberlist(Model model) {
		
		List<Member> members = memberService.getMembers();
		model.addAttribute("members", members);
		
		return "project/member/memberlist";
	}
	
	@RequestMapping("/project/member/memberjoin")
	public String memberjoin() {
		return "project/member/memberjoin";
	}
	
	@RequestMapping("/project/member/doMemberJoin")
	@ResponseBody
	public String doMemberJoin(String loginID, String loginPW, String loginPWCheck, String name, String cellphoneNum, String email) {
		
		if(loginID == null) {
			return Util.jsHistoryBack("로그인 아이디를 입력하세요.");
		}
		
		if(loginPW == null) {
			return Util.jsHistoryBack("로그인 비밀번호를 입력하세요.");
		}
		
		if(!loginPW.equals(loginPWCheck)) {
			return Util.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		if(name == null) {
			return Util.jsHistoryBack("이름을 입력하세요.");
		}
		
		if(cellphoneNum == null) {
			return Util.jsHistoryBack("전화번호를 입력하세요.");
		}
		
		if(email == null) {
			return Util.jsHistoryBack("이메일을 입력하세요.");
		}
		
		memberService.doMemberJoin(loginID, loginPW, name, cellphoneNum, email);
		
		return Util.jsReplace(Util.f("%s님, 회원가입을 축하합니다.", name), "/");
	}
	
	@RequestMapping("/project/member/memberlogin")
	public String memberlogin() {
		
		if(rq.getLoginedMember() != null) {
			return Util.jsReplace("이미 로그인된 상태입니다.", "/");
		}
		
		
		return "project/member/memberlogin";
	}
	
	@RequestMapping("/project/member/doMemberLogin")
	@ResponseBody
	public String doMemberLogin(String loginID, String loginPW) {
		
		if(Util.empty(loginID)) {
			return Util.jsHistoryBack("로그인 아이디를 입력하세요.");
		}
		
		if(Util.empty(loginPW)) {
			return Util.jsHistoryBack("로그인 비밀번호를 입력하세요.");
		}
		
		Member member = memberService.getMemberByLoginID(loginID);
		
		if (member == null) {
			return Util.jsHistoryBack(Util.f("%s는 존재하지 않는 아이디입니다.",loginID));
		}
		
		if(!member.getLoginPW().equals(loginPW)) {
			return Util.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		rq.login(member);
		
		
		return Util.jsReplace(Util.f("%s님, 로그인되었습니다.", member.getName()), "/");
	}
	
	@RequestMapping("/project/member/doMemberLogout")
	@ResponseBody
	public String doMemberLogout() {
		
		if(rq.getLoginedMember() == null) {
			return Util.jsReplace("이미 로그아웃된 상태입니다.", "/");
		}
		rq.logout();
		return Util.jsReplace("로그아웃되었습니다.", "/");
	}
	
	@RequestMapping("/project/member/memberprofile")
	public String memberprofile(Model model) {
		
		Member member = memberService.getMemberById(rq.getLoginedMember().getId());
		model.addAttribute("member", member);
		
		return "project/member/memberprofile";
	}
	
	@RequestMapping("/project/member/membergroup")
	public String membergroup(Model model) {
		
		List<Group> groups = groupService.getgroups();
		model.addAttribute("groups", groups);
		
		return "project/member/membergroup";
	}

	
}

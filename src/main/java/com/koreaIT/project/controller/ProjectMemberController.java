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
import com.koreaIT.project.vo.ResultData;
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
	public String doMemberJoin(int authLevel, String loginID, String loginPW, String loginPWCheck, String name, String cellphoneNum, String email) {
		
		if(Util.empty(loginID)) {
			return Util.jsHistoryBack("로그인 아이디를 입력하세요.");
		}
		
		if(Util.empty(loginPW)) {
			return Util.jsHistoryBack("로그인 비밀번호를 입력하세요.");
		}
		
		if(Util.empty(loginPWCheck)) {
			return Util.jsHistoryBack("로그인 비밀번호를 입력하세요.");
		}
		
		if(!loginPW.equals(loginPWCheck)) {
			return Util.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		if(Util.empty(name)) {
			return Util.jsHistoryBack("이름을 입력하세요.");
		}
		
		if(Util.empty(cellphoneNum)) {
			return Util.jsHistoryBack("전화번호를 입력하세요.");
		}
		
		if(Util.empty(email)) {
			return Util.jsHistoryBack("이메일을 입력하세요.");
		}
		
		memberService.doMemberJoin(loginID, Util.sha256(loginPW), name, cellphoneNum, email, authLevel);
		
		return Util.jsReplace(Util.f("%s님, 회원가입을 축하합니다.", name), "memberlogin");
	}
	
	@RequestMapping("/project/member/memberlogin")
	public String memberlogin() {
		
		if(rq.getLoginedMemberId() != 0) {
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
		
		if(!member.getLoginPW().equals(Util.sha256(loginPW))) {
			return Util.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		rq.login(member);
		
		
		return Util.jsReplace(Util.f("%s님, 로그인되었습니다.", member.getName()), "/");
	}
	
	@RequestMapping("/project/member/doMemberLogout")
	@ResponseBody
	public String doMemberLogout() {
		
		if(rq.getLoginedMemberId() == 0) {
			return Util.jsReplace("이미 로그아웃된 상태입니다.", "/");
		}
		rq.logout();
		return Util.jsReplace("로그아웃되었습니다.", "/");
	}
	
	@RequestMapping("/project/member/doMemberDrop")
	@ResponseBody
	public String doMemberDrop(int id) {
		
		rq.logout();
		memberService.doMemberDrop(id);
		
		return Util.jsReplace("탈퇴되었습니다.", "/");
	}
	
	@RequestMapping("/project/member/memberprofile")
	public String memberprofile(Model model) {
		
		Member member = memberService.getMemberById(rq.getLoginedMemberId());
		model.addAttribute("member", member);
		
		return "project/member/memberprofile";
	}
	
	@RequestMapping("/project/member/membergroup")
	public String membergroup(Model model) {
		
		List<Group> groups = groupService.getgroups();
		model.addAttribute("groups", groups);
		
		return "project/member/membergroup";
	}

	@RequestMapping("/project/member/checkpassword")
	public String checkpassword(Model model, int id) {
		
		Member member = memberService.getMemberById(id);
		model.addAttribute("member", member);
		
		return "project/member/checkpassword";
	}
	
	@RequestMapping("/project/member/membermodify")
	public String membermodify(Model model, int id, String loginPW) {
		
		Member member = memberService.getMemberById(id);
		
		if(!member.getLoginPW().equals(Util.sha256(loginPW))) {
			return rq.jsReturnOnView("비밀번호가 일치하지 않습니다.", true);
		}
		
		model.addAttribute("member", member);
		
		return "project/member/membermodify";
	}
	
	@RequestMapping("/project/member/doMemberModify")
	@ResponseBody
	public String doMemberModify(int id, String name, String cellphoneNum, String email) {
		
		memberService.doMemberModify(id, name, cellphoneNum, email);
		
		return Util.jsReplace("회원정보를 수정하였습니다.", "memberprofile");
	}
	
	@RequestMapping("/project/member/passwordmodify")
	public String passwordmodify(Model model, int id) {
		
		Member member = memberService.getMemberById(id);
		if (member == null) {
			return rq.jsReturnOnView("존재하지 않는 회원입니다.", true);
		}
		model.addAttribute("member", member);
		
		return "project/member/passwordmodify";
	}
	
	@RequestMapping("/project/member/doPasswordModify")
	@ResponseBody
	public String doPasswordModify(int id, String loginPW) {
		
		memberService.doPasswordModify(id, Util.sha256(loginPW));
		
		return Util.jsReplace("비밀번호를 수정하였습니다.", "/");
	}
	
	@RequestMapping("/project/member/getMembersByAuthLevel")
	@ResponseBody
	public ResultData getMembersByAuthLevel(int authLevel) {
		
		List<Member> members = memberService.getMembersByAuthLevel(authLevel);
		
		if(members.isEmpty()) {
			return ResultData.from("F-1", "회원이 없습니다");
		}
		
		return ResultData.from("S-1", "선택한 등급에 맞는 반을 가져왔습니다", "members", members);
	}
	
	@RequestMapping("/project/member/findLoginID")
	public String findLoginID() {
		return "project/member/findLoginID";
	}
	
	@RequestMapping("/project/member/doFindLoginID")
	@ResponseBody
	public String doFindLoginID(String name, String email) {
		
		Member member = memberService.getMemberByNameAndEmail(name, email);
		
		if(member == null) {
			return Util.jsHistoryBack("존재하지 않는 회원정보입니다.");
		}
		
		return Util.jsReplace(Util.f("회원님의 아이디는 %s 입니다.", member.getLoginID()), "memberlogin");
	}
	
	@RequestMapping("/project/member/findLoginPW")
	public String findLoginPW() {
		return "project/member/findLoginPW";
	}
	
	@RequestMapping("/project/member/doFindLoginPW")
	@ResponseBody
	public String doFindLoginPW(String name, String loginID, String email) {
		
		Member member = memberService.getMemberByLoginID(loginID);
		
		if(member == null) {
			return Util.jsHistoryBack("존재하지 않는 아이디입니다.");
		}
		
		if(!member.getName().equals(name)) {
			return Util.jsHistoryBack("이름과 아이디 정보가 일치하지 않습니다.");
		}
		
		if(!member.getEmail().equals(email)) {
			return Util.jsHistoryBack("이름과 이메일 정보가 일치하지 않습니다.");
		}
		
		ResultData notifyTempLoginPwByEmailRd = memberService.notifyTempLoginPwByEmail(member);
		
		return Util.jsReplace(notifyTempLoginPwByEmailRd.getMsg(), "memberlogin");
	}
	
	@RequestMapping("/project/member/groupregistration")
	public String groupregistration(Model model) {
		
		if(rq.getLoginedMember().getAuthLevel() != 2) {
			return rq.jsReturnOnView("학생 회원만 이용할 수 있는 탭입니다.", true);
		}
		
		List<Member> teachers = memberService.getMembersByAuthLevel(1);
		model.addAttribute("teachers",teachers);
		
		return "project/member/groupregistration";
	}
	
	@RequestMapping("/project/member/getGroupsByTeacherID")
	@ResponseBody
	public ResultData getGroupsByTeacherID(int groupTeacherId) {
		
		List<Group> groups = groupService.getGroupsByTeacherID(groupTeacherId);
		
		if(groups.isEmpty()) {
			return ResultData.from("F-1", "해당 선생님이 수업하는 반이 없습니다.");
		}
		
		return ResultData.from("S-1", "해당 선생님이 수업하는 반을 가져왔습니다", "groups", groups);
	}
	
	@RequestMapping("/project/member/groupregisterdetail")
	public String groupregisterdetail(Model model, int id) {
		
		Group group = groupService.getGroupById(id);
		model.addAttribute("group", group);
		
		return "project/member/groupregisterdetail";
	}
	
	
}

package com.koreaIT.project.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.koreaIT.project.service.CouponService;
import com.koreaIT.project.service.FileService;
import com.koreaIT.project.service.GroupService;
import com.koreaIT.project.service.MemberService;
import com.koreaIT.project.util.Util;
import com.koreaIT.project.vo.Coupon;
import com.koreaIT.project.vo.FileVO;
import com.koreaIT.project.vo.Group;
import com.koreaIT.project.vo.Member;
import com.koreaIT.project.vo.ResultData;
import com.koreaIT.project.vo.Rq;

@Controller
public class ProjectMemberController {
	private MemberService memberService;
	private GroupService groupService;
	private FileService fileService;
	private CouponService couponService;
	private Rq rq;
	
	
	@Autowired
	public ProjectMemberController(MemberService memberService, Rq rq, 
			GroupService groupService, FileService fileService, CouponService couponService) {
		this.memberService = memberService;
		this.groupService = groupService;
		this.fileService = fileService;
		this.couponService = couponService;
		this.rq = rq;
	}
	
	// 회원 명단
	
	@RequestMapping("/project/member/memberlist")
	public String memberlist(Model model) {
		
		List<Member> members = memberService.getMembers();
		model.addAttribute("members", members);
		
		return "project/member/memberlist";
	}
	
	
	//회원가입
	
	@RequestMapping("/project/member/memberjoin")
	public String memberjoin() {
		return "project/member/memberjoin";
	}
	
	@RequestMapping("/project/member/doMemberJoin")
	@ResponseBody
	public String doMemberJoin(int authLevel, String loginID, String loginPW, String loginPWCheck, 
			String name, String cellphoneNum, String email, MultipartFile file) {
		
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
		int relId = memberService.getLastId();
		
			try {
				if(!file.isEmpty()) {
					fileService.saveFile(file, "profile", relId);
				}
				else {
					fileService.saveBasicFile("profile", relId);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
		return Util.jsReplace(Util.f("%s님, 회원가입을 축하합니다.", name), "memberlogin");
	}
	
	
	// 회원 로그인
	
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
	
	
	// 회원 로그아웃
	
	@RequestMapping("/project/member/doMemberLogout")
	@ResponseBody
	public String doMemberLogout() {
		
		if(rq.getLoginedMemberId() == 0) {
			return Util.jsReplace("이미 로그아웃된 상태입니다.", "/");
		}
		rq.logout();
		return Util.jsReplace("로그아웃되었습니다.", "/");
	}
	
	
	// 회원 탈퇴
	
	@RequestMapping("/project/member/doMemberDrop")
	@ResponseBody
	public String doMemberDrop(int id) {
		
		rq.logout();
		memberService.doMemberDrop(id);
		
		return Util.jsReplace("탈퇴되었습니다.", "/");
	}
	
	
	// 회원 마이페이지
	
	@RequestMapping("/project/member/memberprofile")
	public String memberprofile(Model model) {
		
		Member member = memberService.getMemberById(rq.getLoginedMemberId());
		
		FileVO profileImg = fileService.getFileByRelId("profile", member.getId());
		
		model.addAttribute("member", member);
		model.addAttribute("profileImg",profileImg);
		
		return "project/member/memberprofile";
	}
	
	
	// 반 안내, 반 리스트 (선생 입장)
	
	@RequestMapping("/project/member/membergroup")
	public String membergroup(Model model) {
		
		List<Group> groups = groupService.getgroups();
		model.addAttribute("groups", groups);
		
		return "project/member/membergroup";
	}
	
	
	// 반 리스트에서 학년별로 반들 가져오기
	
	@RequestMapping("/project/member/getGroupsByGrade")
	@ResponseBody
	public ResultData getGroupsByGrade(String grade) {
		
		List<Group> groups;
		
		if(grade.equals("all")) {
			groups = groupService.getgroups();
		}
		else {
			groups = groupService.getGroupsByGrade(grade);
		}
		
		if(groups.isEmpty()) {
			return ResultData.from("F-1", "해당 학년에는 반이 없습니다.");
		}
		
		return ResultData.from("S-1", "해당 학년의 반을 가져왔습니다", "groups", groups);
	}
	
	
	// 회원 정보 수정 전 비밀번호 체크

	@RequestMapping("/project/member/checkpassword")
	public String checkpassword(Model model, int id) {
		
		Member member = memberService.getMemberById(id);
		model.addAttribute("member", member);
		
		return "project/member/checkpassword";
	}
	
	// 회원 정보 수정
	
	@RequestMapping("/project/member/membermodify")
	public String membermodify(Model model, int id, String loginPW) {
		
		Member member = memberService.getMemberById(id);
		
		if(!member.getLoginPW().equals(Util.sha256(loginPW))) {
			return rq.jsReturnOnView("비밀번호가 일치하지 않습니다.", true);
		}
		
		FileVO profileImg = fileService.getFileByRelId("profile", member.getId());
		
		model.addAttribute("member", member);
		model.addAttribute("profileImg", profileImg);
		
		return "project/member/membermodify";
	}
	
	@RequestMapping("/project/member/doMemberModify")
	@ResponseBody
	public String doMemberModify(int id, String name, String cellphoneNum, String email, int fileId, MultipartFile file) {
		
		memberService.doMemberModify(id, name, cellphoneNum, email);
		
		if(!file.isEmpty() || file != null) {
			try {
				fileService.updateFile(file, "profile", id, fileId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return Util.jsReplace("회원정보를 수정하였습니다.", "memberprofile");
	}
	
	
	// 회원 비밀번호 수정
	
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
	
	
	// 회원명단에서 선생/학생/학부모 별로 구분해서 보기
	
	@RequestMapping("/project/member/getMembersByAuthLevel")
	@ResponseBody
	public ResultData getMembersByAuthLevel(int authLevel) {
		
		List<Member> members;
		
		if(authLevel == 0) {
			members = memberService.getMembers();
		}
		else {
			members = memberService.getMembersByAuthLevel(authLevel);
		}
		
		if(members.isEmpty()) {
			return ResultData.from("F-1", "회원이 없습니다");
		}
		
		return ResultData.from("S-1", "선택한 등급에 맞는 반을 가져왔습니다", "members", members);
	}
	
	
	// 회원 아이디 찾기
	
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
	
	
	// 회원 비밀번호 찾기
	
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
	
	// 학생이 수강신청할 때
	
	@RequestMapping("/project/member/groupregistration")
	public String groupregistration(Model model) {
		
		if(rq.getLoginedMember().getAuthLevel() != 2) {
			return rq.jsReturnOnView("학생 회원만 이용할 수 있는 탭입니다.", true);
		}
		
		if (rq.getLoginedMember().getClassId() != 0) {
			return rq.jsReturnOnView("이미 수강신청을 완료했습니다.", true);
		}
		
		List<Group> groups = groupService.getgroups();
		List<Member> teachers = memberService.getMembersByAuthLevel(1);
		
		model.addAttribute("groups",groups);
		model.addAttribute("teachers",teachers);
		
		return "project/member/groupregistration";
	}
	
	
	// 선생님 별로 수강신청 반 구분
	
	@RequestMapping("/project/member/getGroupsByTeacherID")
	@ResponseBody
	public ResultData getGroupsByTeacherID(int groupTeacherId) {
		
		List<Group> groups;
		
		if(groupTeacherId == 0) {
			groups = groupService.getgroups();
		}
		else {
			groups = groupService.getGroupsByTeacherID(groupTeacherId);
		}
		
		if(groups.isEmpty()) {
			return ResultData.from("F-1", "해당 선생님이 수업하는 반이 없습니다.");
		}
		
		return ResultData.from("S-1", "해당 선생님이 수업하는 반을 가져왔습니다", "groups", groups);
	}
	
	
	// 수강신청 페이지 상세보기
	
	@RequestMapping("/project/member/groupregisterdetail")
	public String groupregisterdetail(Model model, int id) {
		
		Group group = groupService.getGroupById(id);
		
		Coupon coupon = couponService.getCouponByStudentId(rq.getLoginedMemberId());
		
		model.addAttribute("group", group);
		model.addAttribute("coupon", coupon);
		
		return "project/member/groupregisterdetail";
	}
	
	
	// 수강신청하기
	
	@RequestMapping("/project/member/doRegister")
	@ResponseBody
	public String doRegister(int classId) {
		
		// 멤버 테이블에 반 정보 업데이트
		memberService.doRegister(rq.getLoginedMemberId(), classId);
		
		return Util.jsReplace("수강신청을 완료했습니다.", "/");
	}
	
	
	// 결제 후 수강신청하기
	
	@RequestMapping("/project/member/doRegisterAfterPayment")
	@ResponseBody
	public String doRegisterAfterPayment(int classId, boolean paymentComplete) {
		
		if (paymentComplete == false) {
			return "결제 후 수강신청에 실패했습니다.";
		}
		
		// 멤버 테이블에 반 정보 업데이트
		memberService.doRegister(rq.getLoginedMemberId(), classId);
		
		return "결제 후 수강신청을 완료했습니다.";
	}
	
	
	// 각 반 학생 명단 상세보기
	
	@RequestMapping("/project/member/getMemberByClassId")
	public String getMemberByClassId(Model model, int classId) {
		
		List<Member> students = memberService.getMemberByClassId(classId);
		if(students.isEmpty()) {
			rq.jsReturnOnView("해당 반에는 학생이 없습니다.", true);
		} 
		
		model.addAttribute("students",students);
		model.addAttribute("classId",classId);
		
		return "project/member/studentlist";
	}
	
	
	// 학생 명단 출석부로 다운받기
	
	public static String fileName = "";
	
	@RequestMapping("/project/member/excelDownload")
	@ResponseBody
	public void excelDownload(HttpServletResponse response, int classId) throws IOException {
		
		List<Member> students = memberService.getMemberByClassId(classId);
		
//		if(students.isEmpty()) {
//			return Util.jsReplace("해당 반에는 학생이 없습니다.", "studentlist");
//		}
		
		Group group = groupService.getGroupById(classId);
		
		fileName = group.getGroupName() + "_attendance_book.xlsx";
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet sheet = workbook.createSheet("attendance book");
		
		Map<String, Object[]> data = new TreeMap<>();
		
		data.put("1", new Object[]{"학생 이름", "수강하는 반", "전화번호", "부모님 번호"});
		int i = 2;
		for(Member student : students) {
			data.put(String.valueOf(i), new Object[]{student.getName(), student.getGroupName(), 
					student.getCellphoneNum(), student.getEmail()});
			i++;
		}
		
		Set<String> keyset = data.keySet();
		int rownum = 0;
		
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if(obj instanceof String) {
					cell.setCellValue((String)obj);
				} else if (obj instanceof Integer){
					cell.setCellValue((Integer)obj);
				}
			}
		}
		
		// 다운로드 받을때 브라우저 하단에 다운로드 헤더 나오게 하기		
		// 그냥 fileName 쓰면 브라우저가 유니코드로 인식해서 꼭 encoding 해줘야함		
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		
		workbook.write(response.getOutputStream());
		workbook.close();
		
	}
	
	
	// 선생님이 학생한테 쿠폰줄 때
	
	@RequestMapping("/project/member/givecoupon")
	public String givecoupon() {
		
		if(rq.getLoginedMember().getAuthLevel() != 1) {
			return rq.jsReturnOnView("선생님 회원만 이용할 수 있는 탭입니다.", true);
		}
		
		return "project/member/givecoupon";
	}
	
	
	// 이름 검색해서 학생 명단 가져오기
	
	@RequestMapping("/project/member/getStudentsByNameKeyWord")
	@ResponseBody
	public ResultData getStudentsByNameKeyWord(String keyWord) {
		
		List<Member> students = memberService.getStudentsByNameKeyWord(keyWord);
		
		if(students.isEmpty()) {
			return ResultData.from("F-1", "해당하는 학생 명단이 없습니다.");
		}
		
		return ResultData.from("S-1", "해당하는 학생 명단을 가져왔습니다", "students", students);
	}
	
	
	// 학생한테 최종적으로 쿠폰 발행
	
	@RequestMapping("/project/member/doGiveCoupon")
	@ResponseBody
	public String doGiveCoupon(String deadLine, int studentId) throws Exception {
		
		Member member = memberService.getMemberById(studentId);
		
		if (member == null) {
			return Util.jsReplace("존재하지 않는 학생입니다.", "givecoupon");
		}
		
		Coupon coupon = couponService.getCouponByStudentId(studentId);
		
		if (coupon != null) {
			return Util.jsReplace(Util.f("%s 학생에게는 이미 쿠폰이 있습니다.", member.getName()) , "givecoupon");
		}
		
		ResultData notifyTempCouponByMessageRd =  memberService.notifyTempCouponByMessage(member, deadLine, studentId);
		
		return Util.jsReplace(notifyTempCouponByMessageRd.getMsg(), "/");
	}
	
	
	// 학생이 적은 쿠폰번호 맞는지 확인하기
	
	@RequestMapping("/project/member/verifyPassword")
	@ResponseBody
	public ResultData verifyPassword(String couponPassword) {
		
		Coupon coupon = couponService.getCouponByStudentId(rq.getLoginedMemberId());
		
		if(coupon == null) {
			return ResultData.from("F-1", "쿠폰이 지급되지 않았습니다.");
		}
		
		if(!coupon.getCouponPassword().equals(couponPassword)) {
			return ResultData.from("F-1", "쿠폰번호가 틀렸습니다.");
		}
		
		return ResultData.from("S-1", "쿠폰이 성공적으로 등록되었습니다.","coupon", coupon);
	}
	
	
	// 반 생성
	
	@RequestMapping("/project/member/makeGroup")
	public String makeGroup(Model model) {
		
		List<Member> teachers = memberService.getMembersByAuthLevel(1);
		model.addAttribute("teachers", teachers);
		
		return "project/member/makeGroup";
	}
	
	
	// 반 최종 생성
	
	@RequestMapping("/project/member/doMakeGroup")
	@ResponseBody
	public String doMakeGroup(String grade, String groupName, String groupDay, int groupTeacherId, String textbook) {
		
		groupService.doMakeGroup(grade, groupName, groupDay, groupTeacherId, textbook);
			
		return Util.jsReplace(Util.f("%s반 생성이 완료되었습니다.", groupName), "membergroup");
	}
	
	
	// 반 삭제
	
	@RequestMapping("/project/member/doDeleteGroup")
	@ResponseBody
	public String doDeleteGroup(int classId, String groupName) {
		
		groupService.doDeleteGroup(classId);
		
		return Util.jsReplace("반이 삭제되었습니다.", "membergroup");
	}
	
}

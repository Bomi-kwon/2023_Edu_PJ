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
	
	/**
	 * 회원 명단 리스트 (선생님만 볼 수 있음)
	 * @param model 전체 회원 명단 리스트 보내주기
	 * @return memberlist.jsp 페이지
	 */
	@RequestMapping("/project/member/memberlist")
	public String memberlist(Model model) {
		
		List<Member> members = memberService.getMembers();
		model.addAttribute("members", members);
		
		return "project/member/memberlist";
	}
	
	
	/**
	 * 회원가입
	 * @return memberjoin.jsp 페이지
	 */
	@RequestMapping("/project/member/memberjoin")
	public String memberjoin() {
		return "project/member/memberjoin";
	}
	
	
	/**
	 * 최종 회원가입
	 * @param authLevel (선생,학생,학부모 등급 구분)
	 * @param loginID 아이디
	 * @param loginPW 비밀번호
	 * @param loginPWCheck 비밀번호 확인
	 * @param name 이름
	 * @param cellphoneNum 휴대폰번호
	 * @param email 이메일
	 * @param file 프로필 이미지파일
	 * @return 완료메시지 > 로그인 페이지
	 */
	@RequestMapping("/project/member/doMemberJoin")
	@ResponseBody
	public String doMemberJoin(int authLevel, String loginID, String loginPW, String loginPWCheck, 
			String name, String cellphoneNum, String email, MultipartFile file) {
		
		if(!loginPW.equals(loginPWCheck)) {
			return Util.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}
		
		memberService.doMemberJoin(loginID, Util.sha256(loginPW), name, cellphoneNum, email, authLevel);
		int relId = memberService.getLastId();
		
			try {
				if(!file.isEmpty()) {
					fileService.saveFile(file, "profile", relId);
				}
				else {
					// 프로필 이미지 입력 안 하면 기본 이미지로 저장
					fileService.saveBasicFile("profile", relId);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return Util.jsReplace(Util.f("%s님, 회원가입을 축하합니다.", name), "memberlogin");
	}
	
	
	/**
	 * 회원 로그인
	 * @return memberlogin 페이지
	 */
	@RequestMapping("/project/member/memberlogin")
	public String memberlogin() {
		return "project/member/memberlogin";
	}
	
	
	/**
	 * 최종 로그인
	 * @param loginID 아이디
	 * @param loginPW 비밀번호
	 * @return 완료 메시지 > 메인 페이지
	 */
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
		
		// 누가 로그인했는지 rq에 꼭 저장해야함!!!
		rq.login(member);
		
		return Util.jsReplace(Util.f("%s님, 로그인되었습니다.", member.getName()), "/");
	}
	
	
	/**
	 * 회원 로그아웃
	 * @return 완료메시지 > 메인페이지
	 */
	@RequestMapping("/project/member/doMemberLogout")
	@ResponseBody
	public String doMemberLogout() {
		
		if(rq.getLoginedMemberId() == 0) {
			return Util.jsReplace("이미 로그아웃된 상태입니다.", "/");
		}
		
		// rq에서 현재 로그인된 멤버 없애주기!!
		rq.logout();
		return Util.jsReplace("로그아웃되었습니다.", "/");
	}
	
	
	/**
	 * 회원 탈퇴
	 * @param id 회원 번호
	 * @return 완료 메시지 > 메인 페이지
	 */
	@RequestMapping("/project/member/doMemberDrop")
	@ResponseBody
	public String doMemberDrop(int id) {
		
		// 로그아웃 먼저 하고
		rq.logout();
		// DB에 멤버 정보 지우기
		memberService.doMemberDrop(id);
		
		return Util.jsReplace("탈퇴되었습니다.", "/");
	}
	
	
	/**
	 * 회원 마이페이지
	 * @param model 현재 로그인된 멤버 정보 보내주기
	 * @return memberprofile 페이지
	 */
	@RequestMapping("/project/member/memberprofile")
	public String memberprofile(Model model) {
		
		Member member = memberService.getMemberById(rq.getLoginedMemberId());
		
		FileVO profileImg = fileService.getFileByRelId("profile", member.getId());
		
		model.addAttribute("member", member);
		model.addAttribute("profileImg",profileImg);
		
		return "project/member/memberprofile";
	}
	
	
	/**
	 * 반 안내, 반 리스트 (선생 입장)
	 * @param model
	 * @return membergroup.jsp에 반 명단 전달해주기
	 */
	@RequestMapping("/project/member/membergroup")
	public String membergroup(Model model) {
		
		List<Group> groups = groupService.getgroups();
		model.addAttribute("groups", groups);
		
		return "project/member/membergroup";
	}
	
	
	/**
	 * 반 리스트에서 학년별로 반들 가져오기 (membergroup.jsp의 ajax 함수)
	 * @param grade (내가 선택한 학년)
	 * @return 선택한 학년의 반 리스트들
	 */
	@RequestMapping("/project/member/getGroupsByGrade")
	@ResponseBody
	public ResultData getGroupsByGrade(String grade) {
		
		List<Group> groups;
		
		// 전체 선택하면 전체 반 리스트 다 가져오기
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
	
	
	/**
	 * 회원 정보 수정 전 비밀번호 체크
	 * @param id 회원 번호
	 * @param model 회원 정보 보내주기
	 * @return checkpassword 페이지
	 */
	@RequestMapping("/project/member/checkpassword")
	public String checkpassword(Model model, int id) {
		
		Member member = memberService.getMemberById(id);
		model.addAttribute("member", member);
		
		return "project/member/checkpassword";
	}
	
	/**
	 * 비밀번호 맞게 입력했는지 확인 > 회원 정보 수정
	 * @param id 회원 번호
	 * @param loginPW 비밀번호
	 * @param model 회원 정보 보내주기
	 * @return membermodify 페이지
	 */
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
	
	/**
	 * 회원 정보 최종 수정
	 * @param id 회원 번호
	 * @param name 이름
	 * @param cellphoneNum 휴대폰 번호
	 * @param email 이메일
	 * @param fileId 기존 이미지파일 번호
	 * @param file 새로운 이미지파일
	 * @return 완료메시지 > memberprofile 페이지
	 */
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
	
	
	/**
	 * 회원 비밀번호 수정
	 * @param id 회원 번호
	 * @param model 멤버 정보 보내주기
	 * @return passwordmodify 페이지
	 */
	@RequestMapping("/project/member/passwordmodify")
	public String passwordmodify(Model model, int id) {
		
		Member member = memberService.getMemberById(id);
		if (member == null) {
			return rq.jsReturnOnView("존재하지 않는 회원입니다.", true);
		}
		model.addAttribute("member", member);
		
		return "project/member/passwordmodify";
	}
	
	/**
	 * 회원 비밀번호 최종 수정
	 * @param id 회원 번호
	 * @param loginPW 비밀번호
	 * @return 완료메시지 > 메인페이지
	 */
	@RequestMapping("/project/member/doPasswordModify")
	@ResponseBody
	public String doPasswordModify(int id, String loginPW) {
		
		// 비밀번호 암호화해서 DB에 저장
		memberService.doPasswordModify(id, Util.sha256(loginPW));
		
		return Util.jsReplace("비밀번호를 수정하였습니다.", "/");
	}
	
	
	/**
	 * 회원명단에서 선생/학생/학부모 별로 구분해서 보여주는 ajax 함수
	 * @param authLevel 멤버 등급
	 * @return 회원 리스트 (resultdata)
	 */
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
	
	
	/**
	 * 회원 아이디 찾기
	 * @return findloginID 페이지
	 */
	@RequestMapping("/project/member/findLoginID")
	public String findLoginID() {
		return "project/member/findLoginID";
	}
	
	/**
	 * 회원 아이디 찾기 최종
	 * @param name 이름
	 * @param email 이메일
	 * @return 아이디 alert해주고 로그인 페이지
	 */
	@RequestMapping("/project/member/doFindLoginID")
	@ResponseBody
	public String doFindLoginID(String name, String email) {
		
		Member member = memberService.getMemberByNameAndEmail(name, email);
		
		if(member == null) {
			return Util.jsHistoryBack("존재하지 않는 회원정보입니다.");
		}
		
		return Util.jsReplace(Util.f("회원님의 아이디는 %s 입니다.", member.getLoginID()), "memberlogin");
	}
	
	
	/**
	 * 회원 비밀번호 찾기
	 * @return findLoginPW 페이지
	 */
	@RequestMapping("/project/member/findLoginPW")
	public String findLoginPW() {
		return "project/member/findLoginPW";
	}
	
	/**
	 * 회원 비밀번호 찾기 최종
	 * @param name 이름
	 * @param loginID 아이디
	 * @param email 이메일
	 * @return 비밀번호 이메일 발송 후 로그인 페이지
	 */
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
		
		// 난수 발생시켜 새로운 비밀번호 만들고 회원의 이메일로 전송하기!!
		ResultData notifyTempLoginPwByEmailRd = memberService.notifyTempLoginPwByEmail(member);
		
		return Util.jsReplace(notifyTempLoginPwByEmailRd.getMsg(), "memberlogin");
	}
	
	/**
	 * 학생이 수강신청할 때
	 * @param model 반과 선생님 정보를 보내주기
	 * @return 수강신청 페이지
	 */
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
	
	
	/**
	 * 선생님 별로 수강신청 반 구분해서 보여주는 ajax 함수
	 * @param groupTeacherId 선생님 번호
	 * @return 해당 선생님이 가르치는 반 리스트 (resultdata)
	 */
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
	
	
	/**
	 * 수강신청 페이지 상세보기
	 * @param model
	 * @param id 반 번호
	 * @return 반 정보와 쿠폰여부를 담아 jsp 페이지로 보내기
	 */
	@RequestMapping("/project/member/groupregisterdetail")
	public String groupregisterdetail(Model model, int id) {
		
		Group group = groupService.getGroupById(id);
		
		Coupon coupon = couponService.getCouponByStudentId(rq.getLoginedMemberId());
		
		model.addAttribute("group", group);
		model.addAttribute("coupon", coupon);
		
		return "project/member/groupregisterdetail";
	}
	
	
	/**
	 * 수강신청하기 (쿠폰 사용 후)
	 * @param classId 반 번호
	 * @return 완료메시지를 alert하고 메인페이지로 돌리기
	 */
	@RequestMapping("/project/member/doRegister")
	@ResponseBody
	public String doRegister(int classId) {
		
		// 멤버 테이블에 반 정보 업데이트
		memberService.doRegister(rq.getLoginedMemberId(), classId);
		
		return Util.jsReplace("수강신청을 완료했습니다.", "/");
	}
	
	
	/**
	 * 수강신청하기 (결제 후 ajax 함수로)
	 * @param classId 학생이 어느반에 수강신청한건지 알아야함
	 * @param paymentComplete 결제가 성공적으로 된건지 알아야함
	 * @return 문자열을 리턴해서 jsp에서 alert창으로 보여줌!
	 * return 타입 String이라서 ajax 타입 text로 바꿔줘야함
	 */
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
	
	
	/**
	 * 각 반 학생 명단 상세보기
	 * @param model
	 * @param classId (membergroup에서 클릭한 반)
	 * @return 그 반의 학생명단을 담아서 studentlist.jsp에 보내주기
	 */
	@RequestMapping("/project/member/getMemberByClassId")
	public String getMemberByClassId(Model model, int classId) {
		
		List<Member> students = memberService.getMemberByClassId(classId);
		if(students.isEmpty()) {
			rq.jsReturnOnView("해당 반에는 학생이 없습니다.", true);
		} 
		
		model.addAttribute("students",students);
		
		// 반 삭제하거나 출석부 다운받을때 classId 필요함
		model.addAttribute("classId",classId);
		
		return "project/member/studentlist";
	}
	
	
	
	public static String fileName = "";
	/**
	 * 학생 명단 출석부로 다운받기
	 * @param response (다운로드시 웹사이트 하단에 header 설정해서 보낼것임)
	 * @param classId (이걸로 반 정보 가져와야함)
	 * @throws IOException
	 * apache.poi maven dependancy 추가
	 */
	@RequestMapping("/project/member/excelDownload")
	@ResponseBody
	public void excelDownload(HttpServletResponse response, int classId) throws IOException {
		
		List<Member> students = memberService.getMemberByClassId(classId);
		
		Group group = groupService.getGroupById(classId);
		
		// 엑셀파일 제목 만들기
		fileName = group.getGroupName() + "_attendance_book.xlsx";
		
		// 엑셀파일 생성 (.xlsw 파일 포맷 사용할때 XSSF로 시작하는 class 사용)
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		// 엑셀파일 안에 sheet 생성하고 이름 짓기
		XSSFSheet sheet = workbook.createSheet("attendance book");
		
		// 일단 맵에다가 정보들을 저장하고 저 맵을 엑셀의 행열에 추가할것임
		Map<String, Object[]> data = new TreeMap<>();
		
		// 첫번째 줄에 표의 head 만들기
		data.put("1", new Object[]{"학생 이름", "수강하는 반", "전화번호", "부모님 번호"});
		int i = 2;
		for(Member student : students) {
			// 두번째 줄부터 웹페이지 속 학생 정보들을 한줄한줄 채우기
			data.put(String.valueOf(i), new Object[]{student.getName(), student.getGroupName(), 
					student.getCellphoneNum(), student.getParentPhoneNum()});
			i++;
		}
		
		// data에서 keySet를 가져와서 조회하며 데이터들을 sheet에 입력한다.
		// map에 key값만 전체 출력 (키는 행 번호들이 문자화된 모습임)
		Set<String> keyset = data.keySet();
		int rownum = 0;
		
		// TreeMap을 통해 생성된 keySet는 for로 조회시, 키값의 오름차순으로 조회된다.
		// 시트에서 행(row) 생성
		// key값에 맞는 데이터들(이름, 반이름, 폰번호, 부모님번호)를 뽑아서 배열에 넣어놓고
		// 행에서 cell 하나씩 생성해서 배열 속 데이터들 하나씩 넣어줌
		// 데이터들의 형태가 다양할 수 있으므로 일단 object로 넣어놓고
		// 나중에 뽑아쓸때 string, integer 등 원래 타입을 다시 적용해줌
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
		
		// 위에까지만 하면 view 상에서는 어떤 변화도 없이 엑셀파일만 다운받아짐. 그래서 아래 코드 추가
		// 파일을 client에게 전송할 때 response 사용함
		// 다운로드 받을때 브라우저 하단에 다운로드 헤더 나오게 하기!!
		// 그냥 fileName 쓰면 브라우저가 유니코드로 인식해서 꼭 UTF-8 encoding 해줘야함
		// stream은 데이터의 입력과 출력을 하도록 이어주는 통로
		// stream은 buffer에 임시적으로 데이터를 보아서 보내줌
		
		// 파일 유형설정
		response.setContentType("ms-vnd/excel");
		// 데이터 형식 설정 (attachment : 첨부파일)
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		// 버퍼의 출력스트림을 출력
		workbook.write(response.getOutputStream());
		// 출력 스트림 닫기
		workbook.close();
		
	}
	
	
	/**
	 * 선생님이 학생한테 쿠폰줄 때
	 * @return 쿠폰주기 페이지
	 */
	@RequestMapping("/project/member/givecoupon")
	public String givecoupon() {
		
		// 선생님만 쿠폰주기 페이지 들어갈 수 있음
		if(rq.getLoginedMember().getAuthLevel() != 1) {
			return rq.jsReturnOnView("선생님 회원만 이용할 수 있는 탭입니다.", true);
		}
		
		return "project/member/givecoupon";
	}
	
	
	/**
	 * 쿠폰 주기전 이름 검색해서 학생 명단 가져오기
	 * @param keyWord 검색어
	 * @return 학생명단(type : resultData)
	 */
	@RequestMapping("/project/member/getStudentsByNameKeyWord")
	@ResponseBody
	public ResultData getStudentsByNameKeyWord(String keyWord) {
		
		// 이름에 검색어를 포함한 학생 명단을 가져온다.
		List<Member> students = memberService.getStudentsByNameKeyWord(keyWord);
		
		if(students.isEmpty()) {
			return ResultData.from("F-1", "해당하는 학생 명단이 없습니다.");
		}
		
		return ResultData.from("S-1", "해당하는 학생 명단을 가져왔습니다", "students", students);
	}
	
	
	/**
	 * 학생한테 최종적으로 쿠폰 발행
	 * @param deadLine 유효기간
	 * @param studentId 어떤 학생에게 쿠폰을 줄건지
	 * @return 성공여부 메시지 alert하고 메인페이지로 이동
	 * @throws Exception
	 */
	@RequestMapping("/project/member/doGiveCoupon")
	@ResponseBody
	public String doGiveCoupon(String deadLine, int studentId) throws Exception {
		
		Member member = memberService.getMemberById(studentId);
		
		if (member == null) {
			return Util.jsReplace("존재하지 않는 학생입니다.", "givecoupon");
		}
		
		// 쿠폰 테이블에서 이미 쿠폰 준적 있는지 확인하기
		Coupon coupon = couponService.getCouponByStudentId(studentId);
		
		if (coupon != null) {
			return Util.jsReplace(Util.f("%s 학생에게는 이미 쿠폰이 있습니다.", member.getName()) , "givecoupon");
		}
		
		// 없으면 쿠폰 지급
		// 쿠폰은 난수를 발생시켜 문자로 지급할것임!! 
		ResultData notifyTempCouponByMessageRd =  memberService.notifyTempCouponByMessage(member, deadLine);
		
		return Util.jsReplace(notifyTempCouponByMessageRd.getMsg(), "/");
	}
	
	
	/**
	 * 학생이 적은 쿠폰번호 맞는지 확인하기
	 * @param couponPassword 학생이 입력한 쿠폰번호
	 * @return 쿠폰 등록 성공 여부 (type : resultdata)
	 */
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
	
	
	/**
	 * 반 생성
	 * @param model 선생님 명단 보내주기
	 * @return 반 생성 페이지
	 */
	@RequestMapping("/project/member/makeGroup")
	public String makeGroup(Model model) {
		
		List<Member> teachers = memberService.getMembersByAuthLevel(1);
		model.addAttribute("teachers", teachers);
		
		return "project/member/makeGroup";
	}
	
	
	/**
	 * 반 최종 생성
	 * @param grade 학년
	 * @param groupName 반 이름
	 * @param groupDay 수강 요일
	 * @param groupTeacherId 선생님 번호
	 * @param textbook 수업 교재
	 * @return 완료 메시지 > 반 안내 페이지
	 */
	@RequestMapping("/project/member/doMakeGroup")
	@ResponseBody
	public String doMakeGroup(String grade, String groupName, String groupDay, int groupTeacherId, String textbook) {
		
		groupService.doMakeGroup(grade, groupName, groupDay, groupTeacherId, textbook);
			
		return Util.jsReplace(Util.f("%s반 생성이 완료되었습니다.", groupName), "membergroup");
	}
	
	
	/**
	 * 반 삭제
	 * @param classId 반 번호
	 * @param groupName 반 이름 (alert 메시지에 쓰려고)
	 * @return 반 안내 페이지
	 */
	@RequestMapping("/project/member/doDeleteGroup")
	@ResponseBody
	public String doDeleteGroup(int classId, String groupName) {
		
		groupService.doDeleteGroup(classId);
		
		return Util.jsReplace(Util.f("%d반이 삭제되었습니다.", groupName), "membergroup");
	}
	
}

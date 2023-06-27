package com.koreaIT.project.controller;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.koreaIT.project.service.AttendanceService;
import com.koreaIT.project.service.FileService;
import com.koreaIT.project.service.GroupService;
import com.koreaIT.project.service.MemberService;
import com.koreaIT.project.util.Util;
import com.koreaIT.project.vo.FileVO;
import com.koreaIT.project.vo.Group;
import com.koreaIT.project.vo.Member;

@Controller
public class ProjectHomeController {
	
	private FileService fileService;
	private GroupService groupService;
	private MemberService memberService;
	private AttendanceService attendanceService;
	
	

	@Autowired
	public ProjectHomeController(FileService fileService, GroupService groupService, 
			MemberService memberService, AttendanceService attendanceService) {
		this.fileService = fileService;
		this.groupService = groupService;
		this.memberService = memberService;
		this.attendanceService = attendanceService;
	}
	
	@RequestMapping("/project/home/main")
	public String showMain() {
		return "project/home/main";
	}
	
	@RequestMapping("/")
	public String redirect() {
		return "redirect:/project/home/main";
	}
	
	
	// 모의고사 시계
	
	@RequestMapping("/project/home/timer")
	public String timer() {
		return "project/home/timer";
	}
	
	
	// 찾아오시는 길
	
	@RequestMapping("/project/home/map")
	public String map() {
		return "project/home/map";
	}
	
	
	// QR코드 생성을 위한 날짜,반 입력 화면
	
	@RequestMapping("/project/home/setqrurl")
	public String setqrurl() {
		return "project/home/setqrurl";
	}
	
	
	/**
	 * QR코드 생성
	 * @param todayDate (qr코드에 들어갈 정보)
	 * @param classId (qr코드에 들어갈 정보)
	 * @return QR코드 리턴함
	 * @throws WriterException
	 * @throws IOException
	 * google.zxing maven dependancy 추가
	 */
	@RequestMapping("/project/home/doMakeQR")
	@ResponseBody
	public Object doMakeQR(String todayDate, int classId) throws WriterException, IOException {
		int width = 400;
		int height = 400;
		
		// 아직 배포를 하지 않아 localhost로만 이용할 수 있어서
		// 일단 서버가 있는 컴퓨터의 ip주소로 연결해놓음
		// 즉 핸드폰 또는 다른 컴퓨터가 localhost 컴퓨터와 같은 와이파이를 사용한다면 내 사이트 들어올 수 있음
		// 대신 이 장소의 ip 주소가 주기적으로 바뀌므로 시연하기 직전에
		// Window + R -> cmd -> ipconfig로 ipv4 주소 확인후
		// 이 url을 업데이트 해줘야함 꼭!!!
		String url = Util.f("http://192.168.200.18:8081/project/home/attendanceChk?todayDate=%s&classId=%d", todayDate, classId);
		
		// zxing 라이브러리를 이용해 바코드 또는 QR코드를 생성할 수 있음!!
		// url을 특정 높이와 너비의 bitmatrix로 생성한 후
		// multiformatwriter.encode()를 이용해 boolean형 2차원 배열로 표현된 QR코드를 만든다.
		BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
		
		// ByteArrayOutputStream을 선언한 시점에 그것만의 `Buffer(byte[])`가 자동으로 켜지고
		// 그것의 저장소에 .write()하여 내용을 적게 되는것이다.
		// toByteArray()를 이용하여 Array화 할 수도 있다.
		// ByteArrayOutputStream 은 Stream과는 크게 상관이 없어 flush, close하는것은 의미가 없다.
		// 선언후 값이 아무것도 없으면 초기값을 가지게되고
		// 만약 값을 넣게되면 필요한만큼 사이즈를 증가시킨다.
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			
			MatrixToImageWriter.writeToStream(matrix, "PNG", out);
			
			// QR코드를 함수에서 바이트 배열로 반환 후 이미지로 출력해준다.
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(out.toByteArray());
		}
	}
	
	
	/**
	 * 출석체크를 위한 이름 입력
	 * @param model
	 * @param todayDate (출석체크 전 최종 확인 : 오늘 날짜 맞는지)
	 * @param classId (출석체크 전 최종 확인 : 내가 수강하는 반 정보 맞는지)
	 * @return 학생이 출석체크하는 화면을 보여줌
	 */
	@RequestMapping("/project/home/attendanceChk")
	public String attendanceChk(Model model, String todayDate, int classId) {
		
		Group group = groupService.getGroupById(classId);
		
		model.addAttribute("todayDate", todayDate);
		model.addAttribute("group", group);
		
		return "project/home/attendanceChk";
	}
	
	
	/**
	 * 최종 출석체크 확정
	 * @param classId (우리 반 수강생에 그 이름이 있는지 확인)
	 * @param name (input에 적은 이름 받아오기)
	 * @return 출석체크 결과를 alert로 알려주고 메인으로 돌아감
	 */
	@RequestMapping("/project/home/doAttendanceChk")
	@ResponseBody
	public String doAttendanceChk(int classId, String name){
		
		Member member = memberService.getMemberByName(name);
		
		if(member == null) {
			return Util.jsHistoryBack("이름을 잘못 입력했습니다.");
		}
		
		if(member.getClassId() != classId) {
			return Util.jsHistoryBack(Util.f("%s 학생은 우리반 수강생이 아닙니다.", name));
		}
			
		attendanceService.insertAttendance(classId, member.getId());
		
		return Util.jsReplace(Util.f("%s 학생 출석체크 완료되었습니다.", name), "/");
	}
	
	
	
	
	/**
	 * 업로드한 파일은 번호로 절대경로 연결
	 * @param id 파일번호
	 * @param model 
	 * @return 파일의 경로를 리턴
	 * @throws IOException
	 */
	@RequestMapping("/project/home/file/{fileId}")
	@ResponseBody
	public Resource downloadImage(@PathVariable("fileId") int id, Model model) throws IOException {
		
		// 파일의 src에 보통은 경로를 적지만 이렇게 컨트롤러 함수를 적어줘도 됨
		// 단, 파일마다 달라지는 id를 표현하기 위해 경로의 마지막 부분을 ${file.id} 이렇게 적어주고
		// 함수에서 걔를 이런식으로 parameter로 받아올 수 있음!!
		
		FileVO fileVo = fileService.getFileById(id);

		// 그럼 그 id로 file을 찾아와서 걔의 경로를 적어서 이렇게 나타내주면 파일이 잘 나옴
		return new UrlResource("file:" + fileVo.getSavedPath()); 
	}
	
	
	// 메세지 발송 (카카오톡/문자) 선택 화면
	
	@RequestMapping("/project/home/select")
	public String select() {
		
		return "project/home/select";
	}
	
	
	/**
	 * 오디오 파일 업로드 하기 (ajax 함수)
	 * @param request (timer.jsp의 form에서 받아온 오디오 파일)
	 * @return 그 파일을 DB에 저장하고 가져온 id
	 * @throws Exception
	 */
	@RequestMapping(value="/project/home/uploadAudioFile", method=RequestMethod.POST)
	@ResponseBody
	public int uploadAudioFile(MultipartHttpServletRequest request) throws Exception{
		
		MultipartFile audioFile = request.getFile("file");
		
		int audioFileId = 0;
		
		if(!audioFile.isEmpty() || audioFile != null) {
			
			// 어차피 id로 오디오 파일들이 잘 구분되고
			// relId가 특별한 의미가 없으므로 1로 고정
			fileService.saveFile(audioFile, "audio", 1);
			
			// 방금 DB에 추가된 file의 id 가져와서 리턴할 것임
			audioFileId = fileService.getLastId();
		}
		
		return audioFileId;
	}
	
	
}

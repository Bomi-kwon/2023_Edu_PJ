package com.koreaIT.project.controller;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
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
	
	

	@Autowired
	public ProjectHomeController(FileService fileService, GroupService groupService, MemberService memberService) {
		this.fileService = fileService;
		this.groupService = groupService;
		this.memberService = memberService;
	}
	
	@RequestMapping("/project/home/main")
	public String showMain() {
		return "project/home/main";
	}
	
	@RequestMapping("/")
	public String redirect() {
		return "redirect:/project/home/main";
	}
	
	@RequestMapping("/project/home/timer")
	public String timer() {
		return "project/home/timer";
	}
	
	@RequestMapping("/project/home/map")
	public String map() {
		return "project/home/map";
	}
	
	@RequestMapping("/project/home/setqrurl")
	public String setqrurl() {
		return "project/home/setqrurl";
	}
	
	@RequestMapping("/project/home/doMakeQR")
	@ResponseBody
	public Object doMakeQR(String todayDate, int classId) throws WriterException, IOException {
		int width = 400;
		int height = 400;
		
		String url = Util.f("http://192.168.200.32:8081/project/home/attendanceChk?todayDate=%s&classId=%d", todayDate, classId);
		BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
		
		System.out.println(todayDate);
		System.out.println(classId);
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			MatrixToImageWriter.writeToStream(matrix, "PNG", out);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(out.toByteArray());
		}
	}
	
	@RequestMapping("/project/home/attendanceChk")
	public String attendanceChk(Model model, String todayDate, int classId) {
		
		Group group = groupService.getGroupById(classId);
		
		model.addAttribute("todayDate", todayDate);
		model.addAttribute("group", group);
		
		return "project/home/attendanceChk";
	}
	
	
	@RequestMapping("/project/home/doAttendanceChk")
	@ResponseBody
	public String doAttendanceChk(String todayDate, int classId, String name){
		
		Member member = memberService.getMemberByName(name);
		
		if(member == null) {
			return Util.jsHistoryBack("이름을 잘못 입력했습니다.");
		}
		
		if(member.getClassId() != classId) {
			return Util.jsHistoryBack(Util.f("%s 학생은 우리반 수강생이 아닙니다.", name));
		}
			
		
		
		return Util.jsReplace(Util.f("%s 학생 출석체크 완료되었습니다.", name), "/");
	}
	
	
	// 이미지에서 src에 보통은 경로를 적지만 이렇게 컨트롤러 함수를 적어줘도 됨
	// 단 이미지마다 달라지는 id를 표현하기 위해 경로의 마지막 부분을 ${file.id} 이렇게 적어주고
	// 함수에서 걔를 이런식으로 parameter로 받아올 수 있음!!
	@RequestMapping("/project/home/file/{fileId}")
	@ResponseBody
	public Resource downloadImage(@PathVariable("fileId") int id, Model model) throws IOException {
		
		FileVO fileVo = fileService.getFileById(id);

		// 그럼 그 id로 file을 찾아와서 걔의 경로를 적어서 이렇게 나타내주면 이미지가 잘 나옴
		return new UrlResource("file:" + fileVo.getSavedPath()); 
	}
	
	
	@RequestMapping("/project/home/select")
	public String select() {
		
		System.out.println("까꿍");
		
		return "project/home/select";
	}
	
	
	@RequestMapping("/project/home/entranceinfo")
	public String entranceinfo() {
		
		String URL = "https://cafe.naver.com/suhui?iframe_url=/ArticleList.nhn%3Fsearch.clubid=10197921%26search.menuid=2016%26search.boardtype=L";
		Connection conn = Jsoup.connect(URL);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Elements elms = doc.getElementsByClass("type_up");
		
		for(Element elm : elms) {
			String title = elm.select("a.article").text();
			System.out.println(title);
		}
		
		
		
		// System.out.println(titles.toString());
		
		/*
		for(Element title : titles) {
			
			String titleStr = title.text();
			// String href = title.attr("href");
			System.out.println("글 제목 : " + titleStr);
			// System.out.println("링크 : " + href);
		}
		*/
		
		return "project/home/entranceinfo";
	
	
	
	
	}
	
}

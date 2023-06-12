package com.koreaIT.project.controller;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreaIT.project.service.FileService;
import com.koreaIT.project.vo.FileVO;

@Controller
public class ProjectHomeController {
	
	private FileService fileService;

	@Autowired
	public ProjectHomeController(FileService fileService) {
		this.fileService = fileService;
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
	
	@RequestMapping("/project/home/select")
	public String select() {
		
		return "project/home/select";
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
	
	
	@RequestMapping("/project/home/entranceinfo")
	public String entranceinfo() {
		
		String URL = "https://cafe.naver.com/suhui?iframe_url=/ArticleList.nhn%3Fsearch.clubid=10197921%26search.menuid=2016%26search.boardtype=L";
		Document doc;
		try {
			doc = Jsoup.connect(URL).get();
			
			Elements titles = doc.select("tr.type-up");
			
			for(Element elm : titles) {
				String title = elm.select("a.article").text();
				String href = elm.select("a.article").attr("href");
				System.out.println("글 제목 : "+title);
				System.out.println("링크 : "+href);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "project/home/entranceinfo";
	}
	
	
	
	
	
}

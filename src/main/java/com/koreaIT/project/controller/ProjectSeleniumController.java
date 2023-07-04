package com.koreaIT.project.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koreaIT.project.vo.InfoArticle;
import com.koreaIT.project.vo.Rq;

@Controller
public class ProjectSeleniumController {
	
	private Rq rq;
	private String url = null;
	
	// 크롬 드라이버 저장 경로
	Path path = Paths.get("C:\\bbomi\\chromedriver\\chromedriver.exe");
	
	@Autowired
	public ProjectSeleniumController(Rq rq) {
		this.rq = rq;
	}


	/**
	 * 셀레니움으로 홈페이지 특정 게시판 자동으로 들어가서 리스트 크롤링!!
	 * @param model
	 * @return 웹크롤링 한 결과
	 * @throws InterruptedException
	 */
	@RequestMapping("/project/selenium/entranceinfo")
	public String entranceinfo(Model model) throws InterruptedException {
		
		url = "https://cafe.naver.com/suhui";

		// key와 value값으로 실행되는 곳의 정보를 얻어온다.
		// 이 함수가 기동되며 시스템 정보를 property에 담아놓고 이를 수정/사용할 수 있게함
		// 가급적 조회 용도로 사용하는 것이 좋음.
		System.setProperty("webdriver.chrome.driver", path.toString());

		// 크롬 브라우저를 가져올 때 적용할 옵션들
		ChromeOptions options = new ChromeOptions();
		
		/*
		options.addArguments("--disable-popup-blocking");   // 팝업 안띄움
        options.addArguments("headless");   // 브라우저 안띄움
        options.addArguments("--disable-gpu");  // gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false");   // 이미지 로딩 안함
        */
		options.addArguments("--remote-allow-origins=*");

		// 웹 어플리케이션을 테스팅할 때 사용할 수 있는 무료 도구.
		// API를 제공하는 오픈소스 프레임워크
		WebDriver driver = new ChromeDriver(options);

		// WebDriver을 해당 url로 이동한다.
		driver.get(url);
		
		// 해당 url로 이동할 때까지 1초동안 기다려줌
		Thread.sleep(1000);
		
		// 그 url에서 해당 element를 클릭한다
		driver.findElement(By.cssSelector("#menuLink3782")).click();

		Thread.sleep(1000);
		
		// iframe 태그에 갇혀있는 요소들 데려오려면 거기로 switch 해줘야함!!
		driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#cafe_main")));
		
		List<WebElement> contents = new ArrayList<>();
		List<InfoArticle> infoArticleList = new ArrayList<>();
		
		for(int i = 0 ; i < 5 ; i++) {
			
			// 원하는 태그들을 선택해 배열에 담아놓는다.
			contents = driver.findElements(By.cssSelector("#main-area > div:nth-child(4) > table > tbody > tr"));
				
			for(WebElement content : contents) {
				String title = content.findElement(By.cssSelector("td.td_article > div.board-list > div > a.article")).getText();
				String date = content.findElement(By.cssSelector("td.td_date")).getText();
				String url = content.findElement(By.cssSelector("td.td_article > div.board-list > div > a.article")).getAttribute("href");
				
				InfoArticle infoArticle = new InfoArticle();
				
				infoArticle.setTitle(title);
				infoArticle.setDate(date);
				infoArticle.setUrl(url);
				
				// 하나씩 꺼내서 infoarticle이라는 객체에 담아서 또 배열에 담아놓는다.
				infoArticleList.add(infoArticle);
				
			}
			
			// 그 페이지에 있는 모든 element들에서 그 동작이 끝났으면
			// 다음 페이지로 넘어가는 pagination 버튼을 누른다!!
			// 그리고 다섯 페이지에서 똑같은 동작 반복.
			driver.findElement(By.cssSelector("#main-area > div.prev-next > a.on + a")).click();

		}
		
		// 그렇게 얻은 리스트를 entranceinfo.jsp로 넘겨주기
		model.addAttribute("infoArticleList", infoArticleList);
		
		if(infoArticleList.isEmpty()) {
			rq.jsReturnOnView("웹페이지에서 정보를 가져오지 못했습니다.", true);
		}

		// driver 꺼주기
		// driver.quit();

		return "project/home/entranceinfo";
	}
	
	
	
}

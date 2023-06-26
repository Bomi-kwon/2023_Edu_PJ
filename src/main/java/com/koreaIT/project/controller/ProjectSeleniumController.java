package com.koreaIT.project.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
	Path path = Paths.get("C:\\bbomi\\chromedriver\\chromedriver.exe");
	
	@Autowired
	public ProjectSeleniumController(Rq rq) {
		this.rq = rq;
	}


	@RequestMapping("/project/selenium/entranceinfo")
	public String entranceinfo(Model model) throws InterruptedException {
		
		url = "https://cafe.naver.com/suhui";

		System.setProperty("webdriver.chrome.driver", path.toString());

		ChromeOptions options = new ChromeOptions();

		/*
		options.addArguments("--disable-popup-blocking");   // 팝업 안띄움
        options.addArguments("headless");   // 브라우저 안띄움
        options.addArguments("--disable-gpu");  // gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false");   // 이미지 다운 안받음
        */
		options.addArguments("--remote-allow-origins=*");

		WebDriver driver = new ChromeDriver(options);

		//WebDriver을 해당 url로 이동한다.
		driver.get(url);
		
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("#menuLink3782")).click();

		Thread.sleep(1000);
		
		// iframe 태그에 갇혀있는 요소들 데려오려면 거기로 switch 해줘야함!!

		driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#cafe_main")));
		List<WebElement> contents = new ArrayList<>();
		List<InfoArticle> infoArticleList = new ArrayList<>();
		
		for(int i = 0 ; i < 5 ; i++) {
			
			contents = driver.findElements(By.cssSelector("#main-area > div:nth-child(4) > table > tbody > tr"));
				
			for(WebElement content : contents) {
				String title = content.findElement(By.cssSelector("td.td_article > div.board-list > div > a.article")).getText();
				String date = content.findElement(By.cssSelector("td.td_date")).getText();
				String url = content.findElement(By.cssSelector("td.td_article > div.board-list > div > a.article")).getAttribute("href");
				
				InfoArticle infoArticle = new InfoArticle();
				
				infoArticle.setTitle(title);
				infoArticle.setDate(date);
				infoArticle.setUrl(url);
				
				infoArticleList.add(infoArticle);
				
			}
			
			driver.findElement(By.cssSelector("#main-area > div.prev-next > a.on + a")).click();

		}
		model.addAttribute("infoArticleList", infoArticleList);
		
		if(infoArticleList.isEmpty()) {
			rq.jsReturnOnView("웹페이지에서 정보를 가져오지 못했습니다.", true);
		}

		driver.quit();

		return "project/home/entranceinfo";
	}
	
	/*
	@RequestMapping("/project/selenium/entranceinfoDatail")
	public String entranceinfoDetail(Model model, String href) throws InterruptedException {
		
		url = href;

		System.setProperty("webdriver.chrome.driver", path.toString());

		ChromeOptions options = new ChromeOptions();

		
		options.addArguments("--disable-popup-blocking");   // 팝업 안띄움
        options.addArguments("headless");   // 브라우저 안띄움
        options.addArguments("--disable-gpu");  // gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false");   // 이미지 다운 안받음
        // options.setExperimentalOption("debuggerAddress", "localhost:9222");
		options.addArguments("--remote-allow-origins=*");

		WebDriver driver = new ChromeDriver(options);

		//WebDriver을 해당 url로 이동한다.
		driver.get(url);
		
		Thread.sleep(1000);
		
		// iframe 태그에 갇혀있는 요소들 데려오려면 거기로 switch 해줘야함!!

		
		
		driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#cafe_main")));
		
		WebElement body = driver.findElement(By.cssSelector("#app > div > div > div.ArticleContentBox > div.article_container > div.article_viewer > div:nth-child(2) > div.content.CafeViewer > div > div.se-main-container"));
		
		List<WebElement> texts = body.findElements(By.cssSelector("div.se-text > div > div > div > p > span"));
		
		List<String> bodyList = new ArrayList<>();
		
		for(WebElement text : texts) {
			bodyList.add(text.getText());
		}
		
		if(bodyList.isEmpty()) {
			rq.jsReturnOnView("웹페이지에서 찾아온 결과가 없습니다.", true);
		}
		
		model.addAttribute("bodyList", bodyList);
		
		driver.quit();
		
		

		return "project/home/entranceinfoDetail";
	}
	*/
	
	
}

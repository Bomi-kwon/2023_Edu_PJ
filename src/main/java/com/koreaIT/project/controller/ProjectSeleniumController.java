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
import org.openqa.selenium.support.ui.ExpectedConditions;
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
	
	@Autowired
	public ProjectSeleniumController(Rq rq) {
		this.rq = rq;
	}

	private String url = "https://cafe.naver.com/suhui";

	@RequestMapping("/project/selenium/entranceinfo")
	public String entranceinfo(Model model) throws InterruptedException {
		
		System.out.println("까꿍");

		Path path = Paths.get("C:\\bbomi\\chromedriver\\chromedriver.exe");

		System.setProperty("webdriver.chrome.driver", path.toString());

		ChromeOptions options = new ChromeOptions();

		options.addArguments("--disable-popup-blocking");   // 팝업 안띄움
        options.addArguments("headless");   // 브라우저 안띄움
        options.addArguments("--disable-gpu");  // gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false");   // 이미지 다운 안받음
		options.addArguments("--remote-allow-origins=*");

		WebDriver driver = new ChromeDriver(options);

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));

		//WebDriver을 해당 url로 이동한다.
		driver.get(url);
		
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("#menuLink3782")).click();

		Thread.sleep(1000);
		
		// iframe 태그에 갇혀있는 요소들 데려오려면 거기로 switch 해줘야함!!

		driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#cafe_main")));

		List<WebElement> contents = driver.findElements(By.cssSelector("#main-area > div:nth-child(4) > table > tbody > tr"));
		
		List<InfoArticle> infoArticleList = new ArrayList();

		
		if(contents.size() > 0) {

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

			model.addAttribute("infoArticleList", infoArticleList);

		}
		rq.jsReturnOnView("웹페이지에서 정보를 가져오지 못했습니다.", true);

		driver.quit();

		return "project/home/entranceinfo";
	}
	
	
}

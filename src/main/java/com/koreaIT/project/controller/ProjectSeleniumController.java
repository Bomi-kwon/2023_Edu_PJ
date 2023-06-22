package com.koreaIT.project.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectSeleniumController {
	
	private String url = "https://admission.snu.ac.kr/undergraduate/notice?sc=y";

	@GetMapping("/project/selenium/entranceinfo")
	public String entranceinfo() {
		String result = "";

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

		webDriverWait.until(
				ExpectedConditions.presenceOfElementLocated(By.cssSelector("#skip-content > div > div.ly-inner > div.board-list > table > tbody > tr")));

		try {
			List<WebElement> contents = driver.findElements(By.cssSelector("#skip-content > div > div.ly-inner > div.board-list > table > tbody > tr"));
			result += "서울대 입학정보 공지글 갯수 : " + contents.size() + "<br /><br />";


			if(contents.size() > 0) {

				for(WebElement content : contents) {
					String title = content.findElement(By.cssSelector("td.col-title > a > span > span")).getText();
					String date = content.findElement(By.cssSelector("td.col-date")).getText();

					String url = content.findElement(By.cssSelector("td.col-title > a")).getAttribute("href");

					result += "제목 : " + title + "<br />";
					result += "날짜 : " + date + "<br />";
					result += "공지 url : " + url + "<br /><br />";
				}


			}
		} catch (Exception e) {
			result += "elements 가져오지 못함";

		}


		driver.quit();

		return result;
	}
	
	
}

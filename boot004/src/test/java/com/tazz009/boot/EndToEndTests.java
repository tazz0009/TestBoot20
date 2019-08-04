package com.tazz009.boot;

import static org.assertj.core.api.Assertions.*;
import static org.openqa.selenium.chrome.ChromeDriverService.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTests {

	static ChromeDriverService service;

	static ChromeDriver driver;

	@LocalServerPort
	int port;
	
	@BeforeClass
	public static void setUp() throws IOException {
		System.setProperty("webdriver.chrome.driver",
			"ext/chromedriver.exe");
		service = createDefaultService();
		driver = new ChromeDriver(service);
		Path testResults = Paths.get("target", "test-results");
		if (!Files.exists(testResults)) {
			Files.createDirectory(testResults);
		}
	}

	@AfterClass
	public static void tearDown() {
		service.stop();
	}
	
	@Test
	public void homePageShouldWork() throws IOException {
		driver.get("http://localhost:" + port);

		takeScreenshot("homePageShouldWork-1");

		assertThat(driver.getTitle())
			.isEqualTo("Learning Spring Boot: Spring-a-Gram");

		String pageContent = driver.getPageSource();

		assertThat(pageContent)
			.contains("<a href=\"/images/btn_play_n.png/raw\">");

		WebElement element = driver.findElement(
			By.cssSelector("a[href*=\"btn_play_n.png\"]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();

		takeScreenshot("homePageShouldWork-2");

		driver.navigate().back();
	}
	
	private void takeScreenshot(String name) throws IOException {
		FileCopyUtils.copy(
			driver.getScreenshotAs(OutputType.FILE),
			new File("target/test-results/TEST-" + name + ".png"));
	}

}

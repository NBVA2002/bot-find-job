package com.example.botfindjob.config;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

  @Value("${topcv.username}")
  private String username;

  @Value("${topcv.password}")
  private String password;

  @Bean
  public WebDriver webDriver() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--incognito");
    WebDriver driver = new ChromeDriver(options);
    driver.manage()
        .timeouts()
//        .implicitlyWait(Duration.ofSeconds(5))
    ;
    driver.get("https://www.topcv.vn/login");
    WebElement emailInput = driver.findElement(By.xpath("//*[@id=\"form-login\"]/div[1]/div/input"));
    emailInput.sendKeys(username);
    WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"));
    passwordInput.sendKeys(password);
    WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"form-login\"]/div[4]/button"));
    loginButton.click();
    return driver;
  }

//  @Bean
//  public WebDriver webDriver() {
//    ChromeOptions options = new ChromeOptions();
//    options.addArguments("--incognito");
//    WebDriver driver = new ChromeDriver(options);
//    driver.manage()
//        .timeouts()
//        .implicitlyWait(Duration.ofSeconds(5));
//
//    driver.get("https://www.youtube.com/watch?v=x-HT2cgpkqY");
//    return driver;
//  }

}

package com.example.botfindjob.service;

import com.example.botfindjob.dto.request.ChatRequest;
import com.example.botfindjob.dto.request.JobInfomation;
import com.example.botfindjob.dto.response.ChatResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiAIService {

  private final RestTemplate restTemplate;
  private final WebDriver driver;

  @Value("${ai.google-gemini.url}")
  private String urlAI;

  @Value("${ai.google-gemini.token}")
  private String apiKey;

  @Value("${ai.mantra}")
  private String mantra;

  @Cacheable(value = "isSuitableJob", key = "#url")
  public Boolean isSuitableJob(String url, JobInfomation jobProfile) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(mantra);
    sb.append(System.lineSeparator());
    sb.append(jobProfile);
    sb.append(System.lineSeparator());
    sb.append("For the job below, please let me know if I am suitable for this job or not? \n");

    List<WebElement> contentValues = driver.findElements(
        By.cssSelector("div.job-detail__info--section-content-value"));

    JobInfomation jobInfo = new JobInfomation();
    jobInfo.setJobName(driver.findElement(By.xpath("//*[@id=\"header-job-info\"]/h1")).getText());
    jobInfo.setDesiredSalary(contentValues.get(0).getText());
    jobInfo.setLocation(contentValues.get(1).getText());
    jobInfo.setYearsOfExperience(contentValues.get(2).getText());
    jobInfo.setDescription(
        driver.findElement(By.xpath("//*[@id=\"box-job-information-detail\"]/div[2]")).getText());

    sb.append(jobInfo);

    ChatRequest request = new ChatRequest();
    ChatRequest.Content content = new ChatRequest.Content();
    ChatRequest.Part part = new ChatRequest.Part();
    part.setText(sb.toString());
    content.setParts(List.of(part));
    request.setContents(List.of(content));

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity(request, headers);
    ResponseEntity<ChatResponse> response = restTemplate.exchange(urlAI + "?key=" + apiKey,
        HttpMethod.POST, entity, ChatResponse.class);
    Boolean result = Boolean.parseBoolean(
        response.getBody().
            getCandidates().
            get(0).
            getContent().
            getParts().
            get(0).
            getText());

    log.info(jobInfo.toString());
    log.info(result.toString());
    return result;
  }
}

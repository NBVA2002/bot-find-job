package com.example.botfindjob.service;

import com.example.botfindjob.dto.request.JobInfomation;
import com.example.botfindjob.entity.JobSuitableEntity;
import com.example.botfindjob.repository.JobSuitableRepository;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebScrapingService {

  private final WebDriver driver;
  private final GeminiAIService geminiAIService;
  private final JobSuitableRepository jobSuitableRepository;
  private final ResourceLoader resourceLoader;

  public List<String> getLinks(String url) throws IOException {
    driver.get(url);
    List<String> links = new ArrayList<>();
    WebElement jobList = driver.findElement(By.cssSelector("div.job-list-2"));
    if (jobList != null) {
      List<WebElement> jobItems = jobList.findElements(By.cssSelector("div.job-item-2"));
      for (WebElement jobItem : jobItems) {
        WebElement anchor = jobItem.findElement(By.cssSelector("a[target='_blank']"));
        String link = anchor.getAttribute("href");
        if(link.matches("^https://www\\.topcv\\.vn/viec-lam/.*")){
          links.add(anchor.getAttribute("href"));
        }
      }
    }
    return links;
  }

  @Transactional
  public void submit(String url, JobInfomation jobProfile) throws IOException {
    driver.get(url);
    if(geminiAIService.isSuitableJob(url, jobProfile)){
      WebElement submittButton = driver.findElement(By.cssSelector("a.btn-apply-job"));
      submittButton.click();
      JobSuitableEntity entity = new JobSuitableEntity();
      entity.setLink(url);
      jobSuitableRepository.save(entity);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public InputStreamResource getLiveStreamResource(String youtubeUrl) {
    driver.get(youtubeUrl);
//    WebElement videoElement = driver.findElement(By.xpath("//*[@id=\"page-header\"]/yt-page-header-renderer/yt-page-header-view-model/div/div[1]/yt-decorated-avatar-view-model/yt-avatar-shape/div/div/div/img"));
    WebElement videoElement = driver.findElement(By.xpath("//*[@id=\"movie_player\"]/div[1]/video"));

    String videoUrl = videoElement.getAttribute("src");
    return urlToInputStreamResource(videoUrl);
  }

  private InputStreamResource urlToInputStreamResource(String videoUrl) {
    try {
      videoUrl = videoUrl.substring(5);
      URL url = new URL(videoUrl);
      URLConnection connection = url.openConnection();
      InputStream inputStream = connection.getInputStream();
      return new InputStreamResource(inputStream);
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch video stream", e);
    }
  }

  public Mono<Resource> getVideo(String url){
    return Mono.fromSupplier(()->resourceLoader.
        getResource(String.format("classpath:videos/%s.mp4", url)));
  }
}

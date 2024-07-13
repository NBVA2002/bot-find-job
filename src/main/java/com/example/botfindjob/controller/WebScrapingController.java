package com.example.botfindjob.controller;

import com.example.botfindjob.dto.request.JobInfomation;
import com.example.botfindjob.service.WebScrapingService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebScrapingController {

  private final WebScrapingService webScrapingService;

  @PostMapping("/scrape")
  public String scrape(@RequestParam String url, @RequestParam int pageLimit, @RequestBody JobInfomation jobProfile) {
    try {
      for (int i = 1; i < pageLimit; i++) {
        List<String> links = webScrapingService.getLinks(url + "?page=" + i);
        links.forEach(link -> {
          try {
            webScrapingService.submit(link, jobProfile);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
        System.out.println(links.toString());
        System.out.println("====================================================================" + i);
      }
      return "Scraping completed";
    } catch (IOException e) {
      return "Failed to retrieve content: " + e.getMessage();
    }
  }

  @PostMapping("/stream")
  public ResponseEntity<InputStreamSource> scrape(@RequestParam String url) {
    InputStreamResource videoStream = webScrapingService.getLiveStreamResource(url);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "video/mp4");
    return new ResponseEntity<>(videoStream, headers, HttpStatus.OK);
  }
}

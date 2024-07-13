package com.example.botfindjob.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private List<Content> contents;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Content {
      private List<Part> parts;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Part {
      private String text;
    }
}

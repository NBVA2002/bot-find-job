package com.example.botfindjob.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private List<Candidate> candidates;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Candidate {
        private Content content;
    }

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

package com.example.botfindjob.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobInfomation {
  private String jobName;
  private String desiredSalary;
  private String location;
  private String yearsOfExperience;
  private String description;
}

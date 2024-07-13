package com.example.botfindjob.repository;

import com.example.botfindjob.entity.JobSuitableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobSuitableRepository extends JpaRepository<JobSuitableEntity, Long>,
    JpaSpecificationExecutor<JobSuitableEntity>  {

}

package com.edrone.task.controller;

import com.edrone.task.models.Job;
import com.edrone.task.repository.JobRepository;
import com.edrone.task.service.JobService;
import com.edrone.task.service.FileUtility;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@RestController
public class JobController {
    private final JobRepository repository;
    private final JobService service;
    private final FileUtility fileUtils;

    @GetMapping("/jobs/{id}")
    Job getJob(@PathVariable Long id) throws SQLException, IOException {
        return repository.getJob(id);
    }

    @GetMapping("/jobs")
    List<Job> getAllJobs() throws SQLException, IOException {
        return repository.getJobs();
    }

    @GetMapping("/jobs/active")
    List<Job> getActiveJobs() throws SQLException, IOException {
        return repository.getActiveJobs();
    }

    @GetMapping("/jobs/get/{id}")
    @ResponseBody
    ResponseEntity<Object> getResults(@PathVariable long id) throws SQLException, IOException {
        return service.returnResponse(id);
    }

    @PostMapping("/jobs/send")
    ResponseEntity<Object> newJob(@RequestBody Job newJob) throws ExecutionException, InterruptedException {
        if (service.parametersCorrect(newJob).get()) {
            Thread newThread = new Thread(() -> {
                try {
                    service.generate(newJob);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            });
            newThread.start();
        }
        return new ResponseEntity(service.getResponseMessage(newJob), service.getHttpStatus(newJob));
    }

}

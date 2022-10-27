package com.edrone.task.controller;

import com.edrone.task.models.Job;
import com.edrone.task.repository.JobRepository;
import com.edrone.task.service.JobService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@RestController
public class JobController {
    private final JobRepository repository;
    private final JobService service;

    @GetMapping("/jobs/{id}")
    Job getJob(@PathVariable Long id) throws SQLException {
        return repository.getJob(id);
    }

    @GetMapping("/jobs")
    List<Job> getAllJobs() throws SQLException {
        return repository.getJobs();
    }

    @GetMapping("/jobs/active")
    List<Job> getActiveJobs() throws SQLException {
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

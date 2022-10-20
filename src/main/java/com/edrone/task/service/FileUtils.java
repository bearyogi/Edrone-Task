package com.edrone.task.service;

import com.edrone.task.models.Job;
import com.edrone.task.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;

@Service
public class FileUtils {
    private final JobRepository repository = new JobRepository();

    public File createFile(long id, LocalDate date) throws IOException {
        Files.createDirectories(Paths.get(getWorkingPath() + "/permutations"));
        return new File(getWorkingPath() + "/permutations/permutation_" + id + "_" + date.toString().replaceAll("-", "_") + ".txt");
    }

    public String getFileName(long id) throws SQLException, IOException {
        Job job = repository.getJob(id);
        return "permutation_" + id + "_" + job.getDate().toString().replaceAll("-", "_") + ".txt";
    }

    public File getFile(long id) throws SQLException, IOException {
        Job job = repository.getJob(id);
        return new File(getWorkingPath() + "/permutations/permutation_" + id + "_" + job.getDate().toString().replaceAll("-", "_") + ".txt");
    }

    public String getWorkingPath() {
        return System.getProperty("user.dir").replaceAll(Matcher.quoteReplacement("\\"), "/");
    }
}

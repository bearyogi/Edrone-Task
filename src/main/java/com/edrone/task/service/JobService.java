package com.edrone.task.service;

import com.edrone.task.models.Job;
import com.edrone.task.repository.JobRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.edrone.task.utility.MathUtils.fact;
import static com.edrone.task.utility.PermUtils.findPermutations;
import static com.edrone.task.utility.PermUtils.findSubPermutations;

@Service
public class JobService {
    private final JobRepository repository = new JobRepository();
    private final FileUtility fileUtils = new FileUtility();
    public JobService() throws SQLException {
    }

    @Async
    public void generate(Job job) throws SQLException, IOException {
        TreeSet<String> result = new TreeSet<>();
        long id = getId();
        job.setActive(true);
        job.setDate(LocalDate.now());
        repository.addJob(job);
        findPermutations(job.getPossibleChars(), 0, job.getPossibleChars().length(), result);
        result = findSubPermutations(job.getMinLength(), job.getMaxLength(), result, job.getStringAmount());
        job.setActive(false);
        repository.updateJob(job);
        saveToFile(id, job.getDate(), result);
    }

    @Async
    public void saveToFile(long id, LocalDate date, TreeSet<String> strings) throws IOException {
        File file = fileUtils.createFile(id, date);
        int counter = 0;
        if (file.createNewFile()) {
            FileOutputStream outputStream = new FileOutputStream(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            for (String str : strings) {
                counter++;
                bufferedWriter.write(str);
                if (counter < strings.size()) {
                    bufferedWriter.newLine();
                }

            }
            bufferedWriter.close();
            outputStream.close();
        }
    }

    @Async
    CompletableFuture<Integer> permute(int n, int r) {
        return CompletableFuture.completedFuture(fact(n) / fact(n - r));
    }

    @Async
    public CompletableFuture<Integer> findNumberOfPermutations(int min, int max) throws ExecutionException, InterruptedException {
        int sum = 0;
        int p;
        for (int i = min; i <= max; i++) {
            p = permute(max, i).get();
            sum = sum + p;
        }
        return CompletableFuture.completedFuture(sum);
    }

    @Async
    public CompletableFuture<Boolean> parametersCorrect(Job job) throws ExecutionException, InterruptedException {
        boolean result;
        result = job.getMinLength() > 0;
        result &= job.getMinLength() <= job.getMaxLength();
        result &= job.getStringAmount() >= 0;
        result &= !job.getPossibleChars().trim().isEmpty();
        result &= findNumberOfPermutations(job.getMinLength(), job.getMaxLength()).get() >= job.getStringAmount();
        result &= job.getMinLength() != 0;
        result &= job.getMaxLength() != 0;
        result &= job.getStringAmount() != 0;
        result &= job.getPossibleChars() != null;
        return CompletableFuture.completedFuture(result);
    }
    public ResponseEntity<Object> returnResponse(long id) throws SQLException, IOException {
        File file = fileUtils.getFile(id);
        String name = fileUtils.getFileName(id);
        Job job = repository.getJob(id);
        if(job != null && job.getDate() != null) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }else{
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("File not found");
        }
    }
    public String getResponseMessage(Job job) throws ExecutionException, InterruptedException {
        if(job.getMinLength() == 0 || job.getMaxLength() == 0 || job.getStringAmount() == 0 || job.getPossibleChars() == null){
            return "You must fill all required parameters.";
        }
        if (job.getMinLength() < 0) {
            return "Minimal length of permutation cannot be negative number.";
        } else if (job.getMinLength() > job.getMaxLength()) {
            return "Minimal length of permutation must be smaller or equal to maximum.";
        } else if (job.getStringAmount() < 0) {
            return "Number of returned permutations cannot be negative number.";
        } else if  (job.getPossibleChars().trim().isEmpty()) {
            return "You must enter characters to permute from.";
        } else if (findNumberOfPermutations(job.getMinLength(), job.getMaxLength()).get() < job.getStringAmount()) {
            return "Cannot create that much permutations with given chars.";
        }
        return "Creating permutations.";
    }

    public HttpStatus getHttpStatus(Job job) throws ExecutionException, InterruptedException {
        if (parametersCorrect(job).get()) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    private long getId() throws SQLException {
        long id = 0;
        List<Job> jobs = repository.getJobs();
        for (Job jb : jobs) {
            id = jb.getId();
        }
        return id + 1;
    }
}

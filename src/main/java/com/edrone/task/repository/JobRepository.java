package com.edrone.task.repository;

import com.edrone.task.models.Job;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import org.springframework.stereotype.Repository;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;

@Repository
public class JobRepository {
    Dao<Job, Long> jobDao;
    JdbcPooledConnectionSource connectionSource;
    public JobRepository() throws SQLException {
        String url = "jdbc:sqlite:" + getWorkingPath() + "/Strings.db";
        connectionSource = new JdbcPooledConnectionSource(url);
        jobDao = DaoManager.createDao(connectionSource, Job.class);
    }

    public void addJob(Job job) throws SQLException {
        jobDao.create(job);
    }

    public void updateJob(Job job) throws SQLException {
        jobDao.update(job);
    }

    public void deleteJob(long id) throws SQLException {
        jobDao.deleteById(id);
    }

    public Job getJob(long id) throws SQLException {
        Job job;
        job = jobDao.queryForId(id);
        return job;
    }

    public List<Job> getJobs() throws SQLException {
        List<Job> jobs;
         jobs = jobDao.queryForAll();
        return jobs;
    }
    public List<Job> getActiveJobs() throws  SQLException {
        List<Job> jobs;
        jobs = jobDao.queryForEq("active",true);
        return jobs;
    }

    private String getWorkingPath() {
        return System.getProperty("user.dir").replaceAll(Matcher.quoteReplacement("\\"), "/");
    }
}

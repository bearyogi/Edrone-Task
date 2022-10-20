package com.edrone.task.repository;

import com.edrone.task.models.Job;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;

@Repository
public class JobRepository {
    private final String url;
    Dao<Job, Long> jobDao;

    public JobRepository(){
        url = "jdbc:sqlite:" + getWorkingPath() + "/Strings.db";
    }

    public void addJob(Job job) throws SQLException, IOException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(url);
        jobDao = DaoManager.createDao(connectionSource, Job.class);
        jobDao.create(job);
        connectionSource.close();
    }

    public void updateJob(Job job) throws SQLException, IOException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(url);
        jobDao = DaoManager.createDao(connectionSource, Job.class);
        jobDao.update(job);
        connectionSource.close();
    }

    public void deleteJob(long id) throws SQLException, IOException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(url);
        jobDao = DaoManager.createDao(connectionSource, Job.class);
        jobDao.deleteById(id);
        connectionSource.close();
    }

    public Job getJob(long id) throws SQLException, IOException {
        Job job;
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(url);
        jobDao = DaoManager.createDao(connectionSource, Job.class);
        job = jobDao.queryForId(id);
        connectionSource.close();
        return job;
    }

    public List<Job> getJobs() throws SQLException, IOException {
        List<Job> jobs;
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(url);
        jobDao = DaoManager.createDao(connectionSource, Job.class);
         jobs = jobDao.queryForAll();
        connectionSource.close();
        return jobs;
    }
    public List<Job> getActiveJobs() throws  SQLException, IOException {
        List<Job> jobs;
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(url);
        jobDao = DaoManager.createDao(connectionSource, Job.class);
        jobs = jobDao.queryForEq("active",true);
        connectionSource.close();
        return jobs;
    }

    private String getWorkingPath() {
        return System.getProperty("user.dir").replaceAll(Matcher.quoteReplacement("\\"), "/");
    }
}

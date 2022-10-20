package com.edrone.task.database;

import com.edrone.task.models.Job;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Matcher;

public class Database {
    private final String url;
    private final String databaseName = "Strings";

    public Database( ) {
        url = "jdbc:sqlite:" + getWorkingPath() + "/" + databaseName + ".db";
    }

    public void createNewDatabase() throws IOException {
        if (checkIfPathAbsent("")) {
            Files.createDirectories(Paths.get(getWorkingPath()));
        }

        if (checkIfPathAbsent(databaseName)) {

            try {

                Connection conn = DriverManager.getConnection(url.substring(0, url.length() - 3));
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void createJobTable() throws SQLException, IOException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(url);
        TableUtils.createTableIfNotExists(connectionSource, Job.class);
        connectionSource.close();
    }

    private boolean checkIfPathAbsent(String file) {
        return !Files.exists(Paths.get(getWorkingPath() + "/" + file));
    }

    private String getWorkingPath() {
        return System.getProperty("user.dir").replaceAll(Matcher.quoteReplacement("\\"), "/");

    }
}

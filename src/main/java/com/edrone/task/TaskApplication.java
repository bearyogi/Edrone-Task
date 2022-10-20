package com.edrone.task;

import com.edrone.task.database.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TaskApplication {

	public static void main(String[] args) throws IOException, SQLException {
		SpringApplication.run(TaskApplication.class, args);
		Database database = new Database();
		database.createNewDatabase();
		database.createJobTable();
	}


}

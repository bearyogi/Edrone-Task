package com.edrone.task.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DatabaseTable(tableName = "jobs")
public class Job {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false, dataType= DataType.SERIALIZABLE)
    private LocalDate date;

    @DatabaseField(canBeNull = false)
    private int minLength;

    @DatabaseField(canBeNull = false)
    private int maxLength;

    @DatabaseField(canBeNull = false)
    private long stringAmount;

    @DatabaseField(canBeNull = false)
    private String possibleChars;

    @DatabaseField(canBeNull = false)
    private boolean active;

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", date=" + date +
                ", minLength=" + minLength +
                ", maxLength=" + maxLength +
                ", stringAmount=" + stringAmount +
                ", possibleChars='" + possibleChars + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}

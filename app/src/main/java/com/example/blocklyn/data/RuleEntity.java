package com.example.blocklyn.data;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rules")
public class RuleEntity {
    @PrimaryKey(autoGenerate = true) public int id;
    public String packageName;
    public long startHour, endHour;
    public boolean active;
}

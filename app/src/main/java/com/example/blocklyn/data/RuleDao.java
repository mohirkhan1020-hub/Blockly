package com.example.blocklyn.data;
import androidx.room.*;
import java.util.List;

@Dao
public interface RuleDao {
    @Query("SELECT * FROM rules WHERE active = 1")
    List<RuleEntity> getActiveRules();
    @Insert void insert(RuleEntity rule);
    @Update void update(RuleEntity rule);
}

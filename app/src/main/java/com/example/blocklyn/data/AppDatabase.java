package com.example.blocklyn.data;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RuleEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RuleDao ruleDao();
    private static AppDatabase INSTANCE;
    public static synchronized AppDatabase get(Context c) {
        if (INSTANCE == null) INSTANCE = Room.databaseBuilder(c,
            AppDatabase.class, "blocklyn.db").build();
        return INSTANCE;
    }
}

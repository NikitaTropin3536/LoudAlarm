package com.example.loudalarm.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDAO {
    @Query("SELECT * FROM AlarmEntity")
    List<AlarmEntity> getAll();

    @Insert
    void save(AlarmEntity alarm);

    @Insert
    void saveAll(List<AlarmEntity> list);

    @Query("SELECT * FROM AlarmEntity WHERE id = :id")
    AlarmEntity get(int id);

    @Query("SELECT music FROM AlarmEntity WHERE id = :id")
    String getMusicUriInString(int id);

    @Query("DELETE FROM AlarmEntity")
    void deleteAll();

    @Update
    void updateAll(List<AlarmEntity> data);

    @Update
    void update(AlarmEntity alarm);
}

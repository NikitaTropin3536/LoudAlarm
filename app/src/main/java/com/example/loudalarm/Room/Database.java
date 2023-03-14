package com.example.loudalarm.Room;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {AlarmEntity.class}, version = 2)
public abstract class Database extends RoomDatabase {
    public abstract AlarmDAO alarmDAO();


}

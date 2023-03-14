package com.example.loudalarm;

import android.app.Application;
import android.net.Uri;

import androidx.room.Room;

import com.example.loudalarm.Room.Database;
import com.example.loudalarm.Sp.DatabaseSP;


public class App extends Application {

    public static String ID = "id_alarm";
    public static String URI_SP_NAME = "uri";
    public static int SAVE_URI = 1234;
    private static App instance;
    public static Uri defaultMusicUri;

    private static Database database;

    public static DatabaseSP databaseSP;

    public static Uri getDefaultMusicUri() {
        return databaseSP.get();
    }
    public static void setDefaultMusicUri(Uri uri) {
        defaultMusicUri = uri;
        databaseSP.save(defaultMusicUri);
    }

    public static App getInstance() {
        return instance;
    }
    public static Database getDatabase() {
        return database;
    }


    @Override
    public void onCreate() {
        instance = this;
        databaseSP = new DatabaseSP(instance);
        database = Room.databaseBuilder(this, Database.class, "database.db").fallbackToDestructiveMigration().build();

        super.onCreate();
    }
}

package com.example.loudalarm.Sp;

import static com.example.loudalarm.App.URI_SP_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;

public class DatabaseSP implements SPDatabase {
    private final SharedPreferences storage;

    public DatabaseSP(Context context) {
        storage = context.getSharedPreferences("storage", Context.MODE_PRIVATE);
    }


    @Override
    public void save(Uri uri) {
        SharedPreferences.Editor editor = storage.edit();
        editor.putString(URI_SP_NAME, uri.toString());
        editor.apply();
    }

    @Override
    public Uri get() {
        return Uri.parse(storage.getString(URI_SP_NAME, String.valueOf(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL))));
    }

}
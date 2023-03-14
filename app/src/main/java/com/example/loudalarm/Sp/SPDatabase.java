package com.example.loudalarm.Sp;

import android.net.Uri;

public interface SPDatabase {
    void save(Uri uri);

    Uri get();

}

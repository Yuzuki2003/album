package com.minami.album;

import android.net.Uri;

import com.orm.SugarRecord;

import java.util.List;

public class Album extends SugarRecord {
    List ListPictures;
    String title;
    Uri path;

    public Album() {
    }

    public Album(List ListPictures, String title) {
        this.ListPictures = ListPictures;
        this.title = title;
    }
}
package com.minami.album;

import android.net.Uri;

import com.orm.SugarRecord;

import java.util.List;

public class Album extends SugarRecord {
    List<Uri> listPictures;
    String title;



    public Album() {
    }

    public Album(List ListPictures, String title) {
        this.listPictures = ListPictures;
        this.title = title;
    }
}
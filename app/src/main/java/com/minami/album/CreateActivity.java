package com.minami.album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    private ImageButton imagebutton;
    private List<Uri> photoList = new ArrayList<>();

    AlbumRecyclerAdaptar adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ImageButton button = findViewById(R.id.imageButton);
        imagebutton = findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGallery;
                if (Build.VERSION.SDK_INT < 19) {
                    intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
                    intentGallery.setType("image/*");
                } else {
                    intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
                    intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
                    intentGallery.setType("image/jpeg");
                }
                startActivityForResult(intentGallery, GALLERY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        Album album = new Album(photoList, "Sample Title");
        adapter = new AlbumRecyclerAdaptar(album, getApplicationContext(), getLayoutInflater());
        recyclerView.setAdapter(adapter);
    }

    private static final int GALLERY_REQUEST_CODE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        Uri uri;
        String filePath = "";
        imagebutton = findViewById(R.id.imageButton);
        switch (requestCode) {
            case (GALLERY_REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    try {
                        if (resultData.getData() != null) {
                            photoList.add(resultData.getData());
                            adapter.updatePhoto(resultData.getData());
                            InputStream is = getContentResolver().openInputStream(resultData.getData());
                            Bitmap bmp = BitmapFactory.decodeStream(is);
                            is.close();
                            imagebutton.setImageBitmap(bmp);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                }
                break;

        }
    }
}

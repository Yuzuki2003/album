package com.minami.album;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button button = findViewById(R.id.button);
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

                startActivityForResult(intentGallery,REQUEST_CODE);
            }
        });


    }

    private static final int REQUEST_CODE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultDate) {
        switch (requestCode) {
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                } else if (resultCode == RESULT_CANCELED) {
                }
        }
    }
}

package com.minami.album;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    private ImageButton photoButton;
    private Uri resultUri;
    private static final int REQUEST_CHOOSER = 1000;
    private final static int REQUEST_PERMISSION = 1002;
    private String filePath;
    private Uri cameraUri;
    private File cameraFile;
    private Intent intentCamera;
    private ImageView photo;
    private ImageView photo2;
    float x;
    float y;
    boolean flag = false;
    boolean change = true;
    RelativeLayout relativelayout;
    FrameLayout frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        photoButton = (ImageButton) findViewById(R.id.photo_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _showGallery();
            }
        });

        photo = (ImageView)findViewById(R.id.photo);
        photo2 = (ImageView)findViewById(R.id.photo2);
        frame = (FrameLayout) findViewById(R.id.framelayout);
        photo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("photo","Drag");
                view.startDrag(data,new View.DragShadowBuilder(view),(Object)view,0);
                return false;
            }
        });

        photo2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("photo2","Drag");
                view.startDrag(data,new View.DragShadowBuilder(view),(Object)view,0);
                return false;
            }
        });

        relativelayout = (RelativeLayout)findViewById(R.id.relativelayout);
        relativelayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch(dragEvent.getAction()){
                    //case DragEvent.ACTION_DRAG_EXITED:
                    // flag = false;
                    //break;
                    case DragEvent.ACTION_DROP:
                        x = dragEvent.getX();
                        y = dragEvent.getY();
                        break;
                    //case DragEvent.ACTION_DRAG_ENTERED:
                    //  flag = true;
                    //break;
                    default:
                        break;

                }
                return false;
            }
        });


    }
    public void addView (int photoNum){
        FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(180,180);
        ImageView image = new ImageView(getApplicationContext());
        image.setImageResource(getResources().getIdentifier("photo" + photoNum,"drawable",getPackageName()));

        frame.addView(image, params);
    }



    private void _showGallery() {

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }
        else {
            intentCamera = cameraIntent(); //cameraIntentというIntentを返す
        }

        Intent intentGallery;
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
            intentGallery.setType("image/*");
        } else {
            intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
            intentGallery.setType("image/jpeg");
        }

        Intent intent = Intent.createChooser(intentGallery, "Select Image");
        if(intentCamera!=null){
            intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {intentCamera});
        }
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CHOOSER) {

            if(resultCode != RESULT_OK) {
                return ;
            }
            resultUri = (data != null ? data.getData() : cameraUri);

            if(resultUri == null) {
                Toast.makeText(this, "Error.Try again.", Toast.LENGTH_LONG).show();
                return;
            }
            MediaScannerConnection.scanFile(
                    this,
                    new String[]{resultUri.getPath()},
                    new String[]{"image/jpeg"},
                    null
            );

            if (change){
                ImageView imageView = (ImageView)findViewById(R.id.photo);
                imageView.setImageURI(resultUri);
                imageView.setVisibility(View.VISIBLE);
                change = false;
            } else if (!change){
                ImageView imageView2 = (ImageView)findViewById(R.id.photo2);
                imageView2.setImageURI(resultUri);
                imageView2.setVisibility(View.VISIBLE);
            }


        }
    }

    private Intent cameraIntent(){
        File cameraFolder = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IMG"
        );
        cameraFolder.mkdirs();

        String fileName = new SimpleDateFormat("ddHHmmss").format(new Date());
        filePath = cameraFolder.getPath() +"/" + fileName + ".jpg";
        Log.d("debug","filePath:"+filePath);


        cameraFile = new File(filePath);
        cameraUri = Uri.fromFile(cameraFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);

        return intent;
    }
    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            cameraIntent();
        }
        else{
            requestLocationPermission();
        }
    }
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(CreateActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this, "Camera function is disabled", Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, REQUEST_PERMISSION);
        }

    }
}

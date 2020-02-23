package com.example.recognizetext;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.jpegkit.Jpeg;

public class CameraKitActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;

    TextView textView;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_kit);

        cameraKitView = findViewById(R.id.camera);

        textView=findViewById(R.id.take_image);

        textView.setOnClickListener(photoOnClickListener);
    }


    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                @Override
                public void onImage(CameraKitView view, final byte[] photo) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final Jpeg jpeg = new Jpeg(photo);
                            imageView.post(new Runnable() {
                                @Override
                                public void run() {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                                    imageView.setImageBitmap(bitmap);

                                    Toast.makeText(CameraKitActivity.this,"ok",Toast.LENGTH_SHORT).show();

                                   // imageView.setB;
                                }
                            });
                        }
                    }).start();
                }
            });
        }
    };


}

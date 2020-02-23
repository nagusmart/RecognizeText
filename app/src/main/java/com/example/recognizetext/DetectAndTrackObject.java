package com.example.recognizetext;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class DetectAndTrackObject extends AppCompatActivity {


    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    TextureView textureView;

    ImageView image;


    FirebaseVisionTextRecognizer detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        textureView = findViewById(R.id.view_finder);

        image=findViewById(R.id.imgCapture);




      /*  if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }*/

      image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              btn_gallery(v);

          }
      });



    }




    public void btn_gallery(View view) {

        Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100 && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();

            try {

               /* InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);*/
            //    selectedImage.setConfig(Bitmap.Config.ARGB_8888);

              /*  BitmapFactory.Options bitmapFatoryOptions=new BitmapFactory.Options();
                bitmapFatoryOptions.inPreferredConfig= Bitmap.Config.RGB_565;
               Bitmap mybitmapss=BitmapFactory.decodeResource(getResources(), R.drawable.women,bitmapFatoryOptions);*/

              //  firebaseVisionImage = FirebaseVisionImage.fromFilePath(FaceDetectionActivity.this, uri);
                FirebaseVisionImage firebaseVisionImage;

                firebaseVisionImage = FirebaseVisionImage.fromFilePath(DetectAndTrackObject.this,uri);

                image.setImageURI(uri);

                recognizeText(firebaseVisionImage);

            }catch (Exception e){
                Log.e("error=",e.toString());
            }


        }
    }

    public void recognizeText(FirebaseVisionImage firebaseVisionImage){



        // Live detection and tracking

        FirebaseVisionObjectDetectorOptions options =
                new FirebaseVisionObjectDetectorOptions.Builder()
                        .setDetectorMode(FirebaseVisionObjectDetectorOptions.STREAM_MODE)
                        .enableClassification()  // Optional
                        .build();

// Multiple object detection in static images
        FirebaseVisionObjectDetectorOptions options1 =
                new FirebaseVisionObjectDetectorOptions.Builder()
                        .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()  // Optional
                        .build();

        FirebaseVisionObjectDetector objectDetector =
                FirebaseVision.getInstance().getOnDeviceObjectDetector();

        Task<List<FirebaseVisionObject>> result =
                objectDetector.processImage(firebaseVisionImage)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionObject>>() {
                                    @Override

                                    public void onSuccess(List<FirebaseVisionObject> detectedObjects) {
                                        for (FirebaseVisionObject obj : detectedObjects) {

                                            Integer id = obj.getTrackingId();
                                            Rect bounds = obj.getBoundingBox();

                                            // If classification was enabled:
                                            int category = obj.getClassificationCategory();
                                            Float confidence = obj.getClassificationConfidence();

                                            Toast.makeText(DetectAndTrackObject.this,obj.toString(),Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
      /*  Task<FirebaseVisionText> result =
                detector.processImage(firebaseVisionImage)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                // ...

                                Toast.makeText(FaceDetectionActivity.this,firebaseVisionText.getText().toString(),Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
*/
    }

}

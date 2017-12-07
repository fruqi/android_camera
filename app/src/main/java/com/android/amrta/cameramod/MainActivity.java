package com.android.amrta.cameramod;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageSurfaceView imageSurfaceView;
    private Camera camera;
    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        capturedImageHolder = (ImageView) findViewById(R.id.captured_image);

        camera = checkDeviceCamera();
        imageSurfaceView = new ImageSurfaceView(this, camera);
        cameraPreviewLayout.addView(imageSurfaceView);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//            if (bitmap == null) {
//                Toast.makeText(MainActivity.this, "Where the f is the picture, bro!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            bitmap = scaledDownBitmap(bitmap, 300, 200);
//
//            capturedImageHolder.setImageBitmap(bitmap);



            if (data != null) {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Notice that width and height are reversed
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
                    int w = scaled.getWidth();
                    int h = scaled.getHeight();
                    // Setting post rotate to 90
                    Matrix mtx = new Matrix();
                    mtx.postRotate(90);
                    // Rotating Bitmap
                    bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                }else{// LANDSCAPE MODE
                    //No need to reverse width and height
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth,screenHeight , true);
                    bm=scaled;
                }

                capturedImageHolder.setImageBitmap(bm);
            }

//            isImageCaptured = true;
//            photoPreview.setVisibility(View.VISIBLE);
//            surfaceView.setVisibility(View.GONE);
        }
    };

    private Bitmap scaledDownBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private Camera checkDeviceCamera() {
        Camera camera1 = null;

        try {
            camera1 = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return camera1;
    }
}

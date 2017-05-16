package com.jiajie.qrscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class CameraActivity extends AppCompatActivity {

    private CameraPreview camPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camPreview = new CameraPreview(this);
        FrameLayout frame = (FrameLayout) findViewById(R.id.cameraLayout);
        frame.addView(camPreview);
    }
}

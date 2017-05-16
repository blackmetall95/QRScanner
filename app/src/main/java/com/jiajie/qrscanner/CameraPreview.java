package com.jiajie.qrscanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.dtksoft.barreader.Barcode;
import com.dtksoft.barreader.BarcodeOrientationEnum;
import com.dtksoft.barreader.BarcodeReader;
import com.dtksoft.barreader.BarcodeTypeEnum;
import com.dtksoft.barreader.QuietZoneSizeEnum;
import com.dtksoft.barreader.ThresholdModeEnum;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private BarcodeReader barReader;
    private Camera mCamera;
    private TextView txtBox;
    private CameraActivity camActivity;
    private SurfaceHolder mSurfaceHolder;

    @SuppressWarnings("depreciations")
    public CameraPreview(Context context){
        super(context);

        this.camActivity = (CameraActivity)context;
        this.txtBox = (TextView)camActivity.findViewById(R.id.txtView1);

        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);

        barReader = new BarcodeReader(context);
        barReader.setThresholdMode(ThresholdModeEnum.TM_Automatic);
        barReader.setThreshold(128);
        barReader.setScanInterval(2);
        barReader.setQuietZoneSize(QuietZoneSizeEnum.QZ_Small);
        barReader.setBarcodeOrientation(EnumSet.of(BarcodeOrientationEnum.BO_All));
        barReader.setBarcodeTypes(EnumSet.of(BarcodeTypeEnum.BT_QRCode));
    }

    private Camera.PreviewCallback mPreviewListener = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.d("DTK", "onPreviewFrame");
            try {
                Camera.Parameters params = camera.getParameters();
                YuvImage frame = new YuvImage(data, params.getPreviewFormat(), params.getPreviewSize().width,
                        params.getPreviewSize().height, null);

                barReader.ReadFromYuvImage(frame);

                List<Barcode> barcodes = barReader.getBarcodes();
                if (barcodes.size() > 0) {
                    txtBox.setText(barcodes.get(0).barcodeString);
                }
                frame = null;
            } catch (Exception e) {
                Log.d("DTK", e.getMessage());
            }
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.setPreviewCallback(mPreviewListener);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera=null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();

        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = getOptimalPreviewSize(sizes, width,height);
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);

        setCameraDisplayOrientation(camActivity, 0, mCamera);

        mCamera.setParameters(parameters);
        mCamera.startPreview();

        AutoFocusCallbackImpl autoFocusCallback = new AutoFocusCallbackImpl();
        mCamera.autoFocus(autoFocusCallback);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double)w/h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        //Try to find a size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width /size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        //Cannot find a match for the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId,
                                                   Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {//back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private class AutoFocusCallbackImpl implements Camera.AutoFocusCallback {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            AutoFocusCallbackImpl autoFocusCallBack = new AutoFocusCallbackImpl();
            mCamera.autoFocus(autoFocusCallBack);
        }
    }
}

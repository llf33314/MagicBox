package com.gt.magicbox.setting;

import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.io.IOException;

/**
 * Description:
 * Created by jack-lin on 2017/8/30 0030.
 */

public class ScanCodeTest  extends BaseActivity{
//    private Camera mCamera;
//    private CameraPreview mPreview;
//    private Handler autoFocusHandler;
//    private CameraManager mCameraManager;
//    private FrameLayout scanPreview;
//    private Button scanRestart;
//    private RelativeLayout scanContainer;
//    private Rect fillRect = null;
//
//    private boolean barcodeScanned = false;
//    private boolean previewing = true;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setToolBarTitle("扫码测试");
//        setContentView(R.layout.activity_scan_test);
//        initCameraViews();
//    }
//
//    @Override
//    protected void onPause() {
//        releaseCamera();
//        super.onPause();
//    }
//
//    private void initCameraViews() {
//        scanContainer=(RelativeLayout)findViewById(R.id.container);
//        scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
//
//        autoFocusHandler = new Handler();
//        mCameraManager = new CameraManager(this);
//        try {
//            mCameraManager.openDriver();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        int cameraWidth = mCameraManager.getCameraResolution().y;
//        int cameraHeight = mCameraManager.getCameraResolution().x;
//
//        mCamera = mCameraManager.getCamera();
//        openFlashLight(mCamera);
//        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
//
//        fillRect=new Rect(0,0,cameraWidth,cameraHeight);
//        scanPreview.addView(mPreview);
//
//
//    }
//    private void openFlashLight(Camera m_Camera){
//        try{
//            Camera.Parameters mParameters;
//            mParameters = m_Camera.getParameters();
//            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            m_Camera.setParameters(mParameters);
//        } catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//    private void closeFlashLight(Camera m_Camera){
//        try{
//            Camera.Parameters mParameters;
//            mParameters = m_Camera.getParameters();
//            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//            m_Camera.setParameters(mParameters);
//            m_Camera.release();
//        } catch(Exception ex){}
//    }
//
//    private void releaseCamera() {
//        Log.i("camera","releaseCamera");
//        if (mCamera != null) {
//            previewing = false;
//            mCamera.setPreviewCallback(null);
//            mCamera.stopPreview();
//            //releaseCamera();
//            barcodeScanned = true;
//            previewing = false;
//            mCamera.setPreviewCallback(null);
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//
//    private Runnable doAutoFocus = new Runnable() {
//        public void run() {
//            if (previewing)
//                mCamera.autoFocus(autoFocusCB);
//        }
//    };
//
//    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
//        public void onPreviewFrame(byte[] data, Camera camera) {
//            Camera.Size size = camera.getParameters().getPreviewSize();
//
//            byte[] rotatedData = new byte[data.length];
//            for (int y = 0; y < size.height; y++) {
//                for (int x = 0; x < size.width; x++)
//                    rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
//            }
//
//            int tmp = size.width;
//            size.width = size.height;
//            size.height = tmp;
//
//            ZBarDecoder zBarDecoder = new ZBarDecoder();
//            String result = zBarDecoder.decodeCrop(rotatedData, size.width, size.height, fillRect.left, fillRect.top, fillRect.width(), fillRect.height());
//
//            if (!TextUtils.isEmpty(result)) {
//                ToastUtil.getInstance().showToast(""+result);
//
//            }
//        }
//    };
//    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
//        public void onAutoFocus(boolean success, Camera camera) {
//            autoFocusHandler.postDelayed(doAutoFocus, 1000);
//        }
//    };
}

package com.gt.magicbox.camera;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.obsessive.zbar.CameraPreview;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.IOException;

/**
 * Description:
 * Created by jack-lin on 2017/9/29 0029.
 * Buddha bless, never BUG!
 */

public class ZBarCameraManager {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private com.obsessive.zbar.CameraManager mCameraManager;
    private boolean previewing = true;


    private boolean isHandleData = true;
    private ImageScanner mImageScanner = null;
    private Rect fillRect = null;
    private Context context;
    private FrameLayout scanPreview;


    private OnScanCodeCallBack onScanCodeCallBack;
    public ZBarCameraManager(Context context,FrameLayout scanPreview){
        this.context=context;
        this.scanPreview=scanPreview;
        initCameraViews();
    }

    private void initCameraViews() {
        mImageScanner = new ImageScanner();
        mImageScanner.setConfig(0, Config.X_DENSITY, 3);
        mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

        autoFocusHandler = new Handler();
        mCameraManager = new com.obsessive.zbar.CameraManager(context);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCameraManager.openFlashLight();
        mCamera = mCameraManager.getCamera();

        mPreview = new CameraPreview(context, mCamera, previewCb, autoFocusCB);
        scanPreview.addView(mPreview);

    }

    public void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };
    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (!isHandleData)return;
            Camera.Size size = camera.getParameters().getPreviewSize();

            fillRect = new Rect(0, 0, size.width, size.height);
            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);
            barcode.setCrop(fillRect.left, fillRect.top, fillRect.width(),
                    fillRect.height());

            int result = mImageScanner.scanImage(barcode);
            String resultStr = null;

            if (result != 0) {
                SymbolSet syms = mImageScanner.getResults();
                for (Symbol sym : syms) {
                    resultStr = sym.getData();
                }
            }

            if (!TextUtils.isEmpty(resultStr)) {
                if (null!=onScanCodeCallBack) {
                    onScanCodeCallBack.scanResult(resultStr);
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
    public interface  OnScanCodeCallBack{
        void scanResult(String result);
    }

    public void setOnScanCodeCallBack(OnScanCodeCallBack onScanCodeCallBack) {
        this.onScanCodeCallBack = onScanCodeCallBack;
    }

    public void setHandleData(boolean handleData) {
        isHandleData = handleData;
    }


}

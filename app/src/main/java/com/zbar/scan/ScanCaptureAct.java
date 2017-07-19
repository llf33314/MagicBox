package com.zbar.scan;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtr.zbar.build.ZBarDecoder;
import com.gt.magicbox.R;
import com.gt.magicbox.webview.WebViewActivity;

import java.io.IOException;
import java.lang.reflect.Field;

@SuppressWarnings("deprecation")
public class ScanCaptureAct extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private CameraManager mCameraManager;

	private FrameLayout scanPreview;
	private Button scanRestart;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	//private ImageView scanLine;

	private Rect mCropRect = null;
	private boolean barcodeScanned = false;
	private boolean previewing = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zbar_scan_capture);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		findViewById();
		addEvents();
		initViews();
	}

	private void findViewById() {
		scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
		scanRestart = (Button) findViewById(R.id.capture_restart_scan);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		//scanLine = (ImageView) findViewById(R.id.capture_scan_line);
	}

	private void addEvents() {
		scanRestart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (barcodeScanned) {
					barcodeScanned = false;
					//scanResult.setText("请将二维码对准扫框内");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}
				
			}
		});
	}

	private void initViews() {
		autoFocusHandler = new Handler();
		mCameraManager = new CameraManager(this);
		try {
			mCameraManager.openDriver();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Display display = this.getWindowManager().getDefaultDisplay();
	    int width = display.getWidth();
	    int height = display.getHeight();
	    
		RelativeLayout.LayoutParams linearParams =  (RelativeLayout.LayoutParams)scanCropView.getLayoutParams();
        linearParams.height = (int) (width*0.8);
        linearParams.width = (int) (width*0.8);
        scanCropView.setLayoutParams(linearParams);
		
		mCamera = mCameraManager.getCamera();
		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		
		scanPreview.addView(mPreview);

		/*TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				0.85f);
		animation.setDuration(5000);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.REVERSE);
		scanLine.startAnimation(animation);*/
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	private void releaseCamera() {
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

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Size size = camera.getParameters().getPreviewSize();

			byte[] rotatedData = new byte[data.length];
			for (int y = 0; y < size.height; y++) {
				for (int x = 0; x < size.width; x++)
					rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
			}

			int tmp = size.width;
			size.width = size.height;
			size.height = tmp;

			initCrop();
			ZBarDecoder zBarDecoder = new ZBarDecoder();
			String result = zBarDecoder.decodeCrop(rotatedData, size.width, size.height, mCropRect.left, mCropRect.top, mCropRect.width(), mCropRect.height());

			if (!TextUtils.isEmpty(result)) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				releaseCamera();
				barcodeScanned = true;

				Log.d("扫码", "onPreviewFrame: " + result);
				handleDecode(result);
//				Intent resultIntent = new Intent(ScanCaptureAct.this, MainActivity.class);
////				Intent resultIntent = new Intent();
//				resultIntent.putExtra("RQ_CODE", result);
////				startActivity(resultIntent);
//				// 设置结果，并进行传送
////				ScanCaptureAct.this.setResult(RESULT_OK, resultIntent);
//				startActivityForResult(resultIntent,RESULT_OK);
//				finish();

			}
		}
	};

	public void handleDecode(String result) {

		Intent resultIntent = new Intent(ScanCaptureAct.this, WebViewActivity.class);
		resultIntent.putExtra("RQ_CODE", result);
		// 设置结果，并进行传送
		startActivity(resultIntent);
//		startActivityForResult(resultIntent,RESULT_OK);

		// Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
		// .show();
		finish();

	}

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	/**
	 */
	private void initCrop() {
		int cameraWidth = mCameraManager.getCameraResolution().y;
		int cameraHeight = mCameraManager.getCameraResolution().x;

		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		int x = cropLeft * cameraWidth / containerWidth;
		int y = cropTop * cameraHeight / containerHeight;

		int width = cropWidth * cameraWidth / containerWidth;
		int height = cropHeight * cameraHeight / containerHeight;

		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}

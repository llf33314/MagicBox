/*
 * Barebones implementation of displaying camera preview.
 * 
 * Created by lisah0 on 2012-02-24
 */
package com.zbar.scan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private PreviewCallback previewCallback;
	private AutoFocusCallback autoFocusCallback;
	private Context context;

	private Paint paint;
	
	
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera, PreviewCallback previewCb, AutoFocusCallback autoFocusCb) {
		super(context);
		mCamera = camera;
		this.context=context;
		previewCallback = previewCb;
		autoFocusCallback = autoFocusCb;
		paint = new Paint();
		/*
		 * Set camera to continuous focus if supported, otherwise use software
		 * auto-focus. Only works for API level >=9.
		 */
		/*
		 * Camera.Parameters parameters = camera.getParameters(); for (String f
		 * : parameters.getSupportedFocusModes()) { if (f ==
		 * Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
		 * mCamera.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		 * autoFocusCallback = null; break; } }
		 */

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);

		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			mCamera.setPreviewDisplay(holder);
			//doDraw();
		} catch (IOException e) {
			Log.d("DBG", "Error setting camera preview: " + e.getMessage());
		}
		
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Camera preview released in activity
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		/*
		 * If your preview can change or rotate, take care of those events here.
		 * Make sure to stop the preview before resizing or reformatting it.
		 */
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		try {
			// Hard code camera surface rotation 90 degs to match Activity view
			// in portrait
//			mCamera.setDisplayOrientation(90);

			mCamera.setPreviewDisplay(mHolder);
			mCamera.setPreviewCallback(previewCallback);
			mCamera.startPreview();
			mCamera.autoFocus(autoFocusCallback);
		} catch (Exception e) {
			Log.d("DBG", "Error starting camera preview: " + e.getMessage());
		}
		
	}
	
	
	

	public void doDraw(){
		int screenWidth = getScreenWidth((Activity)context);
		int screenHeight = getScreenHeight((Activity)context);
		Canvas canvas = mHolder.lockCanvas();
		paint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, screenWidth, screenHeight/40, paint);
		
		mHolder.unlockCanvasAndPost(canvas);
	}

	/*@Override
	protected void onDraw(Canvas canvas) {
		int screenWidth=SystemUtils.getScreenWidth((Activity)context);
		int screenHeight=SystemUtils.getScreenHeight((Activity)context);
		//paint.setColor(resultColor : maskColor);
		paint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, screenWidth, screenHeight/40, paint);
		
		
		super.onDraw(canvas);
	}*/
	public static int getScreenWidth(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;// 获取屏幕分辨率宽度
	}
	public static int getScreenHeight(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;// 获取屏幕分辨率宽度
	}
	
}

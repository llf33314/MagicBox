package com.gt.magicbox.camera;

import android.content.Context;

import com.synodata.scanview.view.CodePreview;
import com.synodata.scanview.view.Preview$IDecodeListener;

/**
 * Description:
 * Created by jack-lin on 2017/11/28 0028.
 * Buddha bless, never BUG!
 */

public class CodeCameraManager {
    private Context context;
    private CodePreview codePreview;
    private Preview$IDecodeListener decodeListener;
    private static final int MSG_RESULT = 1;
    private static final int MSG_SCENE = 4;
    private static final int BUZZER_ON = 166661;
    private static final int BUZZER_OFF = 2266666;

    public CodeCameraManager(Context context, CodePreview codePreview, Preview$IDecodeListener decodeListener) {
        this.context = context;
        this.codePreview = codePreview;
        this.decodeListener = decodeListener;
    }

    public void initCamera() {
        if (decodeListener != null) {
            codePreview.setDecodeListener(decodeListener);
        }
        codePreview.initPara(1600, 1200);
        codePreview.connect(context);
        codePreview.startScanning();
    }

    public void releaseCamera() {
        if (codePreview.isScanning() == true) {
            codePreview.stopScanning();

        }
        codePreview.disconnect();
    }
    public void startScanningCamera(){
        if (codePreview!=null)
        {
            codePreview.startScanning();
        }
    }
    public void stopScanningCamera(){
        if (codePreview!=null)
        {
            codePreview.stopScanning();
        }
    }
}

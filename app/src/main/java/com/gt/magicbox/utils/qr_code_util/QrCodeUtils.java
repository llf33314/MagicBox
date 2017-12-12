package com.gt.magicbox.utils.qr_code_util;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.gt.magicbox.utils.commonutil.LogUtils;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.ByteArrayOutputStream;

/**
 * Description:
 * Created by jack-lin on 2017/12/9 0009.
 * Buddha bless, never BUG!
 */

public class QrCodeUtils {
    public static String scagnningImage(Bitmap obmp) {
        String message="";
        int width = obmp.getWidth();
        int height = obmp.getHeight();
        int[] data = new int[width * height];
        obmp.getPixels(data, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result re = null;
        try {
            re = reader.decode(bitmap1);
            message=re.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }


        return message;
    }

}

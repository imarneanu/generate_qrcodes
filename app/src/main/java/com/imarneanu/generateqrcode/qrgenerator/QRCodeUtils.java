package com.imarneanu.generateqrcode.qrgenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import com.imarneanu.generateqrcode.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by imarneanu on 2/24/17.
 */
public class QRCodeUtils {
    private static final String TAG = QRCodeUtils.class.getSimpleName();

    public static Bitmap generateQRCodeBitmap(Context context, String url) {
        //Find screen size
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(url,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            return qrCodeEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static boolean saveQRCode(Bitmap bitmap, String fileName) {
        boolean saved = false;
        // path to /storage/emulated/0/media/qrCodes
        File folder = Utils.getFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        Log.v(TAG, folder.getAbsolutePath());
        File myPath = Utils.getFileLocation(fileName);
        Log.v(TAG, myPath.getAbsolutePath());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            saved = true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return saved;
    }
}

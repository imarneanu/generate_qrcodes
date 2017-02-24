package com.imarneanu.generateqrcode;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by imarneanu on 2/24/17.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    private static final int BUFFER = 2048;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void zip(ArrayList<String> files, String zipFileName) {
        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(new File(getFolder(), zipFileName));
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (String file : files) {
                Log.v("Compress", "Adding: " + file);
                FileInputStream fi = new FileInputStream(new File(getFolder(), file));
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static File getFolder() {
        return new File(Environment.getExternalStorageDirectory(), "/media/qrCodes");
    }

    public static File getFileLocation(String fileName) {
        return new File(getFolder(), fileName.concat(".png"));
    }

    public static File getZipFolder(String filename) {
        return new File(getFolder(), filename);
    }
}

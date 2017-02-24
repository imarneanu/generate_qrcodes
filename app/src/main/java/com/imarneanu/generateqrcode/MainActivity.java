package com.imarneanu.generateqrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mSaveQRCode;

    private String mQRInput;
    private Bitmap mQRBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button generateQRCode = (Button) findViewById(R.id.generate_qrCode);
        generateQRCode.setOnClickListener(this);

        mSaveQRCode = (Button) findViewById(R.id.save_qrCode);
        mSaveQRCode.setOnClickListener(this);
        mSaveQRCode.setEnabled(false);
    }

    private void generateQRCode() {
        EditText input = (EditText) findViewById(R.id.qr_input);
        mQRInput = input.getText().toString();
        String url = BuildConfig.BASE_URL.concat(mQRInput);
        Log.v(TAG, url);

        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
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
            mQRBitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) findViewById(R.id.qr_imageView);
            myImage.setImageBitmap(mQRBitmap);
            mSaveQRCode.setEnabled(true);
        } catch (WriterException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void saveQRCode() {
        // path to /storage/emulated/0/media/qrCodes
        File folder = new File(Environment.getExternalStorageDirectory(), "/media/qrCodes");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        Log.v(TAG, folder.getAbsolutePath());
        File myPath = new File(folder, mQRInput.concat(".png"));
        Log.v(TAG, myPath.getAbsolutePath());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            mQRBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generate_qrCode:
                generateQRCode();
                break;
            case R.id.save_qrCode:
                saveQRCode();
                break;
        }
    }
}

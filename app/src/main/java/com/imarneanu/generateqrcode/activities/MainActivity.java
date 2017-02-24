package com.imarneanu.generateqrcode.activities;

import com.imarneanu.generateqrcode.BuildConfig;
import com.imarneanu.generateqrcode.R;
import com.imarneanu.generateqrcode.Utils;
import com.imarneanu.generateqrcode.qrgenerator.QRCodeUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.qr_input)
    EditText mQRInputEditText;
    @BindView(R.id.qr_imageView)
    ImageView mQRImageView;
    @BindView(R.id.save_qrCode)
    Button mSaveQRCodeButton;

    private String mQRInput;
    private Bitmap mQRBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void generateQRCode() {
        // Get base url from preferences - if none saved, use the one defined in gradle.properties
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String baseUrl = sharedPreferences.getString("baseUrl", BuildConfig.BASE_URL);

        mQRInput = mQRInputEditText.getText().toString();
        String url = baseUrl.concat(mQRInput);
        Log.v(TAG, url);

        // Generate QR Code
        mQRBitmap = QRCodeUtils.generateQRCodeBitmap(this, url);
        // Show QR code
        mQRImageView.setImageBitmap(mQRBitmap);
        // Enable QR Code saving
        mSaveQRCodeButton.setEnabled(true);
    }

    private void saveQRCode() {
        boolean saved = QRCodeUtils.saveQRCode(mQRBitmap, mQRInput);
        Crouton.makeText(this, getString(saved ? R.string.qr_code_saved : R.string.qr_code_error),
                saved ? Style.CONFIRM : Style.ALERT).show();
    }

    @OnClick({R.id.generate_qrCode, R.id.save_qrCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generate_qrCode:
                Utils.hideKeyboard(this);
                generateQRCode();
                break;
            case R.id.save_qrCode:
                saveQRCode();
                break;
        }
    }
}

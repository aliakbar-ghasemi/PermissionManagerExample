package com.example.permissionmanagerexample;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import ir.ghasemi.permissionmanagement.PermissionManager;


public class MainActivity extends AppCompatActivity {

    private final PermissionManager permissionManager = PermissionManager.create(this);
    private static final int PERMISSION_REQUEST_CAMERA = 100;

    Button btnChangeTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showCameraPreview();

        btnChangeTheme = findViewById(R.id.btnChangeTheme);
        btnChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTheme();
            }
        });
    }
    private void changeTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're in day time
                Log.d("##TAG", "changeTheme: Night mode is not active, we're in day time");
                //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're at night!
                Log.d("##TAG", "changeTheme: Night mode is active, we're at night!");
                //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // We don't know what mode we're in, assume notnight
                Log.d("##TAG", "changeTheme: We don't know what mode we're in, assume notnight");
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.handlePermissionResult(requestCode, grantResults);
    }

    private void showCameraPreview() {
        permissionManager.with(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION)
                .onPermissionGranted(() -> {
                    Log.d("##TAG", "onPermissionGranted: ");
                })
                .onPermissionDenied(() -> {
                    Log.d("##TAG", "onPermissionDenied: ");
                })
                .onPermissionShowRationale(permissionRequest -> {
                    Log.d("##TAG", "onPermissionShowRationale: " + permissionRequest);
                })
                .request();
        // Handling the callbacks using the helper methods in the library...
        // permissionManager.with(Manifest.permission.CAMERA)
        //         .onPermissionGranted(startPermissionGrantedActivity(this, new Intent(this, CameraPreviewActivity.class)))
        //         .onPermissionDenied(showPermissionDeniedSnackbar(mLayout, "Camera permission request was denied.", "SETTINGS"))
        //         .onPermissionShowRationale(showPermissionShowRationaleSnackbar(mLayout, "Camera access is required to display the camera preview.", "OK"))
        //         .request();

        // Handling all three callbacks using a single custom handler...
        /*permissionManager.with(Manifest.permission.CAMERA)
                .usingRequestCode(PERMISSION_REQUEST_CAMERA)
                .onCallback(new OnPermissionCallback() {
                    @Override
                    public void onPermissionDenied() {
                        Log.d("##TAG", "onPermissionDenied: ");
                    }

                    @Override
                    public void onPermissionGranted() {
                        Log.d("##TAG", "onPermissionGranted: ");
                    }

                    @Override
                    public void onPermissionShowRationale(PermissionRequest permissionRequest) {
                        Log.d("##TAG", "onPermissionShowRationale: ");
                    }
                })
                .request();
         */
    }
}
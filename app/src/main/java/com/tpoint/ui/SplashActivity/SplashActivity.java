package com.tpoint.ui.SplashActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.tpoint.ui.MainActivity.MainActivity;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class SplashActivity extends AppCompatActivity implements SplashActivityView {

    private SplashActivityPresenterMgmt mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create presenter
        mPresenter = new SplashActivityPresenterMgmt(this);
        // start internet check followed by location check
        mPresenter.connectivityCheck(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
        mPresenter = null;
    }

    @Override
    public void noInternetAlert() {
        // show no internet connectivity alert
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // close activity
                    finish();
                }
                return true;
            }
        });
        dialog.setTitle("Internet Problem")
                .setMessage("Your are not connected to the internet.\nPlease make sure you are connected in order to use the app.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close activity
                        finish();
                    }
                }).show();
    }

    @Override
    public void checkLocationPermission() {
        // all good to proceed to main activity
        if (checkLocationPermissions()) mPresenter.proceed();
    }

    @Override
    public void nextActivity() {
        // start next activity
        startActivity(new Intent(this, MainActivity.class));
        // end SplashActivity
        finish();
    }


    /*
    create location permissions and request them is not granted
     */
    private boolean checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // request for permissions
                createLocationPermissionsRequest();
                return false;
            }
        }
        return true;
    }


    /*
    create location permission request to User
     */
    private void createLocationPermissionsRequest() {
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET
        }, 10);
    }


    /*
    handle permission request results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // go to next activity
            mPresenter.proceed();
        } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            // request location permissions from User
            createLocationPermissionsRequest();
        }
    }
}
